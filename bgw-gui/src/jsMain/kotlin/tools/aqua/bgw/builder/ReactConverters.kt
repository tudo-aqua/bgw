/*
 * Copyright 2025 The BoardGameWork Authors
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

package tools.aqua.bgw.builder

import ID
import data.event.*
import kotlinx.browser.document
import react.dom.events.DragEvent as ReactDragEvent
import react.dom.events.KeyboardEvent as ReactKeyEvent
import react.dom.events.MouseEvent as ReactMouseEvent
import react.dom.events.WheelEvent
import tools.aqua.bgw.DragEndEvent
import tools.aqua.bgw.DragMultiEvent
import tools.aqua.bgw.DragStartEvent
import tools.aqua.bgw.event.KeyCode
import tools.aqua.bgw.event.MouseButtonType
import tools.aqua.bgw.event.WheelDirection

internal object ReactConverters {
  fun getSceneOffset(): Pair<Pair<Double, Double>, Pair<Double, Double>> {
    val menuScene = document.getElementById("menuScene")
    val gameScene = document.getElementById("boardGameScene")

    if (menuScene != null && gameScene != null) {
      val menuSceneOffset = menuScene.getBoundingClientRect()
      if (menuSceneOffset.width == 0.0 && menuSceneOffset.height == 0.0) {
        // If the menu scene is not visible, we return the game scene offset.
        val gameSceneOffset = gameScene.getBoundingClientRect()
        return Pair(
            Pair(gameSceneOffset.left, gameSceneOffset.top),
            Pair(gameSceneOffset.width, gameSceneOffset.height))
      }
      return Pair(
          Pair(menuSceneOffset.left, menuSceneOffset.top),
          Pair(menuSceneOffset.width, menuSceneOffset.height))
    } else if (gameScene != null) {
      val gameSceneOffset = gameScene.getBoundingClientRect()
      return Pair(
          Pair(gameSceneOffset.left, gameSceneOffset.top),
          Pair(gameSceneOffset.width, gameSceneOffset.height))
    } else {
      return Pair(Pair(0.0, 0.0), Pair(0.0, 0.0))
    }
  }

  fun mousePositionToScenePosition(clientX: Double, clientY: Double): Pair<Double, Double> {
    val (offset, size) = getSceneOffset()
    val absoluteX = (clientX - offset.first) / size.first
    val absoluteY = (clientY - offset.second) / size.second

    return Pair(absoluteX, absoluteY)
  }
  fun ReactMouseEvent<*, *>.toMouseEnteredData(targetID: ID?): MouseEnteredEventData {
    val (posX, posY) = mousePositionToScenePosition(clientX, clientY)
    return MouseEnteredEventData(posX, posY).apply { this.id = targetID }
  }

  fun ReactMouseEvent<*, *>.toMouseExitedData(targetID: ID?): MouseExitedEventData {
    val (posX, posY) = mousePositionToScenePosition(clientX, clientY)
    return MouseExitedEventData(posX, posY).apply { this.id = targetID }
  }

  fun ReactMouseEvent<*, *>.toMouseEventData(targetID: ID?): MouseEventData {
    val (posX, posY) = mousePositionToScenePosition(clientX, clientY)
    return MouseEventData(
            when (button as Int) {
              0 -> MouseButtonType.LEFT_BUTTON
              1 -> MouseButtonType.MOUSE_WHEEL
              2 -> MouseButtonType.RIGHT_BUTTON
              3,
              4 -> MouseButtonType.OTHER
              else -> MouseButtonType.UNSPECIFIED
            },
            posX,
            posY)
        .apply { this.id = targetID }
  }

  fun ReactMouseEvent<*, *>.toMousePressedEventData(targetID: ID?): MousePressedEventData {
    val (posX, posY) = mousePositionToScenePosition(clientX, clientY)
    return MousePressedEventData(
            when (button as Int) {
              0 -> MouseButtonType.LEFT_BUTTON
              1 -> MouseButtonType.MOUSE_WHEEL
              2 -> MouseButtonType.RIGHT_BUTTON
              3,
              4 -> MouseButtonType.OTHER
              else -> MouseButtonType.UNSPECIFIED
            },
            posX,
            posY)
        .apply { this.id = targetID }
  }

  fun ReactMouseEvent<*, *>.toMouseReleasedEventData(targetID: ID?): MouseReleasedEventData {
    val (posX, posY) = mousePositionToScenePosition(clientX, clientY)
    return MouseReleasedEventData(
            when (button as Int) {
              0 -> MouseButtonType.LEFT_BUTTON
              1 -> MouseButtonType.MOUSE_WHEEL
              2 -> MouseButtonType.RIGHT_BUTTON
              3,
              4 -> MouseButtonType.OTHER
              else -> MouseButtonType.UNSPECIFIED
            },
            posX,
            posY)
        .apply { this.id = targetID }
  }

  fun ReactKeyEvent<*>.toKeyEventData(targetID: ID?, action: KeyEventAction): KeyEventData {
    return KeyEventData(toKeyCode(), key, ctrlKey, shiftKey, altKey, action).apply {
      this.id = targetID
    }
  }

  private fun ReactKeyEvent<*>.toKeyCode(): KeyCode {
    KeyCode.entries.forEach { if (it.keyCodes.contains(this.key)) return it }
    return KeyCode.UNDEFINED
  }

  fun ReactDragEvent<*>.toDragEventData(targetID: ID?, action: DragEventAction): EventData {
    return when (action) {
      DragEventAction.START -> DragGestureStartedEventData().apply { this.id = targetID }
      DragEventAction.DROP -> {
        val id = dataTransfer.getData("text")
        DragDroppedEventData(targetID ?: "").apply { this.id = id }
      }
      DragEventAction.END -> TODO()
      DragEventAction.ENTER -> TODO()
      DragEventAction.EXIT -> TODO()
    }
  }

  fun DragEndEvent.toDragEventData(): EventData {
    val droppedOn = over?.id
    val elementDragged = active?.id

    return DragDroppedEventData(droppedOn ?: "").apply { this.id = elementDragged }
  }

  fun DragEndEvent.toDragEndedEventData(): EventData {
    val elementDragged = active?.id
    return DragGestureEndedEventData(over?.id).apply { this.id = elementDragged }
  }

  fun DragStartEvent.toDragStartedEventData(): EventData {
    val element = active?.id
    return DragGestureStartedEventData().apply { this.id = element }
  }

  fun DragMultiEvent.toDragMoveEventData(): EventData {
    return DragGestureMovedEventData().apply { this.id = active?.id }
  }

  fun DragMultiEvent.toDragEnteredEventData(): EventData {
    val element = over?.id
    if (element != null) {
      return DragGestureEnteredEventData(element).apply { this.id = active?.id }
    }

    return DragGestureEnteredEventData("").apply { this.id = active?.id }
  }

  fun WheelEvent<*>.toScrollEventData(targetID: ID?): ScrollEventData {
    return ScrollEventData(
            direction = if (this.deltaY > 0) WheelDirection.DOWN else WheelDirection.UP,
            shift = this.shiftKey,
            alt = this.altKey,
            ctrl = this.ctrlKey)
        .apply { this.id = targetID }
  }
}
