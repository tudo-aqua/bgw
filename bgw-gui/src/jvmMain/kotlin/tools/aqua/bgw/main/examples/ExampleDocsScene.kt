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
import jsonMapper
import kotlin.reflect.KProperty0
import kotlinx.serialization.encodeToString
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.container.LinearLayout
import tools.aqua.bgw.components.gamecomponentviews.*
import tools.aqua.bgw.components.uicomponents.*
import tools.aqua.bgw.core.*
import tools.aqua.bgw.main.examples.ExampleDocsScene as Scene
import tools.aqua.bgw.style.BorderRadius
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual

/** Metadata: [Scene] */
internal class ExampleDocsScene : BoardGameScene(width = 445, height = 300) {
  val map = mutableMapOf<String, String>()

  val tokenRect = TokenView(width = 100, height = 100, visual = ColorVisual(Color(0xc6ff6e)))

  val tokenCircle =
      TokenView(
          width = 100,
          height = 100,
          visual = ColorVisual(Color(0xffc656)).apply { style.borderRadius = BorderRadius.FULL })

  val cardImage =
      CardView(width = 120, height = 200, front = ImageVisual("assets/elements/fortify.png"))

  val cardBack =
      CardView(
              width = 120,
              height = 200,
              front = ImageVisual("assets/elements/fortify.png"),
              back = ImageVisual("assets/elements/back.png"),
          )
          .apply { showBack() }

  val diceDefaultSide =
      DiceView(width = 100, height = 100, visuals = listOf(ColorVisual(Color(0xef4444))))

  val diceCustomSide =
      DiceView(
              width = 100,
              height = 100,
              visuals =
                  listOf(
                      ColorVisual(Color(0xc6ff6e)),
                      ColorVisual(Color(0xffc656)),
                      ColorVisual(Color(0xfa6c56)), // Orange
                      ColorVisual(Color(0xef4444)),
                      ColorVisual(Color(0xbb6dff)),
                      ColorVisual(Color(0x6dbeff))))
          .apply { currentSide = 2 }

  val hexagonPointy = HexagonView(size = 60, visual = ColorVisual(Color(0xbb6dff)))

  val hexagonFlat =
      HexagonView(
          size = 60, visual = ColorVisual(Color(0x6dbeff)), orientation = HexOrientation.FLAT_TOP)

  val labelNoWrap =
      Label(
          posX = centerX(200),
          posY = 40,
          width = 200,
          height = 75,
          text = "I am a Label with no wrapping.",
          alignment = Alignment.CENTER,
          font = Font(20.0, Color.WHITE))

  val labelWrap =
      Label(
          posX = centerX(200),
          posY = 145,
          width = 200,
          height = 100,
          text = "This label has a long text that will wrap around.",
          alignment = Alignment.CENTER_LEFT,
          font = Font(20.0, Color(0x6dbeff)),
          isWrapText = true)

  val button =
      Button(
          posX = centerX(250),
          posY = centerY(50),
          width = 250,
          height = 75,
          text = "I am a Button.",
          font = Font(20.0, Color(0x0f141f)),
          visual = ColorVisual(Color(0xc6ff6e)))

  val checkBox =
      CheckBox(
              posX = centerX(250),
              posY = centerY(50),
              width = 250,
              height = 50,
              text = "I am a CheckBox.",
              alignment = Alignment.CENTER,
              font = Font(20.0, Color.WHITE),
              allowIndeterminate = true)
          .apply { isChecked = true }

  val colorPicker =
      ColorPicker(
          posX = centerX(100),
          posY = centerY(100),
          width = 100,
          height = 100,
          initialColor = Color(0xfa6c56))

  val comboBox =
      ComboBox<Double>(
              posX = centerX(300),
              posY = centerY(50),
              width = 300,
              height = 50,
              visual = ColorVisual(Color(0xffc656)),
              prompt = "Select an option...",
              font = Font(20.0, Color(0x0f141f)),
              items = listOf(0.0, 1.0, 2.0),
          )
          .apply { formatFunction = { "Option ${it.toInt()}" } }

  val progressBar =
      ProgressBar(
          posX = centerX(300),
          posY = centerY(50),
          width = 300,
          height = 50,
          progress = 0.85,
          visual = ColorVisual(Color(0x0f141f)),
          barColor = Color(0xef4444))

  val toggleGroup = ToggleGroup()

  val toggleButton =
      ToggleButton(
              posX = centerX(300),
              posY = 75,
              width = 300,
              height = 50,
              text = "I am a ToggleButton.",
              alignment = Alignment.CENTER,
              font = Font(20.0, Color.WHITE),
              toggleGroup = toggleGroup)
          .apply { isSelected = true }

  val radioButton =
      RadioButton(
          posX = centerX(280),
          posY = 175,
          width = 280,
          height = 50,
          text = "I am a RadioButton.",
          alignment = Alignment.CENTER,
          font = Font(20.0, Color.WHITE),
          toggleGroup = toggleGroup)

  val textArea =
      TextArea(
          posX = centerX(300),
          posY = 35,
          width = 300,
          height = 75,
          font = Font(20.0, Color(0x0f141f)),
          prompt = "I am a\nTextArea.")

  val textField =
      TextField(
          posX = centerX(300),
          posY = 135,
          width = 300,
          height = 50,
          visual = ColorVisual(Color(0x0f141f)),
          font = Font(20.0, Color.WHITE),
          prompt = "I am a TextField.",
          text = "Initial text")

  val passwordField =
      PasswordField(
          posX = centerX(300),
          posY = 210,
          width = 300,
          height = 50,
          visual = ColorVisual(Color(0x6dbeff)),
          font = Font(20.0, Color(0x0f141f)),
          prompt = "I am a PasswordField.",
          text = "Hidden")

  val listView =
      ListView(
              posX = centerX(150),
              posY = centerY(200),
              width = 150,
              height = 200,
              items = mutableListOf(42, 1337, 1, 2, 3),
              visual = ColorVisual(Color(0x0f141f)),
              font = Font(20.0, Color.WHITE),
              selectionMode = SelectionMode.SINGLE,
              selectionBackground = ColorVisual(Color(0xbb6dff)),
          )
          .apply { selectIndex(1) }

  val table =
      TableView<Int>(
              posX = centerX(300),
              posY = centerY(200),
              width = 300,
              height = 200,
              visual = ColorVisual(Color(0x0f141f)),
              selectionMode = SelectionMode.MULTIPLE,
              selectionBackground = ColorVisual(Color(0xef4444)),
          )
          .apply {
            columns.addAll(
                listOf(
                    TableColumn(title = "Value", width = 100, font = Font(20.0, Color.WHITE)) {
                      "$it"
                    },
                    TableColumn(title = "Squared", width = 100, font = Font(20.0, Color.WHITE)) {
                      "${it * it}"
                    },
                    TableColumn(title = "Even?", width = 100, font = Font(20.0, Color.WHITE)) {
                      "${it % 2 == 0}"
                    }))

            items.addAll(listOf(42, 1337, 1, 2, 3))
          }
          .apply {
            selectIndex(1)
            selectIndex(2)
          }

  val linearLayout =
      LinearLayout<GameComponentView>(
          posX = 0,
          posY = 0,
          width = this.width,
          height = 300,
          orientation = Orientation.HORIZONTAL,
          alignment = Alignment.CENTER,
          spacing = 50)

  init {
    setComponentsAndSerialize("tokenExample", ::tokenRect, ::tokenCircle)
    setComponentsAndSerialize("cardExample", ::cardImage, ::cardBack)
    setComponentsAndSerialize("hexagonExample", ::hexagonPointy, ::hexagonFlat)
    setComponentsAndSerialize("diceExample", ::diceDefaultSide, ::diceCustomSide)
    setUIComponentsAndSerialize("labelExample", ::labelNoWrap, ::labelWrap)
    setUIComponentsAndSerialize("buttonExample", ::button)
    setUIComponentsAndSerialize("checkBoxExample", ::checkBox)
    setUIComponentsAndSerialize("colorPickerExample", ::colorPicker)
    setUIComponentsAndSerialize("comboBoxExample", ::comboBox)
    setUIComponentsAndSerialize("progressBarExample", ::progressBar)
    setUIComponentsAndSerialize("binaryButtonExample", ::toggleButton, ::radioButton)
    setUIComponentsAndSerialize("textExample", ::textArea, ::textField, ::passwordField)
    setUIComponentsAndSerialize("listViewExample", ::listView)
    setUIComponentsAndSerialize("tableViewExample", ::table)
  }

  internal fun centerX(width: Int): Double {
    return (this.width - width) / 2
  }

  internal fun centerY(height: Int): Double {
    return (this.height - height) / 2
  }

  internal fun <T : ComponentView> setComponentsAndSerialize(
      name: String,
      vararg props: KProperty0<T>
  ) {
    clearComponents()
    linearLayout.clear()

    props.forEach { prop ->
      val comp = prop.get()
      linearLayout.add(comp as GameComponentView)
    }

    addComponents(linearLayout)

    val fullyQualifiedName = "${this::class.qualifiedName}.$name"

    val appData = SceneMapper.map(menuScene = null, gameScene = this)
    val json = jsonMapper.encodeToString(PropData(appData))
    map[fullyQualifiedName] = json

    clearComponents()
  }

  internal fun <T : ComponentView> setUIComponentsAndSerialize(
      name: String,
      vararg props: KProperty0<T>
  ) {
    clearComponents()

    props.forEach { prop ->
      val comp = prop.get()
      addComponents(comp)
    }

    val fullyQualifiedName = "${this::class.qualifiedName}.$name"

    val appData = SceneMapper.map(menuScene = null, gameScene = this)
    val json = jsonMapper.encodeToString(PropData(appData))
    map[fullyQualifiedName] = json

    clearComponents()
  }
}
