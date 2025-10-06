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

package tools.aqua.bgw.main.examples

import PropData
import java.awt.Color
import jsonMapper
import kotlin.reflect.KProperty0
import kotlinx.serialization.encodeToString
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.container.*
import tools.aqua.bgw.components.gamecomponentviews.*
import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.components.uicomponents.*
import tools.aqua.bgw.core.*
import tools.aqua.bgw.main.examples.ExampleDocsScene as Scene
import tools.aqua.bgw.style.BorderRadius
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.CompoundVisual
import tools.aqua.bgw.visual.TextVisual
import tools.aqua.bgw.visual.Visual

/** Metadata: [Scene] */
internal class AdvancedSceneComponents :
    BoardGameScene(width = 445, height = 300, background = Visual.EMPTY) {
  val map = mutableMapOf<String, String>()

  class ExampleButton(posX: Int = 0, posY: Int = 0, text: String = "Button") :
      Button(
          posX = posX,
          posY = posY,
          width = 170,
          height = 50,
          text = text,
          font = Font(14.0, Color(0x0f141f), "Arial", Font.FontWeight.BOLD),
          visual = ColorVisual(Color(0xbb6dff)).apply { style.borderRadius = BorderRadius(7) })

  val exampleButton =
      ExampleButton(posX = centerX(170).toInt(), posY = centerY(50).toInt(), text = "Primary")

  val exampleButton1 = ExampleButton(text = "Primary")
  val exampleButton2 = ExampleButton(text = "Secondary")
  val exampleButton3 = ExampleButton(text = "Tertiary")

  val grid =
      GridPane<ComponentView>(
              posX = 137.5,
              posY = 60,
              layoutFromCenter = false,
              rows = 3,
              columns = 1,
              spacing = 15)
          .apply {
            grid[0, 0] = exampleButton1
            grid[0, 1] = exampleButton2
            grid[0, 2] = exampleButton3
          }

  class ExampleToken(posX: Int = 0, posY: Int = 0, width: Int = 170, text: String = "Token") :
      TokenView(
          posX = posX,
          posY = posY,
          width = width,
          height = 50,
          visual =
              CompoundVisual(
                  ColorVisual(Color(0x6dbeff)).apply { style.borderRadius = BorderRadius(7) },
                  TextVisual(
                      text = text,
                      font = Font(14.0, Color(0x0f141f), "Arial", Font.FontWeight.BOLD),
                      alignment = Alignment.CENTER)))

  val exampleToken1 = ExampleToken(text = "Token 1", width = 170)
  val exampleToken2 = ExampleToken(text = "Token 2", width = 140)
  val exampleToken3 = ExampleToken(text = "Token 3", width = 110)

  val linearLayout =
      LinearLayout<GameComponentView>(
              posX = 0,
              posY = 0,
              width = 445,
              height = 300,
              orientation = Orientation.VERTICAL,
              alignment = Alignment.CENTER,
              spacing = 15)
          .apply { addAll(exampleToken1, exampleToken2, exampleToken3) }

  enum class Theme(private val background: Color, private val foreground: Color) {
    PRIMARY(Color(0xef4444), Color(0x0f141f)),
    SECONDARY(Color(0xfa6c56), Color(0x0f141f)),
    TERTIARY(Color(0xffc656), Color(0x0f141f));

    fun getBackground() = background

    fun getForeground() = foreground
  }

  class NewExampleButton(text: String = "Button", theme: Theme = Theme.PRIMARY) :
      Button(
          posX = 0,
          posY = 0,
          width = 170,
          height = 50,
          text = text,
          font = Font(14.0, theme.getForeground(), "Arial", Font.FontWeight.BOLD),
          visual =
              ColorVisual(theme.getBackground()).apply { style.borderRadius = BorderRadius(7) })

  val newExampleButton1 = NewExampleButton(text = "Primary", theme = Theme.PRIMARY)

  val newExampleButton2 = NewExampleButton(text = "Secondary", theme = Theme.SECONDARY)

  val newExampleButton3 = NewExampleButton(text = "Tertiary", theme = Theme.TERTIARY)

  val colorGrid =
      GridPane<ComponentView>(
              posX = 137.5,
              posY = 60,
              layoutFromCenter = false,
              rows = 3,
              columns = 1,
              spacing = 15)
          .apply {
            grid[0, 0] = newExampleButton1
            grid[0, 1] = newExampleButton2
            grid[0, 2] = newExampleButton3
          }

  val label =
      Label(
          posX = centerX(300),
          posY = centerY(200),
          width = 300,
          height = 200,
          text = "The quick brown fox jumps over the lazy dog.",
          font = Font(20.0, Color(0xFFFFFF), "JetBrains Mono", Font.FontWeight.NORMAL),
          isWrapText = true)

  init {
    setComponentAndSerialize(::exampleButton)
    setComponentAndSerialize(::grid)
    setComponentAndSerialize(::linearLayout)
    setComponentAndSerialize(::colorGrid)
    setComponentAndSerialize(::label)
  }

  internal fun centerX(width: Int): Double {
    return (this.width - width) / 2
  }

  internal fun centerY(height: Int): Double {
    return (this.height - height) / 2
  }

  internal fun <T : ComponentView> setComponentAndSerialize(prop: KProperty0<T>) {
    val comp = prop.get()

    clearComponents()
    addComponents(comp)

    val fullyQualifiedName = "${this::class.qualifiedName}.${prop.name}"

    val appData = SceneMapper.map(menuScene = null, gameScene = this)
    val json = jsonMapper.encodeToString(PropData(appData))
    map[fullyQualifiedName] = json

    clearComponents()
  }
}
