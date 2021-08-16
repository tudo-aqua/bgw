package tools.aqua.bgw.examples.maumau.entity

import java.util.*

/**
 * Enum over all card suits.
 */
enum class CardSuit {
	/**
	 * Clubs.
	 */
	CLUBS,
	
	/**
	 * Diamonds.
	 */
	DIAMONDS,
	
	/**
	 * Hearts.
	 */
	HEARTS,
	
	/**
	 * Spades.
	 */
	SPADES;
	
	companion object {
		/**
		 * Returns complete [EnumSet].
		 *
		 * @return The complete [EnumSet].
		 */
		fun allSuits(): EnumSet<CardSuit> {
			return EnumSet.of(CLUBS, SPADES, HEARTS, DIAMONDS)
		}
	}
}