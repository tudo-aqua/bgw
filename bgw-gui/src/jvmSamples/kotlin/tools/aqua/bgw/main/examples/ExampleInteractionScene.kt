/*
 * Copyright 2025-2026 The BoardGameWork Authors
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

package tools.aqua.bgw.main.examples

import PropData
import jsonMapper
import kotlinx.serialization.encodeToString
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.container.Area
import tools.aqua.bgw.components.gamecomponentviews.GameComponentView
import tools.aqua.bgw.components.gamecomponentviews.TokenView
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.ListView
import tools.aqua.bgw.components.uicomponents.SelectionMode
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.main.examples.ExampleInteractionScene as Scene
import tools.aqua.bgw.style.BorderRadius
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual

/** Metadata: [Scene] */
internal class ExampleInteractionScene : BoardGameScene(width = 445, height = 300) {
  val map = mutableMapOf<String, String>()

  // region Drag and drop
  val redToken =
      TokenView(
              posX = 40,
              posY = 40,
              width = 70,
              height = 70,
              visual =
                  ColorVisual(Color(0xEF4444)).apply { style.borderRadius = BorderRadius.FULL })
          .apply { isDraggable = true }

  val greenToken =
      TokenView(
              posX = 40,
              posY = 190,
              width = 70,
              height = 70,
              visual =
                  ColorVisual(Color(0xC6FF6E)).apply { style.borderRadius = BorderRadius.FULL })
          .apply { isDraggable = true }

  val redArea =
      Area<GameComponentView>(
          posX = 310,
          posY = 25,
          width = 100,
          height = 100,
          visual = ColorVisual(Color(0xEF4444)).apply { transparency = 0.3 })

  val greenArea =
      Area<GameComponentView>(
          posX = 310,
          posY = 175,
          width = 100,
          height = 100,
          visual = ColorVisual(Color(0xC6FF6E)).apply { transparency = 0.3 })
  // endregion

  // region Observable
  val names =
      ListView(
          posX = 122.5,
          posY = 50,
          width = 200,
          height = 200,
          items = mutableListOf("Alice", "Bob", "Carol"),
          visual = ColorVisual(Color(0x0F141F)),
          font = Font(20.0, Color.WHITE),
          selectionMode = SelectionMode.SINGLE,
          selectionBackground = ColorVisual(Color(0xBB6DFF)))
  // endregion

  // region Mouse and keys
  val interactiveButton =
      Button(
          posX = 72.5,
          posY = 100,
          width = 300,
          height = 100,
          text = "Click, hover or focus me",
          font = Font(20.0, Color(0x0F141F)),
          visual = ColorVisual(Color(0xFFC656)).apply { style.borderRadius = BorderRadius.MEDIUM })

  // endregion

  init {
    serialize("dragAndDropExample", redToken, greenToken, redArea, greenArea)
    serialize("observableListExample", names)
    serialize("interactiveButtonExample", interactiveButton)
  }

  private fun serialize(name: String, vararg components: ComponentView) {
    clearComponents()
    addComponents(*components)

    val appData = SceneMapper.map(menuScene = null, gameScene = this)
    map["${this::class.qualifiedName}.$name"] = jsonMapper.encodeToString(PropData(appData))

    clearComponents()
  }
}
