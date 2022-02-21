package tools.aqua.bgw.examples.maumau.entity

/**
 * The draw stack with hidden cards.
 */
class DrawStack : MauMauStack() {
	/**
	 * Pops the first card from the stack.
	 */
	fun drawCard(): MauMauCard = cards.pop()
	
	/**
	 * Shuffles given cards and adds them to the stack. Returns new permutation for network communication.
	 *
	 * @param cards Cards to be shuffled.
	 *
	 * @return New permutation of cards on the stack.
	 */
	fun shuffleBack(cards: List<MauMauCard>) : List<MauMauCard> {
		val stack = cards.shuffled()
		this.cards.addAll(stack)
		return stack
	}
}