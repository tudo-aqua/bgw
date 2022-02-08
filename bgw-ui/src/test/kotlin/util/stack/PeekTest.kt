/*
 *    Copyright 2021 The BoardGameWork Authors
 *    SPDX-License-Identifier: Apache-2.0
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package util.stack

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PeekTest: StackTestBase() {
	
	@Test
	@DisplayName("Test peek on empty Stack")
	fun testPeekOnEmptyStack() {
		assertEquals(0, emptyStack.size)
		assertFailsWith<NoSuchElementException> { emptyStack.peek() }
	}
	
	@Test
	@DisplayName("Test peekOrNull on empty Stack")
	fun testPeekOrNullOnEmptyStack() {
		assertEquals(0, emptyStack.size)
		assertEquals(null, emptyStack.peekOrNull())
	}
	
	@Test
	@DisplayName("Test peek")
	fun testPeek() {
		assertEquals(5, stack.size)
		assertEquals(order[0], stack.peek())
		assertEquals(5, stack.size)
	}
	
	@Test
	@DisplayName("Test peekOrNull")
	fun testPeekOrNull() {
		assertEquals(5, stack.size)
		assertEquals(order[0], stack.peekOrNull())
		assertEquals(5, stack.size)
	}
	
	@Test
	@DisplayName("Test peekAll for all elements")
	fun testPeekAllForAll() {
		assertEquals(5, stack.size)
		
		assertEquals(order, stack.peekAll(order.size))
		
		assertEquals(5, stack.size)
	}
	
	@Test
	@DisplayName("Test peekAll for some elements")
	fun testPeekAllForSome() {
		assertEquals(5, stack.size)
		
		assertEquals(order.subList(0,3), stack.peekAll(3))
		
		assertEquals(5, stack.size)
	}
	
	@Test
	@DisplayName("Test peekAll for one element")
	fun testPeekAllForOne() {
		assertEquals(5, stack.size)
		
		assertEquals(listOf(order[0]), stack.peekAll(1))
		
		assertEquals(5, stack.size)
	}
	
	@Test
	@DisplayName("Test peekAll for zero argument")
	fun testPeekAllForZero() {
		assertEquals(5, stack.size)
		
		assertEquals(listOf(), stack.peekAll(0))
		
		assertEquals(5, stack.size)
	}
	
	@Test
	@DisplayName("Test peekAll for negative argument")
	fun testPeekAllForNegative() {
		assertEquals(5, stack.size)
		
		assertFailsWith<IllegalArgumentException> { stack.peekAll(-1) }
		
		assertEquals(5, stack.size)
	}
	
	@Test
	@DisplayName("Test peekAll for large argument")
	fun testPeekAllForLarge() {
		assertEquals(5, stack.size)
		
		assertFailsWith<IllegalArgumentException> { stack.peekAll(6) }
		
		assertEquals(5, stack.size)
	}
}