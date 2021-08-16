package tools.aqua.bgw.examples.maumau.entity

import java.util.*

/**
 * Abstract baseclass for the stacks in the game.
 */
abstract class MauMauStack {
	/**
	 * The cards currently in this stack.
	 */
	val cards: Stack<MauMauCard> = Stack<MauMauCard>()
	
	/**
	 * Returns the amount of cards currently in this stack.
	 *
	 * @return Number of cards in this stack.
	 */
	fun size(): Int = cards.size
	
	/**
	 * Returns whether this stack is empty.
	 *
	 * @return `true` if there are no cards in this stack, i.e. [size] = 0, `false` otherwise.
	 */
	fun isEmpty(): Boolean = size() == 0
	
	/**
	 * Returns the topmost card of this stack without removing it.
	 *
	 * @return The topmost card.
	 */
	fun peek(): MauMauCard = cards.peek()
}