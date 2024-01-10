/*
 * Copyright 2022-2024 The BoardGameWork Authors
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

package tools.aqua.bgw.event

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.container.Area
import tools.aqua.bgw.components.gamecomponentviews.TokenView
import tools.aqua.bgw.exception.IllegalInheritanceException
import tools.aqua.bgw.visual.ColorVisual

/**
 * Since all Event classes are purely data holding structures, this tests simply instantiates these.
 */
class DummyEventTest {
  /** Tests events. */
  @Test
  @DisplayName("Test event")
  fun testEvent() {
    AnimationFinishedEvent()

    DragEvent(draggedComponent = TokenView(visual = ColorVisual.TRANSPARENT))

    DropEvent(draggedComponent = TokenView(visual = ColorVisual.TRANSPARENT))
    DropEvent(
        draggedComponent = TokenView(visual = ColorVisual.TRANSPARENT), dragTargets = emptyList())
    DropEvent(
        draggedComponent = TokenView(visual = ColorVisual.TRANSPARENT),
        dragTargets = listOf(Area<TokenView>()))

    KeyEvent(
        keyCode = KeyCode.A,
        character = "A",
        isControlDown = false,
        isShiftDown = false,
        isAltDown = false)
    KeyEvent(character = "A", isControlDown = false, isShiftDown = false, isAltDown = false)
    KeyEvent(keyCode = KeyCode.A, isControlDown = false, isShiftDown = false, isAltDown = false)
    KeyEvent(isControlDown = false, isShiftDown = false, isAltDown = false)

    MouseEvent(MouseButtonType.OTHER, posX = 0, posY = 0)

    ScrollEvent(ScrollDirection.UP, isControlDown = false, isShiftDown = false, isAltDown = false)

    IllegalInheritanceException(Any(), ComponentView::class.java)
  }
}
