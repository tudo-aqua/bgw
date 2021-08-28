package util.stack

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import tools.aqua.bgw.util.Stack
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class SortShuffleTest: StackTestBase() {
	@Test
	@DisplayName("Test shuffle")
	fun testShuffle() {
		val orderedStack = Stack(IntRange(0, 1000).toList())
		val comparisonStack = Stack(IntRange(0, 1000).toList())
		
		orderedStack.shuffle()
		
		//size unchanged
		assertEquals(comparisonStack.size, orderedStack.size)
		
		//contents unchanged
		assertNotEquals(comparisonStack.peekAll(), orderedStack.peekAll())
		
		//order has changed
		assertTrue { orderedStack.peekAll().minus(comparisonStack.peekAll()).isEmpty() }
	}
	
	@Test
	@DisplayName("Test sort")
	fun testSort() {
		val orderedStack = Stack(IntRange(0, 1000).shuffled())
		val comparisonStack = Stack(IntRange(0, 1000).toList())
		
		orderedStack.sort(Comparator.comparingInt{it})
		
		assertEquals(comparisonStack.peekAll(), orderedStack.peekAll())
	}
}