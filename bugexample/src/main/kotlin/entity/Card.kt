package entity

/**
 * Entity to represent a card.
 *
 * @property suit The [CardSuit] of the card.
 * @property value The [CardValue] of the card.
 * @constructor Creates a new Card.
 */
data class Card(val suit: CardSuit, val value: CardValue) {
    override fun toString(): String {
        return "$suit$value"
    }
}