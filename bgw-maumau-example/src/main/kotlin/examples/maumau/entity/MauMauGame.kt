package examples.maumau.entity

class MauMauGame {
	
	val mauMauCards: MutableCollection<MauMauCard> = ArrayList()
	
	val drawStack: DrawStack = DrawStack()
	
	val gameStack: GameStack = GameStack()
	
	var currentPlayer: MauMauPlayer = MauMauPlayer()
	var otherPlayer: MauMauPlayer = MauMauPlayer()
	
	var nextSuit: CardSuit = CardSuit.HEARTS
	
	fun shuffleGameStackBack() {
		drawStack.shuffleBack(gameStack.shuffleBack())
	}
	
	fun advancePlayer() {
		val tmp = currentPlayer
		currentPlayer = otherPlayer
		otherPlayer = tmp
	}
}