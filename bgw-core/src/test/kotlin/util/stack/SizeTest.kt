package util.stack

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SizeTest: StackTestBase() {
	@Test
	@DisplayName("Test size")
	fun testSize() {
		assertEquals(5, stack.size)
		assertEquals(0, emptyStack.size)
	}
	
	@Test
	@DisplayName("Test clear")
	fun testClear() {
		assertEquals(5, stack.size)
		assertEquals(order, stack.clear())
		assertEquals(0, emptyStack.size)
	}
	
	@Test
	@DisplayName("Test clear on empty stack")
	fun testClearEmptyStack() {
		assertEquals(0, emptyStack.size)
		assertEquals(listOf(), emptyStack.clear())
		assertEquals(0, emptyStack.size)
	}
	
	@Test
	@DisplayName("Test isEmpty and isNotEmpty")
	fun testIsEmpty() {
		assertEquals(5, stack.size)
		assertFalse { stack.isEmpty() }
		assertTrue { stack.isNotEmpty() }
		
		assertEquals(0, emptyStack.size)
		assertTrue { emptyStack.isEmpty() }
		assertFalse { emptyStack.isNotEmpty() }
	}
	
	@Test
	@DisplayName("Test indexOf on existing element")
	fun testIndexOfExisting() {
		assertEquals(2, stack.indexOf(order[2]))
	}
	
	@Test
	@DisplayName("Test indexOf on duplicate element")
	fun testIndexOfDuplicate() {
		stack.push(order[2])
		assertEquals(2, stack.indexOf(order[2]))
	}
	
	@Test
	@DisplayName("Test indexOf on non-existing element")
	fun testIndexOfNonExisting() {
		assertEquals(-1, stack.indexOf(42))
	}
}