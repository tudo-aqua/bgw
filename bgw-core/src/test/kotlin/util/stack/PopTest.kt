package util.stack

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PopTest : StackTestBase() {
	
	@Test
	@DisplayName("Test pop on empty Stack")
	fun testPopOnEmptyStack() {
		assertEquals(0, emptyStack.size)
		assertFailsWith<NoSuchElementException> { emptyStack.pop() }
	}
	
	@Test
	@DisplayName("Test popOrNull on empty Stack")
	fun testPopOrNullOnEmptyStack() {
		assertEquals(0, emptyStack.size)
		assertEquals(null, emptyStack.popOrNull())
	}
	
	@Test
	@DisplayName("Test pop")
	fun testPop() {
		assertEquals(5, stack.size)
		assertEquals(order[0], stack.pop())
		assertEquals(4, stack.size)
		assertEquals(order[1], stack.pop())
		assertEquals(3, stack.size)
		assertEquals(order[2], stack.pop())
		assertEquals(2, stack.size)
		assertEquals(order[3], stack.pop())
		assertEquals(1, stack.size)
		assertEquals(order[4], stack.pop())
		assertEquals(0, stack.size)
		assertFailsWith<NoSuchElementException> { stack.pop() }
	}
	
	@Test
	@DisplayName("Test popOrNull")
	fun testPopOrNull() {
		assertEquals(5, stack.size)
		assertEquals(order[0], stack.popOrNull())
		assertEquals(4, stack.size)
		assertEquals(order[1], stack.popOrNull())
		assertEquals(3, stack.size)
		assertEquals(order[2], stack.popOrNull())
		assertEquals(2, stack.size)
		assertEquals(order[3], stack.popOrNull())
		assertEquals(1, stack.size)
		assertEquals(order[4], stack.popOrNull())
		assertEquals(0, stack.size)
		assertEquals(null, stack.popOrNull())
	}
	
	@Test
	@DisplayName("Test popAll for all elements")
	fun testPopAllForAll() {
		assertEquals(5, stack.size)
		
		assertEquals(order, stack.popAll(order.size))
		
		assertEquals(0, stack.size)
	}
	
	@Test
	@DisplayName("Test popAll for some elements")
	fun testPopAllForSome() {
		assertEquals(5, stack.size)
		
		assertEquals(order.subList(0,3), stack.popAll(3))
		
		assertEquals(2, stack.size)
	}
	
	@Test
	@DisplayName("Test popAll for one element")
	fun testPopAllForOne() {
		assertEquals(5, stack.size)
		
		assertEquals(listOf(order[0]), stack.popAll(1))
		
		assertEquals(4, stack.size)
	}
	
	@Test
	@DisplayName("Test popAll for zero argument")
	fun testPopAllForZero() {
		assertEquals(5, stack.size)
		
		assertEquals(listOf(), stack.popAll(0))
		
		assertEquals(5, stack.size)
	}
	
	@Test
	@DisplayName("Test popAll for negative argument")
	fun testPopAllForNegative() {
		assertEquals(5, stack.size)
		
		assertFailsWith<IllegalArgumentException> { stack.popAll(-1) }
		
		assertEquals(5, stack.size)
	}
	
	@Test
	@DisplayName("Test popAll for large argument")
	fun testPopAllForLarge() {
		assertEquals(5, stack.size)
		
		assertFailsWith<IllegalArgumentException> { stack.popAll(6) }
		
		assertEquals(5, stack.size)
	}
}