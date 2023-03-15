/*
 * Copyright 2022-2023 The BoardGameWork Authors
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

package tools.aqua.bgw.examples.sudoku.view.scenes

import java.awt.Color
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.examples.sudoku.view.customcomponents.MenuButton
import tools.aqua.bgw.examples.sudoku.view.customcomponents.MenuToggleButton
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual

/** SettingsMenu scene. */
class SudokuSettingsScene :
    MenuScene(width = 300, height = 600, background = ColorVisual(Color.WHITE)) {

  /** [Label] for the title. */
  private val titleLabel: Label =
      Label(
          height = 100,
          width = 200,
          posX = 50,
          posY = 0,
          text = "Main menu",
          font = Font(fontWeight = Font.FontWeight.BOLD))

  /** [MenuToggleButton] "Show Timer". */
  val showTimerToggleButton: MenuToggleButton = MenuToggleButton(position = 1, text = "Show Timer")

  /** [MenuToggleButton] "Instant Check". */
  val instantCheckToggleButton: MenuToggleButton =
      MenuToggleButton(position = 2, text = "Instant Check")

  /** [Button] "Continue". */
  val continueGameButton: Button = MenuButton(position = 3, text = "Continue")

  init {
    addComponents(titleLabel, showTimerToggleButton, instantCheckToggleButton, continueGameButton)
  }
}
