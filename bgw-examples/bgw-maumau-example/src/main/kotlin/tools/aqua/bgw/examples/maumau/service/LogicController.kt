package tools.aqua.bgw.examples.maumau.service

import tools.aqua.bgw.examples.maumau.entity.CardSuit
import tools.aqua.bgw.examples.maumau.entity.CardValue
import tools.aqua.bgw.examples.maumau.entity.MauMauCard
import tools.aqua.bgw.examples.maumau.entity.MauMauGame
import tools.aqua.bgw.examples.maumau.net.GameActionMessage
import tools.aqua.bgw.examples.maumau.net.InitGameMessage
import tools.aqua.bgw.examples.maumau.view.Refreshable
import java.util.*

/**
 * Controller managing game actions.
 */
class LogicController(val view: Refreshable) {
	
	/**
	 * Current game instance.
	 */
	var game: MauMauGame = MauMauGame()
	
	/**
	 * Network service instance.
	 */
	val networkService: NetworkService = NetworkService(this)
	
	/**
	 *
	 */
	var isHost: Boolean = false
	
	/**
	 *
	 */
	var isOnline: Boolean = false
	
	/**
	 * Sets up a new game.
	 */
	fun newGame() {
		val cards = generateMauMauCards(CardSuit.allSuits(), CardValue.shortDeck()).shuffled()
		
		initGame(
			drawStack = cards.subList(11, cards.size),
			gameStack = cards[0],
			hostCards = cards.subList(1,6),
			opponentCards = cards.subList(6, 11)
		)
		
		networkService.sendInit(game)
	}
	
	/**
	 * Initializes game with given cards.
	 *
	 * @param drawStack Draw stack cards.
	 * @param gameStack First card on Game Stack.
	 * @param hostCards Hand of host player.
	 * @param opponentCards Hand of opponent (i.e. you, if joined).
	 */
	fun initGame(
		drawStack: List<MauMauCard>,
		gameStack: MauMauCard,
		hostCards: List<MauMauCard>,
		opponentCards: List<MauMauCard>
	) {
		game = MauMauGame()
		
		game.mauMauCards.addAll(drawStack + gameStack + hostCards + opponentCards)
		
		game.drawStack.cards.addAll(drawStack)
		game.gameStack.cards.add(gameStack)
		game.nextSuit = gameStack.cardSuit
		
		game.players[0].hand.cards.addAll(if(isHost) hostCards else opponentCards)
		game.players[1].hand.cards.addAll(if(!isHost) hostCards else opponentCards)
		
		view.refreshAll()
	}
	
	/**
	 * Processes [InitGameMessage] to initialize game conditions transmitted by host.
	 *
	 * @param message Init conditions.
	 */
	fun initGame(message: InitGameMessage) {
		initGame(
			drawStack = message.drawStack.map { SerializationUtil.deserializeMauMauCard(it) },
			gameStack = SerializationUtil.deserializeMauMauCard(message.gameStack.first()),
			hostCards = message.hostCards.map { SerializationUtil.deserializeMauMauCard(it) },
			opponentCards = message.yourCards.map { SerializationUtil.deserializeMauMauCard(it) }
		)
	}
	
	/**
	 * Current player draws a card from drawStack.
	 * Advances player.
	 */
	fun drawCard() {
		val card = game.drawStack.drawCard()
		game.players[0].hand.addCard(card)
		
		if (game.drawStack.isEmpty()) {
			game.shuffleGameStackBack()
			view.refreshGameStackShuffledBack() //TODO: Send shuffled stack on network
		}
		
		networkService.sendCardDrawn(card)
		view.refreshCardDrawn(card, true)
		
		networkService.sendEndTurn()
		view.refreshAdvancePlayer()
	}
	
	/**
	 * Select a suit by jack effect, advances player
	 */
	fun selectSuit(suit: CardSuit) {
		/*game.nextSuit = suit
		view.refreshSuitSelected()
		
		game.advancePlayer()
		view.refreshAdvancePlayer()*/
	}
	
	/**
	 * Play a card to the GameStack if allowed, advances player
	 */
	fun playCard(card: MauMauCard, animated: Boolean, ) : Boolean {
		if (!checkRules(card))
			return false
		
		val currentPlayer = game.players[0]
		var advance = true
		
		currentPlayer.hand.removeCard(card)
		
		when (card.cardValue) {
			/*CardValue.SEVEN ->
				playSevenEffect(card, animated)*/
			
			CardValue.EIGHT -> {
				playNoEffect(card, animated)
				advance = false
			}
			
			/*CardValue.JACK -> {
				playJackEffect(card, animated)
				advance = false
			}*/
			
			else ->
				playNoEffect(card, animated)
		}
		networkService.sendCardPlayed(card)
		
		if(currentPlayer.hand.cards.isEmpty()) {
			networkService.sendEndGame()
			view.refreshEndGame(currentPlayer)
			return true
		}
		
		if (advance) {
			networkService.sendEndTurn()
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
		view.refreshCardPlayed(card, animated, true)
	}
	
	/**
	 * Play a jack card and show suit selection
	 */
	private fun playJackEffect(card: MauMauCard, animated: Boolean) {
		/*playNoEffect(card, animated)
		view.showJackEffectSelection()*/
	}
	
	/**
	 * Play a seven card and let opponent draw two
	 */
	private fun playSevenEffect(card: MauMauCard, animated: Boolean) {
		/*playNoEffect(card, animated)
		
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
		view.refreshCardsDrawn(game.otherPlayer, cards)*/
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
		/*val card = calculateHint()
		
		if (card == null)
			view.refreshHintDrawCard()
		else
			view.refreshHintPlayCard(card)*/
	}
	
	/**
	 * Calculates a hint for the next turn.
	 *
	 * @return A [MauMauCard] to play or `null` to draw.
	 */
	private fun calculateHint(): MauMauCard? {
		/*val cards = game.currentPlayer.hand.cards
		
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
		
		//Hint 5: Take card*/
		return null
	}
	
	fun doTurn(action: GameActionMessage) {
		error("Not yet implemented")
	}
	
	
	//region helper
	
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
	//endregion
}