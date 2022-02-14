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