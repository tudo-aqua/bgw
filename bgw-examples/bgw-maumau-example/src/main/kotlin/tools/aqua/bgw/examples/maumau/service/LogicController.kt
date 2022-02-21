package tools.aqua.bgw.examples.maumau.service

import tools.aqua.bgw.examples.maumau.entity.CardSuit
import tools.aqua.bgw.examples.maumau.entity.CardValue
import tools.aqua.bgw.examples.maumau.entity.MauMauCard
import tools.aqua.bgw.examples.maumau.entity.MauMauGame
import tools.aqua.bgw.examples.maumau.service.messages.InitGameMessage
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
	
	//region init
	/**
	 * Sets up a new game with cloned players.
	 */
	fun newGame(game: MauMauGame) {
		newGame(game.players[0].name, game.players[1].name)
	}
	
	/**
	 * Sets up a new game.
	 */
	fun newGame(player1: String = "Player 1", player2: String = "Player 2") {
		val cards = generateMauMauCards(CardSuit.allSuits(), CardValue.shortDeck()).shuffled()
		
		initGame(
			player1 = player1,
			player2 = player2,
			drawStack = cards.subList(11, cards.size),
			gameStack = cards[0],
			hostCards = cards.subList(1, 6),
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
	private fun initGame(
		player1: String,
		player2: String,
		drawStack: List<MauMauCard>,
		gameStack: MauMauCard,
		hostCards: List<MauMauCard>,
		opponentCards: List<MauMauCard>
	) {
		game = MauMauGame(player1, player2)
		
		game.mauMauCards.addAll(drawStack + gameStack + hostCards + opponentCards)
		
		game.drawStack.cards.addAll(drawStack)
		game.gameStack.cards.add(gameStack)
		game.nextSuit = gameStack.cardSuit
		
		game.players[0].hand.cards.addAll(if (isHost) hostCards else opponentCards)
		game.players[1].hand.cards.addAll(if (!isHost) hostCards else opponentCards)
		
		view.refreshAll()
	}
	
	/**
	 * Processes [InitGameMessage] to initialize game conditions transmitted by host.
	 *
	 * @param message Init conditions.
	 */
	fun initGame(message: InitGameMessage) {
		initGame(
			player1 = message.players[0],
			player2 = message.players[1],
			drawStack = message.drawStack.map { SerializationUtil.deserializeMauMauCard(it) },
			gameStack = SerializationUtil.deserializeMauMauCard(message.gameStack.first()),
			hostCards = message.hostCards.map { SerializationUtil.deserializeMauMauCard(it) },
			opponentCards = message.yourCards.map { SerializationUtil.deserializeMauMauCard(it) }
		)
	}
	//endregion
	
	/**
	 * Current player draws a card from drawStack.
	 * Advances player.
	 */
	fun drawCard(isCurrentPlayer: Boolean, advance: Boolean) {
		val player = getPlayer(isCurrentPlayer)
		val card = game.drawStack.drawCard()
		
		player.hand.addCard(card)
		view.refreshCardDrawn(card, isCurrentPlayer)
		
		if (isOnline && isCurrentPlayer) {
			networkService.sendCardDrawn()
		}
		
		if (game.drawStack.isEmpty()) {
			game.shuffleGameStackBack()
			view.refreshGameStackShuffledBack() //TODO: Send shuffled stack on network
		}
		
		if (advance) {
			if (isOnline) {
				networkService.sendEndTurn()
				view.refreshAdvanceOnlinePlayer()
			} else {
				advanceSwapPlayers()
			}
		}
	}
	
	/**
	 * Select a suit by jack effect, advances player
	 */
	fun selectSuit(suit: CardSuit, isCurrentPlayer: Boolean) {
		game.nextSuit = suit
		view.refreshSuitSelected()
		
		if (isCurrentPlayer) {
			if (isOnline) {
				networkService.sendSuitSelected(suit)
				networkService.sendEndTurn()
				view.refreshAdvanceOnlinePlayer()
			} else {
				advanceSwapPlayers()
			}
		}
	}
	
	/**
	 * Play a card to the GameStack if allowed, advances player
	 */
	fun playCard(card: MauMauCard, animated: Boolean, isCurrentPlayer: Boolean): Boolean {
		if (isCurrentPlayer && !checkRules(card))
			return false
		
		val player = getPlayer(isCurrentPlayer)
		var advance = true
		
		player.hand.removeCard(card)
		if (isCurrentPlayer) {
			networkService.sendCardPlayed(card)
		}
		
		when (card.cardValue) {
			CardValue.SEVEN -> {
				playSevenEffect(card, animated, isCurrentPlayer)
			}
			
			CardValue.EIGHT -> {
				playNoEffect(card, animated, isCurrentPlayer)
				advance = false
			}
			
			CardValue.JACK -> {
				playJackEffect(card, animated, isCurrentPlayer)
				advance = false
			}
			
			else ->
				playNoEffect(card, animated, isCurrentPlayer)
		}
		
		if (isCurrentPlayer) {
			if (player.hand.cards.isEmpty()) {
				networkService.sendEndGame()
				view.refreshEndGame(player.name)
				return true
			}
			
			if (advance) {
				if (isOnline) {
					networkService.sendEndTurn()
					view.refreshAdvanceOnlinePlayer()
				} else {
					advanceSwapPlayers()
				}
			} else {
				view.refreshPlayAgain()
			}
		}
		
		return true
	}
	
	/**
	 * Play a card without effect
	 */
	private fun playNoEffect(card: MauMauCard, animated: Boolean, isCurrentPlayer: Boolean) {
		game.nextSuit = card.cardSuit
		game.gameStack.playCard(card)
		view.refreshCardPlayed(card, animated, isCurrentPlayer)
	}
	
	/**
	 * Play a jack card and show suit selection
	 */
	private fun playJackEffect(card: MauMauCard, animated: Boolean, isCurrentPlayer: Boolean) {
		playNoEffect(card, animated, isCurrentPlayer)
		
		if (isCurrentPlayer) {
			view.showJackEffectSelection()
		}
	}
	
	/**
	 * Play a seven card and let opponent draw two
	 */
	private fun playSevenEffect(card: MauMauCard, animated: Boolean, isCurrentPlayer: Boolean) {
		playNoEffect(card, animated, isCurrentPlayer)
		
		if (isCurrentPlayer) {
			if (isOnline) {
				networkService.sendDrawTwoRequest()
			} else {
				drawCard(isCurrentPlayer = false, advance = false)
				drawCard(isCurrentPlayer = false, advance = false)
			}
		}
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
	
	private fun advanceSwapPlayers() {
		game.players.reverse()
		view.refreshSwapPlayers()
	}
	
	/**
	 * Calculates a hint for the next turn.
	 *
	 * @return A [MauMauCard] to play or `null` to draw.
	 */
	private fun calculateHint(): MauMauCard? {
		val cards = getPlayer(true).hand.cards
		
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
	
	//region helper
	/**
	 * Returns player instance
	 */
	private fun getPlayer(isCurrentPlayer: Boolean) = game.players[if (isCurrentPlayer) 0 else 1]
	
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