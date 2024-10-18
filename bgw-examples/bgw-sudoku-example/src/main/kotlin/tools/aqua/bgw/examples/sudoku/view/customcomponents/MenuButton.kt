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

package tools.aqua.bgw.examples.sudoku.view.customcomponents

import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.ToggleButton
import tools.aqua.bgw.components.uicomponents.UIComponent
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual

/**
 * MenuButton with automated y coordinate.
 *
 * @param position Position index.
 * @param text Text to display.
 */
class MenuButton(position: Int, text: String) :
    Button(
        height = 80,
        width = 200,
        posX = 50,
        posY = position * 100,
        text = text,
        font = Font(color = Color.WHITE),
        visual = ColorVisual.BLACK)

/**
 * MenuToggleButton with automated y coordinate and text.
 *
 * @param position Position index.
 * @param text Text to display.
 * @param isSelected Whether [ToggleButton] is selected.
 */
class MenuToggleButton(position: Int, text: String, isSelected: Boolean = false) :
    Pane<UIComponent>(
        height = 80,
        width = 200,
        posX = 50,
        posY = position * 100,
    ) {
  /** The [ToggleButton]. */
  val button: ToggleButton =
      ToggleButton(height = 80, width = 50, posX = 0, posY = 0, isSelected = isSelected)

  /** The [Label] beside the [ToggleButton]. */
  private val label: Label = Label(height = 80, width = 150, posX = 50, posY = 0, text = text)

  init {
    addAll(button, label)
  }
}
