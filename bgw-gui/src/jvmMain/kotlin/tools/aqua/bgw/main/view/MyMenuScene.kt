/*
 * Copyright 2025 The BoardGameWork Authors
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

package tools.aqua.bgw.main.view

import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual

internal class MyMenuScene : MenuScene() {
  private val label =
      Button(
              text = "Hello World!",
              width = 200,
              height = 200,
              font = Font(size = 36),
              posX = this.width / 2 - 100,
              posY = this.height / 2 - 100)
          .apply { onMouseClicked = { Application.hideMenuScene() } }

  private val testButton =
      Button(
              text = "Test",
              width = 200,
              height = 200,
              font = Font(size = 36),
              posX = this.width / 2 - 100,
              posY = this.height - 300,
              visual = ColorVisual.MAGENTA)
          .apply {
            onMouseClicked = {
              if (this.opacity == 1.0) {
                this.opacity = 0.2
              } else {
                this.opacity = 1.0
              }
            }
          }

  init {
    addComponents(label, testButton)
  }
}
