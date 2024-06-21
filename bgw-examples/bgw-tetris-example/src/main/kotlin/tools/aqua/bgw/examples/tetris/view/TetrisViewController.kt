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

package tools.aqua.bgw.examples.tetris.view

import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.event.KeyCode
import tools.aqua.bgw.examples.tetris.service.LogicController

/** Main ViewController. */
class TetrisViewController : BoardGameApplication(windowTitle = "Tetris") {

  /** The gameScene. */
  val tetrisGameScene: TetrisGameScene = TetrisGameScene()

  /** RefreshController instance for the [LogicController] callbacks. */
  private val refreshViewController: RefreshViewController = RefreshViewController(this)

  /** Logic controller instance. */
  private val logicController: LogicController = LogicController(refreshViewController)

  init {
    tetrisGameScene.registerEvents()

    onWindowClosed = { logicController.stopTimer() }

    showGameScene(tetrisGameScene)
    show()
  }

  /** Registers events for [tetrisGameScene]. */
  private fun TetrisGameScene.registerEvents() {
    onKeyPressed = {
      when {
        it.keyCode.isArrow() -> logicController.navigate(it.keyCode)
        it.keyCode == KeyCode.ENTER -> logicController.startGame()
      }
    }
  }
}
