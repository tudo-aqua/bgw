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
	 * Deserializes exchange format to [MauMauCard]
	 */
	constructor(cardValue: String, cardSuit: String) : this(CardValue.valueOf(cardValue),	CardSuit.valueOf(cardSuit))
	
	/**
	 * Serializes [MauMauCard] for exchange format
	 */
	fun serialize() : String = "${cardValue}_${cardSuit}"
	
	/**
	 * Converts card into readable string representation.
	 */
	override fun toString(): String = "MauMauCard(cardValue=$cardValue, cardSuit=$cardSuit)"
}