/*
 * Copyright 2026 The BoardGameWork Authors
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

package tools.aqua.bgw.playground

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.container.Area
import tools.aqua.bgw.components.container.CardStack
import tools.aqua.bgw.components.container.HexagonGrid
import tools.aqua.bgw.components.container.LinearLayout
import tools.aqua.bgw.components.container.Satchel
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.gamecomponentviews.DiceView
import tools.aqua.bgw.components.gamecomponentviews.HexagonView
import tools.aqua.bgw.components.gamecomponentviews.TokenView
import tools.aqua.bgw.components.layoutviews.CameraPane
import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.CheckBox
import tools.aqua.bgw.components.uicomponents.ColorPicker
import tools.aqua.bgw.components.uicomponents.ComboBox
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.ListView
import tools.aqua.bgw.components.uicomponents.Orientation
import tools.aqua.bgw.components.uicomponents.PasswordField
import tools.aqua.bgw.components.uicomponents.ProgressBar
import tools.aqua.bgw.components.uicomponents.RadioButton
import tools.aqua.bgw.components.uicomponents.SelectionMode
import tools.aqua.bgw.components.uicomponents.TableColumn
import tools.aqua.bgw.components.uicomponents.TableView
import tools.aqua.bgw.components.uicomponents.TextArea
import tools.aqua.bgw.components.uicomponents.TextField
import tools.aqua.bgw.components.uicomponents.ToggleButton
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.core.HexOrientation
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.CompoundVisual
import tools.aqua.bgw.visual.TextVisual
import tools.aqua.bgw.visual.Visual

private fun story(
    name: String,
    group: String,
    description: String,
    create: () -> ComponentView,
    vararg actions: StoryAction,
) = ComponentStory(name, group, description, create, actions.toList())

private fun action(title: String, run: (ComponentView) -> Unit) = StoryAction(title, run)

internal val componentStories: List<ComponentStory> =
    listOf(
        story(
            "Label",
            "UI",
            "Text, font, wrapping, alignment and background visual.",
            {
              Label(
                  width = 260,
                  height = 90,
                  text = "A live BGW label",
                  visual = ColorVisual.LIGHT_GRAY)
            },
            action("Change text") { (it as Label).text += "!" }),
        story(
            "Button",
            "UI",
            "Click handling, disabled state, focus and labeled rendering.",
            {
              Button(
                  width = 260,
                  height = 90,
                  text = "Click or mutate me",
                  visual = ColorVisual.LIGHT_GRAY)
            },
            action("Toggle disabled") { it.isDisabled = !it.isDisabled }),
        story(
            "CheckBox",
            "UI",
            "Checked and indeterminate states plus user interaction.",
            {
              CheckBox(
                  width = 260, height = 70, text = "Tri-state option", allowIndeterminate = true)
            },
            action("Toggle checked") { (it as CheckBox).isChecked = !it.isChecked },
            action("Indeterminate") { (it as CheckBox).isIndeterminate = !it.isIndeterminate }),
        story(
            "ColorPicker",
            "UI",
            "Native color input and selected-color updates.",
            { ColorPicker(width = 260, height = 70, initialColor = Color.CYAN) },
            action("Select red") { (it as ColorPicker).selectedColor = Color.RED }),
        story(
            "ComboBox",
            "UI",
            "Item replacement, formatting, prompting and selection.",
            {
              ComboBox(
                  width = 300,
                  height = 70,
                  prompt = "Pick a value",
                  items = listOf("Alpha", "Beta", "Gamma"))
            },
            action("Select next") {
              val combo = it as ComboBox<String>
              val index = combo.items.indexOf(combo.selectedItem)
              combo.selectedItem = combo.items[(index + 1).mod(combo.items.size)]
            },
            action("Replace items") {
              (it as ComboBox<String>).items = listOf("One", "Two", "Three", "Four")
            }),
        story(
            "ListView",
            "UI",
            "Structured-data rendering, orientation and multi-selection.",
            {
              ListView(
                  width = 320,
                  height = 220,
                  items = listOf("Mercury", "Venus", "Earth", "Mars"),
                  selectionMode = SelectionMode.MULTIPLE)
            },
            action("Orientation") {
              val list = it as ListView<String>
              list.orientation =
                  if (list.orientation == Orientation.VERTICAL) Orientation.HORIZONTAL
                  else Orientation.VERTICAL
            }),
        story(
            "PasswordField",
            "UI",
            "Masked text input, prompt and programmatic value updates.",
            { PasswordField(width = 300, height = 70, text = "secret", prompt = "Password") },
            action("Change value") { (it as PasswordField).text = "changed-${it.text.length}" }),
        story(
            "ProgressBar",
            "UI",
            "Progress clamping and live bar rendering.",
            {
              ProgressBar(
                  width = 360,
                  height = 60,
                  progress = 0.25,
                  visual = ColorVisual.LIGHT_GRAY,
                  barColor = Color.CYAN)
            },
            action("Advance") {
              val bar = it as ProgressBar
              bar.progress = (bar.progress + 0.25) % 1.25
            }),
        story(
            "RadioButton",
            "UI",
            "Binary selection rendering and toggle-group behavior.",
            { RadioButton(width = 260, height = 70, text = "Radio option") },
            action("Toggle selected") { (it as RadioButton).isSelected = !it.isSelected }),
        story(
            "TableView",
            "UI",
            "Columns, formatted cells, items and structured selection.",
            {
              TableView(
                  width = 430,
                  height = 230,
                  columns =
                      listOf(
                          TableColumn<Pair<String, Int>>("Name", 250) { row -> row.first },
                          TableColumn<Pair<String, Int>>("Value", 150) { row ->
                            row.second.toString()
                          }),
                  items = listOf("Alpha" to 1, "Beta" to 2, "Gamma" to 3))
            }),
        story(
            "TextArea",
            "UI",
            "Multiline editable text and prompt rendering.",
            {
              TextArea(
                  width = 360, height = 190, text = "Line one\nLine two", prompt = "Enter notes")
            },
            action("Append line") { (it as TextArea).text += "\nAnother line" }),
        story(
            "TextField",
            "UI",
            "Single-line editable text and prompt rendering.",
            { TextField(width = 340, height = 70, text = "Editable", prompt = "Enter text") },
            action("Change value") { (it as TextField).text = "Updated by playground" }),
        story(
            "ToggleButton",
            "UI",
            "Selected and unselected binary-button states.",
            {
              ToggleButton(
                  width = 280, height = 80, text = "Toggle me", visual = ColorVisual.LIGHT_GRAY)
            },
            action("Toggle selected") { (it as ToggleButton).isSelected = !it.isSelected }),
        story(
            "TokenView",
            "Game component",
            "Generic movable game token with arbitrary visuals.",
            { TokenView(width = 150, height = 150, visual = ColorVisual.GREEN) }),
        story(
            "CardView",
            "Game component",
            "Independent front/back visuals and card flipping.",
            {
              CardView(
                  width = 160,
                  height = 230,
                  front = CompoundVisual(ColorVisual.WHITE, TextVisual("FRONT")),
                  back = CompoundVisual(ColorVisual.BLUE, TextVisual("BACK")))
            },
            action("Flip") { (it as CardView).flip() }),
        story(
            "DiceView",
            "Game component",
            "Multiple side visuals and current-side updates.",
            {
              DiceView(
                  width = 160,
                  height = 160,
                  visuals =
                      (1..6).map { side ->
                        CompoundVisual(ColorVisual.WHITE, TextVisual(side.toString()))
                      })
            },
            action("Next side") {
              val die = it as DiceView
              die.currentSide = (die.currentSide + 1) % 6
            }),
        story(
            "HexagonView",
            "Game component",
            "Pointy/flat orientation and hexagonal clipping.",
            { HexagonView(size = 100, visual = ColorVisual.ORANGE) },
            action("Orientation") {
              val hex = it as HexagonView
              hex.orientation =
                  if (hex.orientation == HexOrientation.POINTY_TOP) HexOrientation.FLAT_TOP
                  else HexOrientation.POINTY_TOP
            }),
        story(
            "Pane",
            "Layout",
            "Free-positioned child hierarchy and add/remove ordering.",
            {
              Pane<ComponentView>(width = 360, height = 260, visual = ColorVisual.LIGHT_GRAY)
                  .apply {
                    add(
                        Label(
                            posX = 25,
                            posY = 25,
                            width = 170,
                            height = 55,
                            text = "Pane child",
                            visual = ColorVisual.WHITE))
                  }
            },
            action("Add child") {
              val pane = it as Pane<ComponentView>
              pane.add(
                  Label(
                      posX = 30 + pane.components.size * 35,
                      posY = 110,
                      width = 120,
                      height = 45,
                      text = "#${pane.components.size}"))
            },
            action("Remove child") {
              val pane = it as Pane<ComponentView>
              pane.components.lastOrNull()?.let(pane::remove)
            }),
        story(
            "GridPane",
            "Layout",
            "Sparse cells, row/column sizing and cell replacement.",
            {
              GridPane<ComponentView>(
                      columns = 3, rows = 3, spacing = 8, visual = ColorVisual.LIGHT_GRAY)
                  .apply {
                    setColumnWidths(120)
                    setRowHeights(75)
                    this[0, 0] =
                        Label(width = 100, height = 55, text = "0,0", visual = ColorVisual.WHITE)
                    this[1, 1] =
                        Label(width = 100, height = 55, text = "1,1", visual = ColorVisual.CYAN)
                  }
            },
            action("Toggle cell") {
              val grid = it as GridPane<ComponentView>
              grid[2, 2] =
                  if (grid[2, 2] == null) Label(width = 100, height = 55, text = "2,2") else null
            }),
        story(
            "CameraPane",
            "Layout",
            "Viewport target, programmatic zoom and interactive pan/zoom.",
            {
              val target =
                  Pane<ComponentView>(width = 600, height = 420, visual = Visual.EMPTY).apply {
                    add(
                        TokenView(
                            posX = 250,
                            posY = 150,
                            width = 100,
                            height = 100,
                            visual = ColorVisual.RED))
                  }
              CameraPane(width = 400, height = 280, target = target, visual = ColorVisual.WHITE)
            },
            action("Zoom") {
              val camera = it as CameraPane<*>
              camera.zoom = if (camera.zoom < 1.5) 2.0 else 1.0
            },
            action("Interactive") {
              val camera = it as CameraPane<*>
              camera.interactive = !camera.interactive
            }),
        story(
            "Area",
            "Container",
            "Free-positioned game components with lifecycle operations.",
            {
              Area<TokenView>(width = 380, height = 260, visual = ColorVisual.LIGHT_GRAY).apply {
                add(
                    TokenView(
                        posX = 30, posY = 35, width = 90, height = 90, visual = ColorVisual.RED))
              }
            },
            action("Add token") {
              val area = it as Area<TokenView>
              area.add(
                  TokenView(
                      posX = 45 + area.components.size * 55,
                      posY = 130,
                      width = 70,
                      height = 70,
                      visual = ColorVisual.BLUE))
            },
            action("Remove token") {
              val area = it as Area<TokenView>
              area.components.lastOrNull()?.let(area::remove)
            }),
        story(
            "CardStack",
            "Container",
            "Card push/pop, top ordering and alignment.",
            {
              CardStack<CardView>(width = 190, height = 260, visual = ColorVisual.LIGHT_GRAY)
                  .apply {
                    repeat(3) { index ->
                      push(
                          CardView(
                              width = 150,
                              height = 220,
                              front = CompoundVisual(ColorVisual.WHITE, TextVisual("Card $index")),
                              back = ColorVisual.BLUE))
                    }
                  }
            },
            action("Push") {
              val stack = it as CardStack<CardView>
              stack.push(
                  CardView(
                      width = 150,
                      height = 220,
                      front = ColorVisual.ORANGE,
                      back = ColorVisual.BLUE))
            },
            action("Pop") { (it as CardStack<CardView>).popOrNull() }),
        story(
            "LinearLayout",
            "Container",
            "Automatic spacing, orientation, alignment and overflow fitting.",
            {
              LinearLayout<TokenView>(
                      width = 430, height = 220, spacing = 20, visual = ColorVisual.LIGHT_GRAY)
                  .apply {
                    addAll(
                        (1..4).map {
                          TokenView(width = 70, height = 70, visual = ColorVisual(40 * it, 90, 180))
                        })
                  }
            },
            action("Orientation") {
              val layout = it as LinearLayout<TokenView>
              layout.orientation =
                  if (layout.orientation == Orientation.HORIZONTAL) Orientation.VERTICAL
                  else Orientation.HORIZONTAL
            },
            action("Spacing") {
              val layout = it as LinearLayout<TokenView>
              layout.spacing = if (layout.spacing < 30) 60.0 else 10.0
            }),
        story(
            "Satchel",
            "Container",
            "Hidden pooled game components and restored state on removal.",
            {
              Satchel<TokenView>(
                      width = 180,
                      height = 180,
                      visual = CompoundVisual(ColorVisual.ORANGE, TextVisual("Satchel")))
                  .apply {
                    addAll(
                        (1..4).map {
                          TokenView(width = 70, height = 70, visual = ColorVisual.GREEN)
                        })
                  }
            },
            action("Draw") {
              val satchel = it as Satchel<TokenView>
              satchel.components.lastOrNull()?.let(satchel::remove)
            },
            action("Add token") {
              (it as Satchel<TokenView>).add(
                  TokenView(width = 70, height = 70, visual = ColorVisual.GREEN))
            }),
        story(
            "HexagonGrid",
            "Container",
            "Hex coordinates, replacement/removal and orientation layout.",
            {
              HexagonGrid<HexagonView>(visual = ColorVisual.LIGHT_GRAY).apply {
                for (column in -1..1) for (row in -1..1) this[column, row] =
                    HexagonView(size = 48, visual = ColorVisual(80 + 40 * (column + 1), 140, 190))
              }
            },
            action("Toggle center") {
              val grid = it as HexagonGrid<HexagonView>
              if (grid[0, 0] == null) grid[0, 0] = HexagonView(size = 48, visual = ColorVisual.RED)
              else grid.remove(0, 0)
            },
            action("Orientation") {
              val grid = it as HexagonGrid<HexagonView>
              grid.orientation =
                  if (grid.orientation == HexOrientation.POINTY_TOP) HexOrientation.FLAT_TOP
                  else HexOrientation.POINTY_TOP
            }),
    )
