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
import org.junit.jupiter.api.Assertions.assertEquals

class GetSetTest : PropertyTestBase() {
	
	@Test
	@DisplayName("Get and set invoking all listeners")
	fun testGetSet() {
		property.value = newValue
		assertEquals(newValue, property.value)
		
		assertEquals(1, listener1.invokedCount)
		assertEquals(initialValue, listener1.oldValue)
		assertEquals(newValue, listener1.newValue)
		
		assertEquals(1, listener2.invokedCount)
		assertEquals(initialValue, listener2.oldValue)
		assertEquals(newValue, listener2.newValue)
		
		assertEquals(1, internalListener.invokedCount)
		assertEquals(initialValue, internalListener.oldValue)
		assertEquals(newValue, internalListener.newValue)
		
		assertEquals(1, guiListener.invokedCount)
		assertEquals(initialValue, guiListener.oldValue)
		assertEquals(newValue, guiListener.newValue)
	}
	
	@Test
	@DisplayName("Get and set same value invoking no listeners")
	fun testGetSetSameValue() {
		property.value = initialValue
		assertEquals(initialValue, property.value)
		
		assertEquals(0, listener1.invokedCount)
		assertEquals(null, listener1.oldValue)
		assertEquals(null, listener1.newValue)
		
		assertEquals(0, listener2.invokedCount)
		assertEquals(null, listener2.oldValue)
		assertEquals(null, listener2.newValue)
		
		assertEquals(0, internalListener.invokedCount)
		assertEquals(null, internalListener.oldValue)
		assertEquals(null, internalListener.newValue)
		
		assertEquals(0, guiListener.invokedCount)
		assertEquals(null, guiListener.oldValue)
		assertEquals(null, guiListener.newValue)
	}
}