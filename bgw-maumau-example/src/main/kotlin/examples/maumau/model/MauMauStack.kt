package examples.maumau.model

import java.util.*

open class MauMauStack {
	val cards: Stack<MauMauCard> = Stack<MauMauCard>()
	
	fun size(): Int = cards.size
	
	fun isEmpty(): Boolean = cards.size == 0
	
	fun peek(): MauMauCard = cards.peek()
}