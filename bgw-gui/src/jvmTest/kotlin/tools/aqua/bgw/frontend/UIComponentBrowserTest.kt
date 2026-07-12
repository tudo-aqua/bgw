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

package tools.aqua.bgw.frontend

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.openqa.selenium.Keys
import org.openqa.selenium.support.ui.Select
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.CheckBox
import tools.aqua.bgw.components.uicomponents.ColorPicker
import tools.aqua.bgw.components.uicomponents.ComboBox
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
import tools.aqua.bgw.components.uicomponents.ToggleGroup
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.Color

class UIComponentBrowserTest : BrowserTestBase() {

  @Test
  fun textFieldSynchronizesBrowserAndJvmInBothDirections() {
    val field = TextField(width = 300, height = 60, text = "initial", prompt = "type here")
    show(BoardGameScene(width = 800, height = 600).apply { addComponents(field) })
    val input = tester.awaitBGWComp(field.id).descendant("input")

    input.clear()
    input.sendKeys("browser")
    tester.await("text field browser input on JVM") { field.text == "browser" }
    input.sendKeys(Keys.TAB)

    field.text = "from JVM"
    tester.await("text field JVM update in DOM") {
      getBGWComp(field.id).descendant("input").getAttribute("value") == "from JVM"
    }
  }

  @Test
  fun textAreaSynchronizesMultilineInput() {
    val area = TextArea(width = 320, height = 160, text = "one", prompt = "notes")
    show(BoardGameScene(width = 800, height = 600).apply { addComponents(area) })
    val input = tester.awaitBGWComp(area.id).descendant("textarea")

    input.clear()
    input.sendKeys("line one\nline two")
    tester.await("text area browser input on JVM") { area.text.contains("line two") }

    area.isReadonly = true
    tester.await("text area readonly state") {
      getBGWComp(area.id).descendant("textarea").getAttribute("readonly") != null
    }
  }

  @Test
  fun passwordFieldMasksAndSynchronizesItsValue() {
    val field = PasswordField(width = 300, height = 60, text = "secret")
    show(BoardGameScene(width = 800, height = 600).apply { addComponents(field) })
    val input = tester.awaitBGWComp(field.id).descendant("input")
    assertEquals("password", input.getAttribute("type"))

    input.clear()
    input.sendKeys("new-secret")
    tester.await("password input on JVM") { field.text == "new-secret" }
    assertFalse(tester.getBGWComp(field.id).text.contains("new-secret"))
  }

  @Test
  fun checkBoxSupportsClickCheckedIndeterminateAndDisabledStates() {
    val checkBox = CheckBox(width = 260, height = 60, text = "option", allowIndeterminate = true)
    show(BoardGameScene(width = 800, height = 600).apply { addComponents(checkBox) })
    val input = tester.awaitBGWComp(checkBox.id).descendant("input")

    tester.getBGWComp(checkBox.id).descendant("label").click()
    tester.await("checkbox first tri-state click on JVM") { checkBox.isIndeterminate }
    tester.getBGWComp(checkBox.id).descendant("label").click()
    tester.await("checkbox second tri-state click on JVM") { checkBox.isChecked }
    tester.await("checkbox checked state in DOM") {
      getBGWComp(checkBox.id).descendant("input").isSelected
    }

    checkBox.isChecked = false
    checkBox.isIndeterminate = true
    tester.await("checkbox indeterminate DOM property") {
      requireDriver().let { driver ->
        (driver as org.openqa.selenium.JavascriptExecutor).executeScript(
            "return arguments[0].indeterminate", getBGWComp(checkBox.id).descendant("input")) ==
            true
      }
    }

    checkBox.isDisabled = true
    tester.await("checkbox disabled state") {
      !getBGWComp(checkBox.id).descendant("input").isEnabled
    }
  }

  @Test
  fun radioButtonsEnforceToggleGroupSelectionFromBrowserClicks() {
    val group = ToggleGroup()
    val first =
        RadioButton(posY = 20, width = 220, height = 50, text = "first", toggleGroup = group)
    val second =
        RadioButton(posY = 90, width = 220, height = 50, text = "second", toggleGroup = group)
    show(BoardGameScene(width = 800, height = 600).apply { addComponents(first, second) })

    tester.awaitBGWComp(first.id).descendant("input").click()
    tester.await("first radio selection") { first.isSelected && !second.isSelected }
    tester.getBGWComp(second.id).descendant("input").click()
    tester.await("exclusive second radio selection") { !first.isSelected && second.isSelected }
  }

  @Test
  fun toggleButtonSynchronizesSelectedState() {
    val toggle = ToggleButton(width = 240, height = 60, text = "toggle")
    show(BoardGameScene(width = 800, height = 600).apply { addComponents(toggle) })

    tester.awaitBGWComp(toggle.id).descendant("input").click()
    tester.await("toggle click on JVM") { toggle.isSelected }
    toggle.isSelected = false
    tester.await("toggle JVM update in DOM") {
      !getBGWComp(toggle.id).descendant("input").isSelected
    }
  }

  @Test
  fun comboBoxSynchronizesSelectionAndItemReplacement() {
    val combo = ComboBox(width = 300, height = 60, prompt = "choose", items = listOf("A", "B", "C"))
    show(BoardGameScene(width = 800, height = 600).apply { addComponents(combo) })
    val select = Select(tester.awaitBGWComp(combo.id).descendant("select"))

    select.selectByValue("1")
    tester.await("combo selection on JVM") { combo.selectedItem == "B" }

    combo.items = listOf("X", "Y")
    combo.selectedItem = "Y"
    tester.await("combo replacement in DOM") {
      val current = Select(getBGWComp(combo.id).descendant("select"))
      current.options.size == 3 && current.firstSelectedOption.getAttribute("value") == "1"
    }
  }

  @Test
  fun listViewRendersItemsHandlesSelectionAndChangesOrientation() {
    val list =
        ListView(
            width = 320,
            height = 220,
            items = listOf("Alpha", "Beta", "Gamma"),
            selectionMode = SelectionMode.MULTIPLE)
    show(BoardGameScene(width = 800, height = 600).apply { addComponents(list) })
    val rendered = tester.awaitBGWComp(list.id)
    assertEquals(3, rendered.descendants("bgw_contents > bgw_text").size)

    rendered.descendants("bgw_contents > bgw_text")[1].click()
    tester.await("list selection on JVM") { list.selectedItems.toList() == listOf("Beta") }

    list.orientation = Orientation.HORIZONTAL
    list.items.setAll(listOf("One", "Two", "Three", "Four"))
    tester.await("list item and orientation update") {
      val component = getBGWComp(list.id)
      component.descendants("bgw_contents > bgw_text").size == 4 &&
          component.descendant("bgw_contents").getCssValue("flex-direction") == "row"
    }
  }

  @Test
  fun tableViewRendersColumnsRowsAndSelectionUpdates() {
    val table =
        TableView(
            width = 420,
            height = 220,
            columns =
                listOf(
                    TableColumn<Pair<String, Int>>("Name", 200) { it.first },
                    TableColumn<Pair<String, Int>>("Score", 160) { it.second.toString() }),
            items = listOf("Ada" to 10, "Grace" to 20))
    show(BoardGameScene(width = 800, height = 600).apply { addComponents(table) })
    val rendered = tester.awaitBGWComp(table.id)
    assertEquals(2, rendered.descendants("bgw_scroll > bgw_contents").size)
    assertTrue(rendered.text.contains("Grace"))

    rendered
        .descendants("bgw_scroll > bgw_contents")[0]
        .findElements(org.openqa.selenium.By.tagName("bgw_text"))[2]
        .click()
    tester.await("table selection on JVM") { table.selectedItems.toList() == listOf("Grace" to 20) }

    table.items.setAll(listOf("Linus" to 30))
    table.columns[0].title = "Person"
    tester.await("table rows and title update") {
      val text = getBGWComp(table.id).text
      text.contains("Person") && text.contains("Linus") && !text.contains("Grace")
    }
  }

  @Test
  fun progressBarTracksProgressAndBarColor() {
    val progress = ProgressBar(width = 400, height = 50, progress = 0.25, barColor = Color.CYAN)
    show(BoardGameScene(width = 800, height = 600).apply { addComponents(progress) })
    val barId = "${progress.id}--bar"
    tester.awaitBGWComp(progress.id)
    tester.await("initial progress width") {
      val component = getBGWComp(progress.id)
      val bar = getBGWComp(barId)
      bar.css("width").removeSuffix("px").toDouble() /
          component.css("width").removeSuffix("px").toDouble() in 0.24..0.26
    }

    progress.progress = 0.75
    tester.await("updated progress width") {
      val component = getBGWComp(progress.id)
      val bar = getBGWComp(barId)
      bar.css("width").removeSuffix("px").toDouble() /
          component.css("width").removeSuffix("px").toDouble() in 0.74..0.76
    }
  }

  @Test
  fun colorPickerSynchronizesProgrammaticColor() {
    val picker = ColorPicker(width = 240, height = 60, initialColor = Color.RED)
    show(BoardGameScene(width = 800, height = 600).apply { addComponents(picker) })
    assertEquals(
        "#ff0000", tester.awaitBGWComp(picker.id).descendant("input").getAttribute("value"))

    picker.selectedColor = Color.BLUE
    tester.await("color picker JVM update") {
      getBGWComp(picker.id).descendant("input").getAttribute("value") == "#0000ff"
    }
  }

  @Test
  fun commonVisibilityDisableAndFocusPropertiesReachDom() {
    val button = Button(width = 220, height = 70, text = "stateful")
    show(BoardGameScene(width = 800, height = 600).apply { addComponents(button) })
    val rendered = tester.awaitBGWComp(button.id)
    assertEquals("0", rendered.attribute("tabindex"))

    button.isDisabled = true
    tester.await("disabled pointer and focus state") {
      val current = getBGWComp(button.id)
      current.attribute("tabindex") == "-1" && current.css("pointer-events") == "none"
    }
    button.isVisible = false
    tester.await("hidden display state") { getBGWComp(button.id).css("display") == "none" }
  }
}
