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

package tools.aqua.bgw.container.area

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

/** Test remove function in Area. */
class RemoveElementTest : AreaTestBase() {

  /** Removes an element. */
  @Test
  @DisplayName("Removes an element")
  fun removeElement() {
    tokenViewArea.add(redTokenView)
    tokenViewArea.add(blueTokenView)
    tokenViewArea.add(greenTokenView)
    tokenViewArea.remove(redTokenView)
    assertEquals(listOf(blueTokenView, greenTokenView), tokenViewArea.components)
    assertNull(redTokenView.parent)
    tokenViewArea.remove(redTokenView)
    assertEquals(listOf(blueTokenView, greenTokenView), tokenViewArea.components)
  }

  /** Remove all Elements. */
  @Test
  @DisplayName("Remove all Elements")
  fun removeAllElements() {
    tokenViewArea.add(redTokenView)
    tokenViewArea.add(blueTokenView)
    val result = tokenViewArea.clear()
    assertEquals(listOf(redTokenView, blueTokenView), result)
    assertNull(redTokenView.parent)
    assertNull(blueTokenView.parent)
  }
}
