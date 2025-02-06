/*
 * Copyright 2022-2025 The BoardGameWork Authors
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

package examples.concepts.userinput

import tools.aqua.bgw.components.gamecomponentviews.TokenView
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.event.KeyCode
import tools.aqua.bgw.event.MouseEvent
import tools.aqua.bgw.visual.ColorVisual

fun main() {
  UserInputExample()
}

class UserInputExample : BoardGameApplication("User input example") {

  private val button: Button =
      Button(height = 150, width = 300, posX = 30, posY = 30).apply { visual = ColorVisual.GREEN }

  private val token: TokenView = TokenView(posX = 500, posY = 30, visual = ColorVisual.RED)

  private val gameScene: BoardGameScene = BoardGameScene(background = ColorVisual.LIGHT_GRAY)

  init {
    // handling user input on ComponentView
    button.onMouseClicked = this::handleMouseClicked

    button.onMousePressed = { mouseEvent -> button.text = "pressed ${mouseEvent.button}" }
    button.onMouseReleased = { mouseEvent -> button.text = "released ${mouseEvent.button}" }
    button.onMouseEntered = { button.visual = ColorVisual.MAGENTA }
    button.onMouseExited = { button.visual = ColorVisual.GREEN }
    button.onKeyPressed = { keyEvent -> button.text = "pressed key: ${keyEvent.keyCode}" }
    button.onKeyReleased = { keyEvent -> button.text = "released key: ${keyEvent.keyCode}" }
    button.onKeyTyped = { keyEvent -> button.text = "typed key: ${keyEvent.character}" }
    button.dropAcceptor = { true }
    button.onDragDropped = {
      it.draggedComponent.reposition(500, 30)
      it.draggedComponent.rotation = 0.0
      gameScene.addComponents(token)
    }
    button.onDragGestureEntered = { dragEvent -> button.visual = dragEvent.draggedComponent.visual }
    button.onDragGestureExited = { button.visual = ColorVisual.GREEN }

    // Additional function references available only to DynamicComponentViews
    token.isDraggable = true

    token.onDragGestureMoved = { token.rotate(5) }
    token.onDragGestureStarted = { token.scale(1.2) }
    token.onDragGestureEnded = { _, success -> if (success) token.resize(50, 50) }

    // Global input listener
    gameScene.onKeyPressed = { event -> if (event.keyCode == KeyCode.ESCAPE) exit() }

    showGameScene(gameScene.apply { addComponents(button, token) })
    show()
  }

  private fun handleMouseClicked(@Suppress("UNUSED_PARAMETER") mouseEvent: MouseEvent) {
    button.text = "someone clicked on me!"
  }
}
