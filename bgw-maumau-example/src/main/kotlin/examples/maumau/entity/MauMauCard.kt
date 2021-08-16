package examples.maumau.entity

/**
 * Class representing a single card in the game.
 *
 * @constructor Creates a card with given [cardValue] and [cardSuit].
 *
 * @param cardValue Value of this card.
 * @param cardSuit Suit of this card.
 */
class MauMauCard(val cardValue: CardValue, val cardSuit: CardSuit) {
	/**
	 * Converts card into readable string representation.
	 */
	override fun toString(): String {
		return "MauMauCard(cardValue=$cardValue, cardSuit=$cardSuit)"
	}
}