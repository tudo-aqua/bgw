package examples.maumau.entity

class DrawStack : MauMauStack() {
	
	fun drawCard(): MauMauCard = cards.pop()
	
	fun shuffleBack(cards: List<MauMauCard>) {
		this.cards.addAll(cards.shuffled())
	}
}