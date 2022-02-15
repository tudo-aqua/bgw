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

package tools.aqua.bgw.util.bidirectionalmap

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LookupTest : BidirectionalMapTestBase() {
	@Test
	@DisplayName("Test forward lookup on existing element")
	fun testForwardExisting() {
		assertEquals(1, map.forward(0))
		assertEquals(1, map.forwardOrNull(0))
	}
	
	@Test
	@DisplayName("Test backward lookup on existing element")
	fun testBackwardExisting() {
		assertEquals(0, map.backward(1))
		assertEquals(0, map.backwardOrNull(1))
	}
	
	@Test
	@DisplayName("Test forward lookup on non-existing element")
	fun testForwardNonExisting() {
		assertFailsWith<NoSuchElementException> {
			map.forward(5)
		}
		assertEquals(null, map.forwardOrNull(5))
	}
	
	@Test
	@DisplayName("Test backward lookup on non-existing element")
	fun testBackwardNonExisting() {
		assertFailsWith<NoSuchElementException> {
			map.backward(5)
		}
		assertEquals(null, map.backwardOrNull(5))
	}
}