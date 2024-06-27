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

package tools.aqua.bgw.examples.maumau.view.scenes

import tools.aqua.bgw.core.Color
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.examples.maumau.main.BUTTON_BG_FILE
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.util.Font.FontStyle
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual

/** Player won [MenuScene]. */
class MauMauPlayerWonScene :
    MenuScene(width = 300, height = 500, background = ColorVisual(Color.WHITE)) {

  /** The menu [Label]. */
  private val menuLabel: Label =
      Label(
          height = 100,
          width = 200,
          posX = 50,
          posY = 0,
          text = "Game over!",
          font = Font(fontWeight = Font.FontWeight.BOLD))

  /** The winning player name [Label]. */
  private val playerWonLabel: Label =
      Label(
          height = 100,
          width = 200,
          posX = 50,
          posY = 100,
          text = "",
          font = Font(fontWeight = Font.FontWeight.BOLD))

  /** The winning player name. */
  var playerWon: String = "Alice"
    set(value) {
      field = value
      playerWonLabel.text = "$value won the game."
    }

  /** New game [Button]. */
  val newGameButton: Button =
      Button(
          height = 80,
          width = 200,
          posX = 50,
          posY = 220,
          text = "New Game",
          font = Font(color = Color.WHITE, fontStyle = FontStyle.ITALIC),
          visual = ImageVisual(BUTTON_BG_FILE))

  /** Exit game [Button]. */
  val exitButton: Button =
      Button(
          height = 80,
          width = 200,
          posX = 50,
          posY = 330,
          text = "Exit",
          font = Font(color = Color.WHITE, fontStyle = FontStyle.ITALIC),
          visual = ImageVisual(BUTTON_BG_FILE))

  init {
    addComponents(
        menuLabel,
        playerWonLabel,
        newGameButton,
        exitButton,
    )
  }
}
