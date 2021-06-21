package examples.maumau.model

class MauMauGame {
	
	val mauMauCards: MutableCollection<MauMauCard> = ArrayList()
	
	val drawStack = DrawStack()
	
	val gameStack = GameStack()
	
	var currentPlayer = MauMauPlayer()
	var otherPlayer = MauMauPlayer()
	
	var nextSuit = CardSuit.HEARTS
	
	fun shuffleGameStackBack() {
		drawStack.shuffleBack(gameStack.shuffleBack())
	}
	
	fun advancePlayer() {
		val tmp = currentPlayer
		currentPlayer = otherPlayer
		otherPlayer = tmp
	}
}