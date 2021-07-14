package examples.maumau.model

class MauMauCard(val cardValue: CardValue, val cardSuit: CardSuit) {
	override fun toString(): String {
		return "MauMauCard(cardValue=$cardValue, cardSuit=$cardSuit)"
	}
}