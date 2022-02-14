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

package tools.aqua.bgw.util.stack

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