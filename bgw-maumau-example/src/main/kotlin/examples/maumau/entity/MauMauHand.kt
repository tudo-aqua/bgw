package examples.maumau.entity

class MauMauHand {
	
	val cards: ArrayList<MauMauCard> = ArrayList<MauMauCard>()
	
	fun addCard(card: MauMauCard) {
		cards.add(card)
	}
	
	fun removeCard(card: MauMauCard): Boolean {
		return cards.remove(card)
	}
	
}