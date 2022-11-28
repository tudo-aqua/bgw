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

package examples.dialog

import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.dialog.FileDialog
import tools.aqua.bgw.dialog.FileDialogMode
import tools.aqua.bgw.visual.ColorVisual

fun main() {
  FileDialogExample()
}

class FileDialogExample : BoardGameApplication("FileDialog example") {
  private val gameScene: BoardGameScene = BoardGameScene(background = ColorVisual.GRAY)

  private val buttonOpenFile: Button =
      Button(posX = 700, posY = 500, text = "OpenFile").apply {
        visual = ColorVisual.WHITE
        onMouseClicked = {
          showFileDialog(
              FileDialog(
                  mode = FileDialogMode.OPEN_FILE,
                  title = "Open file",
              ))
        }
      }
  private val buttonOpenFiles: Button =
      Button(posX = 900, posY = 500, text = "OpenFiles").apply {
        visual = ColorVisual.WHITE
        onMouseClicked = {
          showFileDialog(
              FileDialog(
                  mode = FileDialogMode.OPEN_MULTIPLE_FILES,
                  title = "Open files",
              ))
        }
      }
  private val buttonSaveFile: Button =
      Button(posX = 1100, posY = 500, text = "SaveFile").apply {
        visual = ColorVisual.WHITE
        onMouseClicked = {
          showFileDialog(
              FileDialog(
                  mode = FileDialogMode.SAVE_FILE,
                  title = "Save file",
              ))
        }
      }
  private val buttonChooseDirectory: Button =
      Button(posX = 900, posY = 600, text = "ChooseDir").apply {
        visual = ColorVisual.WHITE
        onMouseClicked = {
          showFileDialog(
                  FileDialog(
                      mode = FileDialogMode.CHOOSE_DIRECTORY,
                      title = "Choose directory",
                  ))
              .ifPresent { l ->
                println("Chosen Directory:")
                l.forEach { t -> println(t) }
              }
        }
      }

  init {
    gameScene.addComponents(buttonOpenFile, buttonOpenFiles, buttonSaveFile, buttonChooseDirectory)
    showGameScene(gameScene)
    show()
  }
}
