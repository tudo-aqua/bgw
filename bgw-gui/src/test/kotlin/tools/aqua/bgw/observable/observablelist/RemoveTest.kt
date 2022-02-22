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

package tools.aqua.bgw.observable.observablelist

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.assertThrows

class RemoveTest : ObservableListTestBase() {
	@Test
	@DisplayName("Test remove")
	fun testRemove() {
		//13,25,17,13,-4
		assertTrue(list.remove(13))
		
		//25,17,13,-4
		assertEquals(4, list.size)
		
		for (i in 1 until list.size)
			assertEquals(unordered[i], list[i - 1])
		
		checkNotified()
		
		assertFalse(list.remove(42))
	}
	
	@Test
	@DisplayName("Test remove at index")
	fun testRemoveAt() {
		//13,25,17,13,-4
		assertEquals(13, list.removeAt(0))
		
		//25,17,13,-4
		assertEquals(4, list.size)
		
		for (i in 1 until list.size)
			assertEquals(unordered[i], list[i - 1])
		
		checkNotified()
	}
	
	@Test
	@DisplayName("Test remove at index out of bounds")
	fun testRemoveAtOutOfBounds() {
		assertThrows<IndexOutOfBoundsException> {
			list.removeAt(-1)
		}
		assertThrows<IndexOutOfBoundsException> {
			list.removeAt(5)
		}
	}
	
	@Test
	@DisplayName("Test remove first or null")
	fun testRemoveFirstOrNull() {
		//13,25,17,13,-4
		assertEquals(13, list.removeFirstOrNull())
		assertEquals(25, list.removeFirstOrNull())
		assertEquals(17, list.removeFirstOrNull())
		assertEquals(13, list.removeFirstOrNull())
		assertEquals(-4, list.removeFirstOrNull())
		assertEquals(null, list.removeFirstOrNull())
		
		checkNotified(5)
	}
	
	@Test
	@DisplayName("Test remove first")
	fun testRemoveFirst() {
		//13,25,17,13,-4
		assertEquals(13, list.removeFirst())
		assertEquals(25, list.removeFirst())
		assertEquals(17, list.removeFirst())
		assertEquals(13, list.removeFirst())
		assertEquals(-4, list.removeFirst())
		
		checkNotified(5)

		assertThrows<NoSuchElementException> {
			list.removeFirst()
		}
	}
	
	@Test
	@DisplayName("Test remove last or null")
	fun testRemoveLastOrNull() {
		//13,25,17,13,-4
		assertEquals(-4, list.removeLastOrNull())
		assertEquals(13, list.removeLastOrNull())
		assertEquals(17, list.removeLastOrNull())
		assertEquals(25, list.removeLastOrNull())
		assertEquals(13, list.removeLastOrNull())
		assertEquals(null, list.removeLastOrNull())
		
		checkNotified(5)
	}
	
	@Test
	@DisplayName("Test remove last")
	fun testRemoveLast() {
		//13,25,17,13,-4
		assertEquals(-4, list.removeLast())
		assertEquals(13, list.removeLast())
		assertEquals(17, list.removeLast())
		assertEquals(25, list.removeLast())
		assertEquals(13, list.removeLast())
		
		checkNotified(5)

		assertThrows<NoSuchElementException> {
			list.removeLast()
		}
	}
	
	@Test
	@DisplayName("Test clear")
	fun testClear() {
		list.clear()
		assertEquals(0, list.size)
		
		checkNotified(1)
		
		list.clear()
		
		checkNotified(1)
	}
	
	@Test
	@DisplayName("Test remove all")
	fun testRemoveAll() {
		//13,25,17,13,-4
		val subList = listOf(13, -4)
		val resultList = listOf(25, 17)
		
		assertTrue(list.removeAll(subList))
		
		assertEquals(2, list.size)
		for (i in list.indices)
			assertEquals(resultList[i], list[i])
		
		checkNotified()
		
		assertFalse(list.removeAll(listOf(-1, -2)))
		
		//Only notified once
		checkNotified(1)
	}
	
	@Test
	@DisplayName("Test retain all")
	fun testRetainAll() {
		//13,25,17,13,-4
		val subList = listOf(13, -4)
		val resultList = listOf(13, 13, -4)
		
		assertTrue(list.retainAll(subList))
		
		assertEquals(3, list.size)
		for (i in list.indices)
			assertEquals(resultList[i], list[i])
		
		checkNotified()
		
		assertFalse(list.retainAll(subList))
		
		//Only notified once
		checkNotified(1)
	}
	
	@Test
	@DisplayName("Test remove if")
	fun testRemoveIf() {
		//13,25,17,13,-4
		val resultList = listOf(13, 13, -4)
		
		assertTrue(list.removeIf { it > 15 })
		
		assertEquals(3, list.size)
		for (i in list.indices)
			assertEquals(resultList[i], list[i])
		
		checkNotified()
		
		assertFalse(list.removeIf { it > 99 })
		
		//Only notified once
		checkNotified(1)
	}
	
	@Test
	@DisplayName("Test replace all")
	fun testReplaceAll() {
		
		list.replaceAll { it + 1 }
		
		assertEquals(5, list.size)
		for (i in list.indices)
			assertEquals(unordered[i] + 1, list[i])
		
		checkNotified()
	}
}