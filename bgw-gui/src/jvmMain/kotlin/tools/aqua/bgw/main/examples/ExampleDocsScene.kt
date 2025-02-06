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
import tools.aqua.bgw.components.container.*
import tools.aqua.bgw.components.gamecomponentviews.*
import tools.aqua.bgw.components.layoutviews.CameraPane
import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.*
import tools.aqua.bgw.core.*
import tools.aqua.bgw.main.examples.ExampleDocsScene as Scene
import tools.aqua.bgw.style.BorderRadius
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.CompoundVisual
import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.visual.TextVisual

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

  val _linearLayout =
      LinearLayout<GameComponentView>(
          posX = 0,
          posY = 0,
          width = this.width,
          height = 300,
          orientation = Orientation.HORIZONTAL,
          alignment = Alignment.CENTER,
          spacing = 50)

  val absoluteLabel =
      Label(posX = 75, posY = 112, width = 75, height = 75, visual = ColorVisual(Color(0xc6ff6e)))

  val paneLabel =
      Label(posX = 75, posY = 112, width = 75, height = 75, visual = ColorVisual(Color(0x6dbeff)))

  val pane =
      Pane<ComponentView>(
              posX = 223,
              posY = 0,
              width = 222,
              height = 300,
              visual = ColorVisual(Color(0x0f141f)))
          .apply { add(paneLabel) }

  val purpleLabel = Label(width = 75, height = 75, visual = ColorVisual(Color(0xbb6dff)))

  val redLabel = Label(width = 75, height = 75, visual = ColorVisual(Color(0xef4444)))

  val orangeLabel = Label(width = 75, height = 75, visual = ColorVisual(Color(0xfa6c56)))

  val yellowLabel = Label(width = 75, height = 75, visual = ColorVisual(Color(0xffc656)))

  val gridPane =
      GridPane<ComponentView>(
              posX = 100,
              posY = 27.5,
              layoutFromCenter = false,
              rows = 3,
              columns = 3,
              spacing = 10)
          .apply {
            this[0, 0] = purpleLabel
            this[1, 1] = redLabel
            this[2, 1] = orangeLabel
            this[2, 2] = yellowLabel
          }

  val panLabel =
      Label(
          posX = 300,
          posY = 410,
          width = 400,
          height = 200,
          text = "Drag to pan the camera. Scroll to zoom.",
          alignment = Alignment.CENTER,
          font = Font(20.0, Color.WHITE))

  val targetLayout =
      Pane<ComponentView>(
              width = 1000, height = 1000, visual = ImageVisual("assets/elements/background.png"))
          .apply { add(panLabel) }
  val cameraPane =
      CameraPane(width = width, height = height, target = targetLayout, limitBounds = true).apply {
        interactive = true
      }

  val absoluteToken =
      TokenView(
          posX = 75,
          posY = 112,
          width = 75,
          height = 75,
          visual = ColorVisual(Color(0x6dbeff)).apply { style.borderRadius = BorderRadius.FULL })

  val areaToken =
      TokenView(
          posX = 75, posY = 112, width = 75, height = 75, visual = ColorVisual(Color(0xbb6dff)))

  val area =
      Area<GameComponentView>(
              posX = 223,
              posY = 0,
              width = 222,
              height = 300,
              visual = ColorVisual(Color(0x0f141f)))
          .apply { add(areaToken) }

  val redCard =
      CardView(width = 100, height = 160, front = ColorVisual(Color(0xef4444))).apply {
        rotation = -6.0
      }

  val orangeCard =
      CardView(width = 100, height = 160, front = ColorVisual(Color(0xfa6c56))).apply {
        rotation = 8.0
      }

  val yellowCard =
      CardView(width = 100, height = 160, front = ColorVisual(Color(0xffc656))).apply {
        rotation = -1.0
      }

  val cardStack =
      CardStack<CardView>(
              posX = centerX(100),
              posY = centerY(160),
              width = 100,
              height = 160,
              alignment = Alignment.CENTER)
          .apply {
            add(redCard)
            add(orangeCard)
            add(yellowCard)
          }

  val greenCard = CardView(width = 100, height = 160, front = ColorVisual(Color(0xc6ff6e)))

  val blueCard = CardView(width = 100, height = 160, front = ColorVisual(Color(0x6dbeff)))

  val purpleCard = CardView(width = 100, height = 160, front = ColorVisual(Color(0xbb6dff)))

  val linearLayout =
      LinearLayout<GameComponentView>(
              posX = centerX(200),
              posY = centerY(160),
              width = 200,
              height = 160,
              orientation = Orientation.HORIZONTAL,
              alignment = Alignment.CENTER,
              spacing = 20)
          .apply {
            add(greenCard)
            add(blueCard)
            add(purpleCard)
          }

  val hiddenToken =
      TokenView(
          width = 50,
          height = 50,
          visual = ColorVisual(Color(0x6dbeff)).apply { style.borderRadius = BorderRadius.FULL })

  val satchel =
      Satchel<TokenView>(
              posX = centerX(100),
              posY = centerY(100),
              width = 100,
              height = 100,
              visual = ColorVisual(Color(0x0f141f)))
          .apply { add(hiddenToken) }

  val offsetHexagonGrid =
      HexagonGrid<HexagonView>(
              posX = 0, posY = 0, coordinateSystem = HexagonGrid.CoordinateSystem.OFFSET)
          .apply {
            for (x in 0..3) {
              for (y in 0..4) {
                val hexagon =
                    HexagonView(
                        visual =
                            CompoundVisual(
                                ColorVisual(Color(0xfa6c56)),
                                TextVisual("$x, $y", font = Font(10.0, Color(0x0f141f)))),
                        size = 30)
                this[x, y] = hexagon
              }
            }
          }

  val axialHexagonGrid =
      HexagonGrid<HexagonView>(
              posX = 0, posY = 0, coordinateSystem = HexagonGrid.CoordinateSystem.AXIAL)
          .apply {
            for (row in -2..2) {
              for (col in -2..2) {
                if (row + col in -2..2) {
                  val hexagon =
                      HexagonView(
                          visual =
                              CompoundVisual(
                                  ColorVisual(Color(0xc6ff6e)),
                                  TextVisual("$col, $row", font = Font(10.0, Color(0x0f141f)))),
                          size = 30)
                  this[col, row] = hexagon
                }
              }
            }
          }

  val offsetTargetLayout =
      Pane<ComponentView>(width = 1000, height = 1000).apply {
        add(offsetHexagonGrid)

        width = offsetHexagonGrid.actualWidth
        height = offsetHexagonGrid.actualHeight
      }
  val offsetCameraPane =
      CameraPane(width = width, height = height, target = offsetTargetLayout, limitBounds = false)
          .apply { interactive = true }

  val axialTargetLayout =
      Pane<ComponentView>(width = 1000, height = 1000).apply {
        add(axialHexagonGrid)

        width = axialHexagonGrid.actualWidth
        height = axialHexagonGrid.actualHeight
      }

  val axialCameraPane =
      CameraPane(width = width, height = height, target = axialTargetLayout, limitBounds = false)
          .apply { interactive = true }

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
    setUIComponentsAndSerialize("paneExample", ::absoluteLabel, ::pane)
    setUIComponentsAndSerialize("gridPaneExample", ::gridPane)
    setUIComponentsAndSerialize("cameraPaneExample", ::cameraPane)
    setUIComponentsAndSerialize("areaExample", ::absoluteToken, ::area)
    setUIComponentsAndSerialize("cardStackExample", ::cardStack)
    setUIComponentsAndSerialize("linearLayoutExample", ::linearLayout)
    setUIComponentsAndSerialize("satchelExample", ::satchel)
    setUIComponentsAndSerialize("offsetHexagonGridExample", ::offsetCameraPane)
    setUIComponentsAndSerialize("axialHexagonGridExample", ::axialCameraPane)
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
    _linearLayout.clear()

    props.forEach { prop ->
      val comp = prop.get()
      _linearLayout.add(comp as GameComponentView)
    }

    addComponents(_linearLayout)

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
