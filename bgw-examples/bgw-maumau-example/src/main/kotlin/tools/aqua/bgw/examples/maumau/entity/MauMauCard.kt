package tools.aqua.bgw.examples.maumau.entity

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
	override fun toString(): String = "MauMauCard(cardValue=$cardValue, cardSuit=$cardSuit)"
	
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is MauMauCard) return false
		
		if (cardValue != other.cardValue) return false
		if (cardSuit != other.cardSuit) return false
		
		return true
	}
	
	override fun hashCode(): Int {
		var result = cardValue.hashCode()
		result = 31 * result + cardSuit.hashCode()
		return result
	}
}