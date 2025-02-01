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

import tools.aqua.bgw.core.AspectRatio
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.visual.ColorVisual

internal object Application : BoardGameApplication(aspectRatio = AspectRatio.of(1080, 700)) {
  private val grid = GridGameScene()
  private val hexGrid = HexGridGameScene()
  private val animation = AnimationScene()
  val menuScene = MyMenuScene()
  val uiScene = UIScene()
  private val dragDropScene = DragDropScene()
  private val visualScene = VisualScene()
  val cardLayoutScene = CardLayoutScene()

  init {
    loadFont("Rubik.ttf")
    // showGameScene(cardLayoutScene)
    // showGameScene(hexGrid)
    // showGameScene(animation)
    // showGameScene(grid)
    // showGameScene(dragDropScene)
    showMenuScene(uiScene)
    // showGameScene(visualScene)
  }

  fun updateSome() {
    hexGrid.background = ColorVisual(Color(255, 0, 0, 0.5))
  }
}
