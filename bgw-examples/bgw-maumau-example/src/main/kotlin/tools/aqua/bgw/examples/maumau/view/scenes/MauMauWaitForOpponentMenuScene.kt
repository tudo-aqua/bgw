/*
 * Copyright 2022 The BoardGameWork Authors
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

import java.awt.Color
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.examples.maumau.main.MENU_ITEM_HEIGHT
import tools.aqua.bgw.examples.maumau.main.MENU_ITEM_WIDTH
import tools.aqua.bgw.examples.maumau.view.customcomponents.MenuButton
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual

/** ViewController for the wait for opponent menu scene. */
class MauMauWaitForOpponentMenuScene :
    MenuScene(width = 300, height = 500, background = ColorVisual(Color.WHITE)) {

  /** The menu [Label]. */
  private val menuLabel: Label =
      Label(
          height = MENU_ITEM_HEIGHT,
          width = MENU_ITEM_WIDTH,
          posX = (300 - MENU_ITEM_WIDTH) / 2,
          posY = (500 - MENU_ITEM_HEIGHT) / 2,
          font = Font(fontWeight = Font.FontWeight.BOLD))

  /** Start game [Button]. */
  val startGameButton: Button =
      MenuButton("Start Game").apply {
        posX = (300 - MENU_ITEM_WIDTH) / 2.0
        posY = 400.0
        isVisible = false
      }

  private var dots = 1
  private val timer = Timer()

  init {
    addComponents(menuLabel, startGameButton)
  }

  /** Starts the dot animation. */
  fun startAnimation() {
    timer.scheduleAtFixedRate(delay = 0, period = 500) {
      BoardGameApplication.runOnGUIThread {
        menuLabel.text = "Waiting for opponent" + ".".repeat(dots)
        dots %= 3
        dots++
      }
    }
  }

  /** Transitions to player connected state and shows [startGameButton]. */
  fun onOpponentConnected(name: String) {
    stopAnimation()
    menuLabel.text = "$name connected."
    startGameButton.isVisible = true
  }

  /** Stops the dot animation. */
  private fun stopAnimation() {
    timer.cancel()
  }
}
