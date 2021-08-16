package tools.aqua.bgw.examples.maumau.entity

/**
 * The draw stack with hidden cards.
 */
class DrawStack : MauMauStack() {
	
	fun drawCard(): MauMauCard = cards.pop()
	
	fun shuffleBack(cards: List<MauMauCard>) {
		this.cards.addAll(cards.shuffled())
	}
}