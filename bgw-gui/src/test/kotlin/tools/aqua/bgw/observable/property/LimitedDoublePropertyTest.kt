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

package tools.aqua.bgw.observable.property

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import tools.aqua.bgw.observable.properties.LimitedDoubleProperty
import kotlin.test.assertFailsWith

class LimitedDoublePropertyTest : PropertyTestBase() {
	
	@Test
	@DisplayName("Test upperBound < lowerBound")
	fun testBoundsWrongOrderEqual() {
		assertFailsWith<IllegalArgumentException> {
			LimitedDoubleProperty(10, 5, 0)
		}
	}
	
	@Test
	@DisplayName("Test upperBound = lowerBound")
	fun testBoundsEqual() {
		val newProperty = LimitedDoubleProperty(10, 10, 10)
		newProperty.value = 10.0
	}
	
	@Test
	@DisplayName("Test initial value out of bounds")
	fun testInitialValueOutOfBounds() {
		assertFailsWith<IllegalArgumentException> {
			LimitedDoubleProperty(0, 10, 15)
		}
	}
	
	@Test
	@DisplayName("Notify Unchanged invoking all listener")
	fun testSetOutOfBounds() {
		assertFailsWith<IllegalArgumentException> {
			property.value = -7.0
		}
	}
}