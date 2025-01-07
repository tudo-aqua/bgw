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

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.layoutviews.CameraPane
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual

internal class GridGameScene : BoardGameScene() {
  private val targetPane =
      Pane<ComponentView>(
          posX = 0, posY = 0, width = 700, height = 500, visual = ImageVisual("assets/3.jpg"))

  private val cameraPane =
      CameraPane(
              posX = 150,
              posY = 200,
              width = 600,
              height = 500,
              target = targetPane,
              visual = ColorVisual.BLUE)
          .apply { interactive = true }

  init {
    addComponents(cameraPane)
  }
}
