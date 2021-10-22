package tools.aqua.bgw.examples.maumau.entity

/**
 * The game stack with open cards
 */
class GameStack : MauMauStack() {
	
	/**
	 * Adds [card] on top of this stack
	 *
	 * @param card Card to be played and therefore added to this stack.
	 */
	fun playCard(card: MauMauCard) {
		cards.push(card)
	}
	
	/**
	 * Removes all but the topmost card from this stack and returns them as [List].
	 * Note: This function does not shuffle the returned cards.
	 *
	 * @return [List] of removed cards.
	 */
	fun shuffleBack(): List<MauMauCard> {
		//pop topmost card
		val saved = cards.pop()
		
		//save all remaining cards for return
		val tmp = cards.toList()
		
		//clear Stack and push topmost
		cards.clear()
		cards.push(saved)
		
		//return cards
		return tmp
	}
}