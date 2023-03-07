/*
 * Copyright 2021-2023 The BoardGameWork Authors
 * SPDX-License-Identifier: Apache-2.0
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tools.aqua.bgw.observable.property

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

/** Test notifyUnchanged function in Property. */
class NotifyUnchangedTest : PropertyTestBase() {

  /** Notify Unchanged invoking all listener. */
  @Test
  @DisplayName("Notify Unchanged invoking all listener")
  fun testNotifyUnchanged() {
    property.notifyUnchanged()
    assertEquals(initialValue, property.value)

    assertEquals(1, listener1.invokedCount)
    assertEquals(initialValue, listener1.oldValue)
    assertEquals(initialValue, listener1.newValue)

    assertEquals(1, listener2.invokedCount)
    assertEquals(initialValue, listener2.oldValue)
    assertEquals(initialValue, listener2.newValue)

    assertEquals(1, internalListener.invokedCount)
    assertEquals(initialValue, internalListener.oldValue)
    assertEquals(initialValue, internalListener.newValue)

    assertEquals(1, guiListener.invokedCount)
    assertEquals(initialValue, guiListener.oldValue)
    assertEquals(initialValue, guiListener.newValue)
  }
}
