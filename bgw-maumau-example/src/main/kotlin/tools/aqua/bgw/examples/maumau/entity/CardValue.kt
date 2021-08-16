package tools.aqua.bgw.examples.maumau.entity

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
	 * Two.
	 */
	TWO,
	
	/**
	 * Three.
	 */
	THREE,
	
	/**
	 * Four.
	 */
	FOUR,
	
	/**
	 * Five.
	 */
	FIVE,
	
	/**
	 * Six.
	 */
	SIX,
	
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
		@Suppress("unused")
		fun completeDeck(): EnumSet<CardValue> {
			return EnumSet.of(ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING)
		}
		
		fun shortDeck(): EnumSet<CardValue> {
			return EnumSet.of(ACE, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING)
		}
	}
}