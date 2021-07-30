package examples.maumau.entity

class GameStack : MauMauStack() {
	
	fun playCard(card: MauMauCard) {
		cards.push(card)
	}
	
	fun shuffleBack(): List<MauMauCard> {
		val saved = cards.pop()
		val tmp = cards.toList()
		
		cards.clear()
		cards.push(saved)
		
		return tmp
	}
}