package examples.maumau.model

class MauMauHand {
	
	val cards = ArrayList<MauMauCard>()
	
	fun addCard(card: MauMauCard) {
		cards.add(card)
	}
	
	fun removeCard(card: MauMauCard): Boolean {
		return cards.remove(card)
	}
	
}