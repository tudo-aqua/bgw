/*
 * Copyright 2021-2022 The BoardGameWork Authors
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

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import tools.aqua.bgw.components.container.Area
import tools.aqua.bgw.components.gamecomponentviews.TokenView

/** Test add function in Area. */
class AddElementTest : AreaTestBase() {

  /** Add an element. */
  @Test
  @DisplayName("Add an element")
  fun addElement() {
    assertTrue(tokenViewArea.components.isEmpty())
    tokenViewArea.add(redTokenView)
    assertThat(tokenViewArea.components).contains(redTokenView)
    assertEquals(tokenViewArea, redTokenView.parent)
  }

  /** Add an element that is already contained in this. */
  @Test
  @DisplayName("Add an element that is already contained in this")
  fun addElementAlreadyContainedInThis() {
    tokenViewArea.add(redTokenView)
    assertThrows<IllegalArgumentException> { tokenViewArea.add(redTokenView) }
  }

  /** Add an element that is already contained in another container. */
  @Test
  @DisplayName("Add an element that is already contained in another container")
  fun addElementAlreadyContainedInOther() {
    Area<TokenView>().add(blueTokenView)
    assertThrows<IllegalArgumentException> { tokenViewArea.add(blueTokenView) }
  }

  /** Add element with custom index. */
  @Test
  @DisplayName("Add element with custom index")
  fun addElementWithIndex() {
    // add with index
    tokenViewArea.add(redTokenView, 0)

    // index out of bounds
    assertThrows<IllegalArgumentException> { tokenViewArea.add(blueTokenView, 2) }
    assertFalse { tokenViewArea.components.contains(blueTokenView) }
    assertThrows<IllegalArgumentException> { tokenViewArea.add(blueTokenView, -1) }
    assertFalse { tokenViewArea.components.contains(blueTokenView) }

    // add in between two elements
    tokenViewArea.add(blueTokenView, 1)
    assertEquals(listOf(redTokenView, blueTokenView), tokenViewArea.components)
    tokenViewArea.add(greenTokenView, 1)
    assertEquals(listOf(redTokenView, greenTokenView, blueTokenView), tokenViewArea.components)
  }
}
