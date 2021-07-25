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

package observable.property

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SetAndInvokeTest : PropertyTestBase() {
	@Test
	@DisplayName("Test add listener silently")
	fun testAddListenerSilently() {
		val testListener = TestListener()
		property.addListener(testListener)
		
		//Property unchanged
		assertEquals(initialValue, property.value)
		
		//Listener not notified
		assertEquals(0, listener1.invokedCount)
		assertEquals(null, listener1.oldValue)
		assertEquals(null, listener1.newValue)
		//Listener not notified
		assertEquals(0, listener2.invokedCount)
		assertEquals(null, listener2.oldValue)
		assertEquals(null, listener2.newValue)
		//Listener not notified
		assertEquals(0, internalListener.invokedCount)
		assertEquals(null, guiListener.oldValue)
		assertEquals(null, guiListener.newValue)
		//Listener not notified
		assertEquals(0, guiListener.invokedCount)
		assertEquals(null, internalListener.oldValue)
		assertEquals(null, internalListener.newValue)
		//New listener not notified
		assertEquals(0, testListener.invokedCount)
		assertEquals(null, testListener.oldValue)
		assertEquals(null, testListener.newValue)
		
		//Notify
		property.notifyUnchanged()
		
		//Listener notified once
		assertEquals(1, listener1.invokedCount)
		assertEquals(initialValue, listener1.oldValue)
		assertEquals(initialValue, listener1.newValue)
		//Listener notified once
		assertEquals(1, listener2.invokedCount)
		assertEquals(initialValue, listener2.oldValue)
		assertEquals(initialValue, listener2.newValue)
		//Listener notified once
		assertEquals(1, internalListener.invokedCount)
		assertEquals(initialValue, internalListener.oldValue)
		assertEquals(initialValue, internalListener.newValue)
		//Listener notified once
		assertEquals(1, guiListener.invokedCount)
		assertEquals(initialValue, guiListener.oldValue)
		assertEquals(initialValue, guiListener.newValue)
		//New listener notified once
		assertEquals(1, testListener.invokedCount)
		assertEquals(initialValue, testListener.oldValue)
		assertEquals(initialValue, testListener.newValue)
	}
	
	@Test
	@DisplayName("Test and invoke listener")
	fun testAndListenerAndInvoke() {
		val testListener = TestListener()
		property.addListenerAndInvoke(88.0, testListener)
		
		//Property unchanged
		assertEquals(initialValue, property.value)
		
		//Listener not notified
		assertEquals(0, listener1.invokedCount)
		assertEquals(null, listener1.oldValue)
		assertEquals(null, listener1.newValue)
		//Listener not notified
		assertEquals(0, listener2.invokedCount)
		assertEquals(null, listener2.oldValue)
		assertEquals(null, listener2.newValue)
		//Listener not notified
		assertEquals(0, internalListener.invokedCount)
		assertEquals(null, guiListener.oldValue)
		assertEquals(null, guiListener.newValue)
		//Listener not notified
		assertEquals(0, guiListener.invokedCount)
		assertEquals(null, internalListener.oldValue)
		assertEquals(null, internalListener.newValue)
		
		//New listener notified
		assertEquals(1, testListener.invokedCount)
		assertEquals(88.0, testListener.oldValue)
		assertEquals(88.0, testListener.newValue)
		
		//Notify
		property.notifyUnchanged()
		
		//Listener notified once
		assertEquals(1, listener1.invokedCount)
		assertEquals(initialValue, listener1.oldValue)
		assertEquals(initialValue, listener1.newValue)
		//Listener notified once
		assertEquals(1, listener2.invokedCount)
		assertEquals(initialValue, listener2.oldValue)
		assertEquals(initialValue, listener2.newValue)
		//Listener notified once
		assertEquals(1, internalListener.invokedCount)
		assertEquals(initialValue, internalListener.oldValue)
		assertEquals(initialValue, internalListener.newValue)
		//Listener notified once
		assertEquals(1, guiListener.invokedCount)
		assertEquals(initialValue, guiListener.oldValue)
		assertEquals(initialValue, guiListener.newValue)
		//New listener notified twice
		assertEquals(2, testListener.invokedCount)
		assertEquals(initialValue, testListener.oldValue)
		assertEquals(initialValue, testListener.newValue)
	}
	
	@Test
	@DisplayName("Test set and invoke internalListener")
	fun testSetAndInvokeInternalListener() {
		val testListener = TestListener()
		property.setGUIListenerAndInvoke(88.0, testListener)
		
		//Property unchanged
		assertEquals(initialValue, property.value)
		
		//Listener not notified
		assertEquals(0, listener1.invokedCount)
		assertEquals(null, listener1.oldValue)
		assertEquals(null, listener1.newValue)
		//Listener not notified
		assertEquals(0, listener2.invokedCount)
		assertEquals(null, listener2.oldValue)
		assertEquals(null, listener2.newValue)
		//Listener not notified
		assertEquals(0, internalListener.invokedCount)
		assertEquals(null, guiListener.oldValue)
		assertEquals(null, guiListener.newValue)
		//Listener removed and not notified
		assertEquals(0, guiListener.invokedCount)
		assertEquals(null, internalListener.oldValue)
		assertEquals(null, internalListener.newValue)
		
		//New listener notified
		assertEquals(1, testListener.invokedCount)
		assertEquals(88.0, testListener.oldValue)
		assertEquals(88.0, testListener.newValue)
		
		//Notify
		property.notifyUnchanged()
		
		//Listener notified once
		assertEquals(1, listener1.invokedCount)
		assertEquals(initialValue, listener1.oldValue)
		assertEquals(initialValue, listener1.newValue)
		//Listener notified once
		assertEquals(1, listener2.invokedCount)
		assertEquals(initialValue, listener2.oldValue)
		assertEquals(initialValue, listener2.newValue)
		//Listener notified once
		assertEquals(1, internalListener.invokedCount)
		assertEquals(initialValue, internalListener.oldValue)
		assertEquals(initialValue, internalListener.newValue)
		//Listener removed and not notified
		assertEquals(0, guiListener.invokedCount)
		assertEquals(null, guiListener.oldValue)
		assertEquals(null, guiListener.newValue)
		//New listener notified twice
		assertEquals(2, testListener.invokedCount)
		assertEquals(initialValue, testListener.oldValue)
		assertEquals(initialValue, testListener.newValue)
	}
	
	@Test
	@DisplayName("Test set and invoke GUIListener")
	fun testSetAndInvokeGUIListener() {
		val testListener = TestListener()
		property.setInternalListenerAndInvoke(88.0, testListener)
		
		//Property unchanged
		assertEquals(initialValue, property.value)
		
		//Listener not notified
		assertEquals(0, listener1.invokedCount)
		assertEquals(null, listener1.oldValue)
		assertEquals(null, listener1.newValue)
		//Listener not notified
		assertEquals(0, listener2.invokedCount)
		assertEquals(null, listener2.oldValue)
		assertEquals(null, listener2.newValue)
		//Listener removed and not notified
		assertEquals(0, internalListener.invokedCount)
		assertEquals(null, internalListener.oldValue)
		assertEquals(null, internalListener.newValue)
		//Listener not notified
		assertEquals(0, guiListener.invokedCount)
		assertEquals(null, guiListener.oldValue)
		assertEquals(null, guiListener.newValue)
		
		//New listener notified
		assertEquals(1, testListener.invokedCount)
		assertEquals(88.0, testListener.oldValue)
		assertEquals(88.0, testListener.newValue)
		
		//Notify
		property.notifyUnchanged()
		
		//Listener notified once
		assertEquals(1, listener1.invokedCount)
		assertEquals(initialValue, listener1.oldValue)
		assertEquals(initialValue, listener1.newValue)
		//Listener notified once
		assertEquals(1, listener2.invokedCount)
		assertEquals(initialValue, listener2.oldValue)
		assertEquals(initialValue, listener2.newValue)
		//Listener removed and not notified
		assertEquals(0, internalListener.invokedCount)
		assertEquals(null, internalListener.oldValue)
		assertEquals(null, internalListener.newValue)
		//Listener notified once
		assertEquals(1, guiListener.invokedCount)
		assertEquals(initialValue, guiListener.oldValue)
		assertEquals(initialValue, guiListener.newValue)
		//New listener notified twice
		assertEquals(2, testListener.invokedCount)
		assertEquals(initialValue, testListener.oldValue)
		assertEquals(initialValue, testListener.newValue)
	}
}