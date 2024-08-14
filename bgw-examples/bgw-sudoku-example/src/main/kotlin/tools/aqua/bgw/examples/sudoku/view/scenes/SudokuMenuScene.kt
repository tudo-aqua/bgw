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

package tools.aqua.bgw.examples.sudoku.view.scenes

import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.examples.sudoku.view.customcomponents.MenuButton
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual

/** MainMenu scene. */
class SudokuMenuScene :
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

  /** [Button] "Continue". */
  val continueGameButton: Button = MenuButton(position = 1, text = "Continue")

  /** [Button] "New Easy Game". */
  val newGameEasyButton: Button = MenuButton(position = 2, text = "New Easy Game")

  /** [Button] "New Medium Game". */
  val newGameMediumButton: Button = MenuButton(position = 3, text = "New Medium Game")

  /** [Button] "New Hard Game". */
  val newGameHardButton: Button = MenuButton(position = 4, text = "New Hard Game")

  /** [Button] "Exit". */
  val exitButton: Button = MenuButton(position = 5, text = "Exit")

  init {
    addComponents(
        titleLabel,
        continueGameButton,
        newGameEasyButton,
        newGameMediumButton,
        newGameHardButton,
        exitButton,
    )
  }
}
