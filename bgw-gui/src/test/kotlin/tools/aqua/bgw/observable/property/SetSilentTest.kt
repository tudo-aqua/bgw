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
import kotlin.test.assertEquals

class SetSilentTest : PropertyTestBase() {
	
	@Test
	@DisplayName("Set silent only invoking GUI listener")
	fun testSetSilent() {
		property.setSilent(newValue)
		assertEquals(newValue, property.value)
		
		assertEquals(0, listener1.invokedCount)
		assertEquals(null, listener1.oldValue)
		assertEquals(null, listener1.newValue)
		
		assertEquals(0, listener2.invokedCount)
		assertEquals(null, listener2.oldValue)
		assertEquals(null, listener2.newValue)
		
		assertEquals(0, internalListener.invokedCount)
		assertEquals(null, internalListener.oldValue)
		assertEquals(null, internalListener.newValue)
		
		assertEquals(1, guiListener.invokedCount)
		assertEquals(initialValue, guiListener.oldValue)
		assertEquals(newValue, guiListener.newValue)
	}
	
	@Test
	@DisplayName("Set silent same value invoking no listeners")
	fun testGetSetSameValue() {
		property.setSilent(initialValue)
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