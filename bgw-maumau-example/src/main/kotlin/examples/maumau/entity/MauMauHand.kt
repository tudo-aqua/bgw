package examples.maumau.entity

/**
 * Class representing a card hand containing a set of cards.
 */
class MauMauHand {
	/**
	 * The cards currently in the player's hand.
	 */
	val cards: ArrayList<MauMauCard> = ArrayList()
	
	/**
	 * Adds a card to this hand.
	 *
	 * @param card Card to be added.
	 */
	fun addCard(card: MauMauCard) {
		cards.add(card)
	}
	
	/**
	 * Removes a card from this hand.
	 *
	 * @param card Card to be removed.
	 */
	fun removeCard(card: MauMauCard): Boolean {
		return cards.remove(card)
	}
}