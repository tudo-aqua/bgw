package tools.aqua.bgw.examples.maumau.entity

/**
 * Class representing a player.
 */
class MauMauPlayer(val name : String) {
	
	/**
	 * Instance of the player`s hand cards.
	 */
	val hand: MauMauHand = MauMauHand()
}