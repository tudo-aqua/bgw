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

package util.bidirectionalmap

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ContainsTest : BidirectionalMapTestBase() {
	@Test
	@DisplayName("Test contains of existing relation")
	fun testContainsExistingRelation() {
		assertTrue(map.contains(0, 1))
	}
	
	@Test
	@DisplayName("Test contains of relation with existing key and non-existing value")
	fun testContainsRelationExistingKey() {
		assertFalse(map.contains(0, 5))
	}
	
	@Test
	@DisplayName("Test contains of relation with non-existing key and existing value")
	fun testContainsRelationExistingValue() {
		assertFalse(map.contains(5, 1))
	}
	
	@Test
	@DisplayName("Test contains forward on existing key")
	fun testContainsForwardExisting() {
		assertTrue(map.containsForward(0))
	}
	
	@Test
	@DisplayName("Test contains forward on non-existing key")
	fun testContainsForwardNonExisting() {
		assertFalse(map.containsForward(5))
	}
	
	@Test
	@DisplayName("Test contains backward on existing value")
	fun testContainsBackwardExisting() {
		assertTrue(map.containsBackward(1))
	}
	
	@Test
	@DisplayName("Test contains backward on non-existing value")
	fun testContainsBackwardNonExisting() {
		assertFalse(map.containsBackward(5))
	}
}