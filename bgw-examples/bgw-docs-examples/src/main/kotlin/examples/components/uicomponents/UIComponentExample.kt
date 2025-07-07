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

package examples.components.uicomponents

import tools.aqua.bgw.components.uicomponents.*
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual

fun main() {
    // Create rooms for different game lobbies
    val room1 = UIComponentExample("lobby1")
    val room2 = UIComponentExample("lobby2")
}

class UIComponentExample(roomId : String) : BoardGameApplication("UIComponent Example", width = 800, height = 1000, roomId = roomId, headless = true) {
  private val gameScene1 = BoardGameScene(width = 800).apply {
      this.opacity = 1.0
      this.background = ColorVisual.LIGHT_GRAY
  }
  private val gameScene2 = BoardGameScene(width = 800).apply {
      this.opacity = 1.0
      this.background = ColorVisual.DARK_GRAY
  }

  private val outputLabel =
      Label(
          posX = 50,
          posY = 50,
          width = 300,
          text = "I am a Label.",
          alignment = Alignment.CENTER,
          isWrapText = true,
          visual = ColorVisual.GREEN)

    private val outputLabel2 =
        Label(
            posX = 50,
            posY = 50,
            width = 300,
            text = "I am a second Label.",
            alignment = Alignment.CENTER,
            isWrapText = true,
            visual = ColorVisual.RED)

  init {
    gameScene1.addComponents(outputLabel)
    gameScene2.addComponents(outputLabel2)

    // Button
    val button =
        Button(posX = 450, posY = 50, text = "I am a Button.", visual = ColorVisual.LIGHT_GRAY)

    button.onMouseClicked = {
        showGameScene(gameScene2)
    }

    val button2 =
        Button(
            posX = 450,
            posY = 100,
            text = "I am a second Button.",
            visual = ColorVisual.DARK_GRAY)

    button2.onMouseClicked = {
        showGameScene(gameScene1)
    }

    gameScene1.addComponents(button)
    gameScene2.addComponents(button2)

      if(roomId == "lobby1") {
          showGameScene(gameScene1)
      } else {
          showGameScene(gameScene2)
      }
      show()
  }
}
