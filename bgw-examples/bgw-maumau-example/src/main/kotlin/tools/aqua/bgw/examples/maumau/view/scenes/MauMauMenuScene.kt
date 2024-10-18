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

import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.LabeledUIComponent
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.examples.maumau.main.MENU_ITEM_HEIGHT
import tools.aqua.bgw.examples.maumau.main.MENU_ITEM_WIDTH
import tools.aqua.bgw.examples.maumau.view.customcomponents.MenuButton
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual

/** Main [MenuScene]. */
class MauMauMenuScene :
    MenuScene(width = 300, height = 500, background = ColorVisual(Color.WHITE)) {

  /** The menu [Label]. */
  private val menuLabel: Label =
      Label(
          height = MENU_ITEM_HEIGHT,
          width = MENU_ITEM_WIDTH,
          text = "Main menu",
          font = Font(fontWeight = Font.FontWeight.BOLD))

  /** Continue game [Button]. */
  val continueGameButton: Button = MenuButton("Continue")

  /** New local game [Button]. */
  val newLocalGameButton: Button = MenuButton("New Local Game")

  /** Host game [Button]. */
  val hostGameButton: Button = MenuButton("Host Game")

  /** Join game [Button]. */
  val joinGameButton: Button = MenuButton("Join Game")

  /** Exit game [Button]. */
  val exitButton: Button = MenuButton("Exit")

  init {
    addComponents(
        GridPane<LabeledUIComponent>(columns = 1, rows = 6, spacing = 15, layoutFromCenter = false)
            .apply {
              this[0, 0] = menuLabel
              this[0, 1] = continueGameButton
              this[0, 2] = newLocalGameButton
              this[0, 3] = hostGameButton
              this[0, 4] = joinGameButton
              this[0, 5] = exitButton

              setColumnWidth(0, 300)
              setCenterMode(Alignment.CENTER)
            })
  }
}
