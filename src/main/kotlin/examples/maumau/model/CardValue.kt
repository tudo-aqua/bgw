package examples.maumau.model

import java.util.*

enum class CardValue {
	ACE,
	TWO,
	THREE,
	FOUR,
	FIVE,
	SIX,
	SEVEN,
	EIGHT,
	NINE,
	TEN,
	JACK,
	QUEEN,
	KING;
	
	companion object {
		fun completeDeck(): EnumSet<CardValue> {
			return EnumSet.of(ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING)
		}
		
		fun shortDeck(): EnumSet<CardValue> {
			return EnumSet.of(ACE, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING)
		}
	}
}