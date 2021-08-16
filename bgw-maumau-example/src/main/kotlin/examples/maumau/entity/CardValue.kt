package examples.maumau.entity

import java.util.*

/**
 * Enum over all card values.
 */
enum class CardValue {
	/**
	 * Ace.
	 */
	ACE,
	
	/**
	 * Seven.
	 */
	SEVEN,
	
	/**
	 * Eight.
	 */
	EIGHT,
	
	/**
	 * Nine.
	 */
	NINE,
	
	/**
	 * Ten.
	 */
	TEN,
	
	/**
	 * Jack.
	 */
	JACK,
	
	/**
	 * Queen.
	 */
	QUEEN,
	
	/**
	 * King.
	 */
	KING;
	
	companion object {
		fun shortDeck(): EnumSet<CardValue> {
			return EnumSet.of(ACE, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING)
		}
	}
}