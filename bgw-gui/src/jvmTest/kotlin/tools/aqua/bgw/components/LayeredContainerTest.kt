/*
 * Copyright 2021-2026 The BoardGameWork Authors
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

package tools.aqua.bgw.components

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import tools.aqua.bgw.components.container.Area
import tools.aqua.bgw.components.gamecomponentviews.TokenView
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.visual.Visual

/** Regression tests for reordering layered component collections. */
class LayeredContainerTest {

  @Test
  fun `pane setZIndex reorders the actual component list`() {
    val pane = Pane<TokenView>(width = 100, height = 100)
    val back = token(10)
    val middle = token(20)
    val front = token(30)
    pane.addAll(back, middle, front)

    pane.setZIndex(front, 5)

    assertEquals(listOf(front, back, middle), pane.components)
  }

  @Test
  fun `game component container front and back operations are safe and reorder children`() {
    val area = Area<TokenView>()
    val back = token(10)
    val front = token(20)

    area.toFront(back)
    area.toBack(back)
    area.addAll(back, front)
    area.toFront(back)

    assertEquals(listOf(front, back), area.components)
    assertEquals(front.zIndex, back.zIndex)
  }

  @Test
  fun `scene front operation updates the child rather than the root node`() {
    val scene = BoardGameScene()
    val back = token(10)
    val front = token(20)
    scene.addComponents(back, front)
    val rootZIndex = scene.rootNode.zIndex

    scene.rootNode.toFront(back)

    assertSame(back, scene.components.last())
    assertEquals(front.zIndex, back.zIndex)
    assertEquals(rootZIndex, scene.rootNode.zIndex)
  }

  private fun token(zIndex: Int) = TokenView(visual = Visual.EMPTY).apply { this.zIndex = zIndex }
}
