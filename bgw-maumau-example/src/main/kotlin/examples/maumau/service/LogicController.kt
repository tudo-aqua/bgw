package examples.maumau.service

import examples.maumau.entity.CardSuit
import examples.maumau.entity.CardValue
import examples.maumau.entity.MauMauCard
import examples.maumau.entity.MauMauGame
import examples.maumau.view.Refreshable
import java.util.*
import kotlin.math.min

/**
 * Controller managing game actions.
 */
class LogicController(private val view: Refreshable) {
	
	/**
	 * Current game instance.
	 */
	var game: MauMauGame = MauMauGame()
	
	/**
	 * Sets up a new game.
	 */
	fun newGame() {
		game = MauMauGame()
		
		//generate cards
		game.mauMauCards.addAll(generateMauMauCards(CardSuit.allSuits(), CardValue.shortDeck()))
		
		//fill drawStack
		game.drawStack.shuffleBack(game.mauMauCards.toMutableList())
		
		//draw 5 for each player
		for (i in 0..4) {
			game.currentPlayer.hand.addCard(game.drawStack.drawCard())
			game.otherPlayer.hand.addCard(game.drawStack.drawCard())
		}
		
		//initial card for playStack
		game.gameStack.playCard(game.drawStack.drawCard().also { x -> game.nextSuit = x.cardSuit })
		
		//refresh whole view
		view.refreshAll()
	}
	
	/**
	 * Generates a full set of MauMauCards.
	 */
	private fun generateMauMauCards(suits: EnumSet<CardSuit>, values: EnumSet<CardValue>): List<MauMauCard> {
		val cards: MutableList<MauMauCard> = ArrayList(suits.size * values.size)
		
		for (suit in suits) {
			for (value in values) {
				cards.add(MauMauCard(value, suit))
			}
		}
		
		return cards
	}
	
	/**
	 * Current player draws a card from drawStack.
	 * Advances player.
	 */
	fun drawCard() {
		val currentPlayer = game.currentPlayer
		
		val card = game.drawStack.drawCard()
		currentPlayer.hand.addCard(card)
		
		if (game.drawStack.isEmpty()) {
			game.shuffleGameStackBack()
			view.refreshGameStackShuffledBack()
		}
		
		view.refreshCardsDrawn(currentPlayer, mutableListOf(card))
		
		game.advancePlayer()
		view.refreshAdvancePlayer()
	}
	
	/**
	 * Select a suit by jack effect, advances player
	 */
	fun selectSuit(suit: CardSuit) {
		game.nextSuit = suit
		view.refreshSuitSelected()
		
		game.advancePlayer()
		view.refreshAdvancePlayer()
	}
	
	/**
	 * Play a card to the GameStack if allowed, advances player
	 */
	fun playCard(card: MauMauCard, animated: Boolean): Boolean {
		if (!checkRules(card))
			return false
		
		val currentPlayer = game.currentPlayer
		var advance = true
		
		currentPlayer.hand.removeCard(card)
		
		when (card.cardValue) {
			CardValue.SEVEN ->
				playSevenEffect(card, animated)
			
			CardValue.EIGHT -> {
				playNoEffect(card, animated)
				advance = false
			}
			
			CardValue.JACK -> {
				playJackEffect(card, animated)
				advance = false
			}
			
			else ->
				playNoEffect(card, animated)
		}
		
		if (advance) {
			game.advancePlayer()
			view.refreshAdvancePlayer()
		} else {
			view.refreshPlayAgain()
		}
		
		return true
	}
	
	/**
	 * Play a card without effect
	 */
	private fun playNoEffect(card: MauMauCard, animated: Boolean) {
		game.nextSuit = card.cardSuit
		game.gameStack.playCard(card)
		view.refreshCardPlayed(card, animated)
	}
	
	/**
	 * Play a jack card and show suit selection
	 */
	private fun playJackEffect(card: MauMauCard, animated: Boolean) {
		playNoEffect(card, animated)
		view.showJackEffectSelection()
	}
	
	/**
	 * Play a seven card and let opponent draw two
	 */
	private fun playSevenEffect(card: MauMauCard, animated: Boolean) {
		playNoEffect(card, animated)
		
		//information for refresh
		val cards = mutableListOf<MauMauCard>()
		
		//draw 2
		repeat(min(game.drawStack.size(), 2)) {
			val tmpCard = game.drawStack.drawCard()
			game.otherPlayer.hand.addCard(tmpCard)
			cards.add(tmpCard)
		}
		
		if (game.drawStack.isEmpty()) {
			game.shuffleGameStackBack()
			view.refreshGameStackShuffledBack()
		}
		view.refreshCardsDrawn(game.otherPlayer, cards)
	}
	
	/**
	 * Checks play rules
	 */
	fun checkRules(card: MauMauCard): Boolean =
		card.cardValue == CardValue.JACK
				|| card.cardSuit == game.nextSuit
				|| card.cardValue == game.gameStack.peek().cardValue
	
	/**
	 * Shows a hint for the next turn.
	 */
	fun showHint() {
		val card = calculateHint()
		
		if (card == null)
			view.refreshHintDrawCard()
		else
			view.refreshHintPlayCard(card)
	}
	
	/**
	 * Calculates a hint for the next turn.
	 *
	 * @return A [MauMauCard] to play or `null` to draw.
	 */
	private fun calculateHint(): MauMauCard? {
		val cards = game.currentPlayer.hand.cards
		
		//Hint 1: Play 8
		var hint: List<MauMauCard> = cards.filter { it.cardValue == CardValue.EIGHT && checkRules(it) }
		if (hint.isNotEmpty())
			return hint.first()
		
		//Hint 2: Play 7
		hint = cards.filter { it.cardValue == CardValue.SEVEN && checkRules(it) }
		if (hint.isNotEmpty())
			return hint.first()
		
		//Hint 3: Play regular card
		hint = cards.filter { it.cardValue != CardValue.JACK && checkRules(it) }
		if (hint.isNotEmpty())
			return hint.first()
		
		//Hint 4: Play Jack
		hint = cards.filter { it.cardValue == CardValue.JACK && checkRules(it) }
		if (hint.isNotEmpty())
			return hint.first()
		
		//Hint 5: Take card
		return null
	}
}