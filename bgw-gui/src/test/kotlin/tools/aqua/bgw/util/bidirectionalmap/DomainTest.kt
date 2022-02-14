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
import tools.aqua.bgw.util.BidirectionalMap
import kotlin.test.assertEquals

class DomainTest : BidirectionalMapTestBase() {
	@Test
	@DisplayName("Test domain")
	fun testDomain() {
		assertEquals(setOf(0, 2), map.getDomain())
	}
	
	@Test
	@DisplayName("Test co-domain")
	fun testCoDomain() {
		assertEquals(setOf(1, 3), map.getCoDomain())
	}
	
	@Test
	@DisplayName("Test empty domain")
	fun testEmptyDomain() {
		assertEquals(setOf(), BidirectionalMap<Int, Int>().getDomain())
	}
	
	@Test
	@DisplayName("Test empty co-domain")
	fun testEmptyCoDomain() {
		assertEquals(setOf(), BidirectionalMap<Int, Int>().getCoDomain())
	}
}