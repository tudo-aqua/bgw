package tools.aqua.bgw.examples.maumau.entity

/**
 * Class representing a game of MauMau.
 */
class MauMauGame {
	/**
	 * Collection of all cards in the game.
	 */
	val mauMauCards: MutableCollection<MauMauCard> = ArrayList()
	
	/**
	 * Players.
	 */
	val players = mutableListOf(MauMauPlayer(), MauMauPlayer())
	
	/**
	 * The draw stack.
	 */
	val drawStack: DrawStack = DrawStack()
	
	/**
	 * The game stack.
	 */
	val gameStack: GameStack = GameStack()
	
	/**
	 * Next suit to be placed. May differ from topmost game stack card due to jack selection.
	 */
	var nextSuit: CardSuit = CardSuit.HEARTS
	
	/**
	 * Shuffles cards from game stack back to draw stack.
	 */
	fun shuffleGameStackBack() {
		drawStack.shuffleBack(gameStack.shuffleBack())
	}
}