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
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AddTest : BidirectionalMapTestBase() {
	@Test
	@DisplayName("Test add new pair")
	fun testAddNew() {
		assertTrue(map.add(4, 5))
		
		assertEquals(3, map.size)
		assertTrue(map.contains(4, 5))
	}
	
	@Test
	@DisplayName("Test adding a key already existing")
	fun testAddKeyAlreadyExisting() {
		assertFalse(map.add(0, 5))
		
		assertEquals(2, map.size)
		assertFalse(map.contains(0, 5))
	}
	
	@Test
	@DisplayName("Test adding a value already existing")
	fun testAddValueAlreadyExisting() {
		assertFalse(map.add(5, 1))
		
		assertEquals(2, map.size)
		assertFalse(map.contains(5, 1))
	}
	
	@Test
	@DisplayName("Test adding a key already existing as value")
	fun testAddKeyAlreadyExistingAsValue() {
		assertTrue(map.add(3, 5))
		
		assertEquals(3, map.size)
		assertTrue(map.contains(3, 5))
	}
	
	@Test
	@DisplayName("Test adding a value already existing as key")
	fun testAddValueAlreadyExistingAsKey() {
		assertTrue(map.add(5, 0))
		
		assertEquals(3, map.size)
		assertTrue(map.contains(5, 0))
	}
	
	@Test
	@DisplayName("Test adding new values by addAll")
	fun testAddAllNew() {
		assertTrue(
			map.addAll(
				Pair(5, 6),
				Pair(7, 8)
			)
		)
		
		//assertEquals(4, map.size)
		assertTrue(map.contains(5, 6))
		assertTrue(map.contains(7, 8))
	}
	
	@Test
	@DisplayName("Test adding new and old values by addAll")
	fun testAddAllMixedNewAndOld() {
		assertTrue(
			map.addAll(
				Pair(5, 6),
				Pair(0, 1)
			)
		)
		
		assertEquals(3, map.size)
		assertTrue(map.contains(5, 6))
	}
	
	@Test
	@DisplayName("Test adding new, old and invalid values by addAll")
	fun testAddAllMixedWithInvalid() {
		assertFalse(
			map.addAll(
				Pair(5, 6),
				Pair(0, 5),
				Pair(0, 1)
			)
		)
		
		assertEquals(2, map.size)
		assertFalse(map.contains(5, 6))
		assertFalse(map.contains(0, 5))
	}
	
	@Test
	@DisplayName("Test adding old values by addAll")
	fun testAddAllOld() {
		assertTrue(
			map.addAll(
				Pair(0, 1),
				Pair(2, 3)
			)
		)
		
		assertEquals(2, map.size)
	}
}