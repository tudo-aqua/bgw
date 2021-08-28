package util.stack

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class PushTest: StackTestBase() {
	
	@Test
	@DisplayName("Test push")
	fun testPush() {
		assertEquals(5, stack.size)
		
		stack.push(42)
		
		assertEquals(6, stack.size)
		assertEquals(42, stack.peek())
	}
	
	@Test
	@DisplayName("Test pushAll without elements in varargs")
	fun testPushAllEmptyVarargs() {
		assertEquals(5, stack.size)
		
		stack.pushAll()
		
		assertEquals(5, stack.size)
	}
	
	@Test
	@DisplayName("Test pushAll with empty list")
	fun testPushAllEmptyList() {
		assertEquals(5, stack.size)
		
		stack.pushAll(listOf())
		
		assertEquals(5, stack.size)
	}
	
	@Test
	@DisplayName("Test pushAll with one element in varargs")
	fun testPushAllOneElementVarargs() {
		assertEquals(5, stack.size)
		
		stack.pushAll(42)
		
		assertEquals(6, stack.size)
		assertEquals(42, stack.peek())
	}
	
	@Test
	@DisplayName("Test pushAll with one element in list")
	fun testPushAllOneElementList() {
		assertEquals(5, stack.size)
		
		stack.pushAll(listOf(42))
		
		assertEquals(6, stack.size)
		assertEquals(42, stack.peek())
	}
	
	@Test
	@DisplayName("Test pushAll with multiple elements in varargs")
	fun testPushAllMultipleElementsVarargs() {
		assertEquals(5, stack.size)
		
		stack.pushAll(42, 96, 666)
		
		assertEquals(8, stack.size)
		assertEquals(666, stack.pop())
		assertEquals(96, stack.pop())
		assertEquals(42, stack.pop())
	}
	
	@Test
	@DisplayName("Test pushAll with multiple elements in list")
	fun testPushAllMultipleElementsList() {
		assertEquals(5, stack.size)
		
		stack.pushAll(listOf(42, 96, 666))
		
		assertEquals(8, stack.size)
		assertEquals(666, stack.pop())
		assertEquals(96, stack.pop())
		assertEquals(42, stack.pop())
	}
}