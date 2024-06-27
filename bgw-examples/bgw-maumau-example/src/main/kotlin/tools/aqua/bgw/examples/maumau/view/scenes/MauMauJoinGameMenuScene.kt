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
import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.components.uicomponents.*
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.examples.maumau.main.MENU_ITEM_HEIGHT
import tools.aqua.bgw.examples.maumau.main.MENU_ITEM_WIDTH
import tools.aqua.bgw.examples.maumau.view.customcomponents.MenuButton
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual

/** Join game [MenuScene]. */
class MauMauJoinGameMenuScene :
    MenuScene(width = 300, height = 500, background = ColorVisual(Color.WHITE)) {

  /** The menu [Label]. */
  private val menuLabel: Label =
      Label(
          height = MENU_ITEM_HEIGHT,
          width = MENU_ITEM_WIDTH,
          text = "Join Game",
          font = Font(fontWeight = Font.FontWeight.BOLD))

  /** [TextField] for the hostname or address and port. */
  val addressText: TextField =
      TextField(
          height = 40,
          width = MENU_ITEM_WIDTH,
          text = "sopra.cs.tu-dortmund.de:80/bgw-net/connect",
          prompt = "Server address")

  /** [PasswordField] for the network secret. */
  val secretText: PasswordField =
      PasswordField(height = 40, width = MENU_ITEM_WIDTH, prompt = "Secret")

  /** [TextField] for the player's name. */
  val nameText: TextField = TextField(height = 40, width = MENU_ITEM_WIDTH, prompt = "Your Name")

  /** [PasswordField] for the session id. */
  val sessionIDText: PasswordField =
      PasswordField(height = 40, width = MENU_ITEM_WIDTH, prompt = "sessionID")

  /** Join game [Button]. */
  val joinGameButton: Button = MenuButton("Join Game")

  /** Back [Button]. */
  val backButton: Button = MenuButton("← Back")

  init {
    addComponents(
        GridPane<UIComponent>(columns = 1, rows = 7, spacing = 15, layoutFromCenter = false).apply {
          this[0, 0] = menuLabel
          this[0, 1] = addressText
          this[0, 2] = secretText
          this[0, 3] = nameText
          this[0, 4] = sessionIDText
          this[0, 5] = joinGameButton
          this[0, 6] = backButton

          setColumnWidth(0, this@MauMauJoinGameMenuScene.width)
          setCenterMode(Alignment.CENTER)
        })
  }
}
