/*
 *    Copyright 2021 The BoardGameWork Authors
 *    SPDX-License-Identifier: Apache-2.0
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

@file:Suppress("unused")

package tools.aqua.bgw.components.dsl

import tools.aqua.bgw.components.StaticComponentView
import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.components.uicomponents.*
import tools.aqua.bgw.core.MenuScene
import java.awt.Color

//region Util
/**
 * MenuSceneBuilder is a static class, that can be used to build [MenuScene]s.
 * It is meant to be used in conjunction with the internal
 * domain specific language in [tools.aqua.bgw.components.dsl].
 *
 * To initiate a new description of a menu scene, simply call the `dsl` function,
 * which then returns the resulting [MenuScene].
 */
class MenuSceneBuilder {
    companion object {
        /**
         * Initiates a new description of a menu scene.
         *
         * @return the resulting [MenuScene]
         */
        fun dsl(height: Number, width: Number, func: MenuScene.() -> Unit): MenuScene =
            MenuScene(height = height, width = width).apply(func)
    }
}

/**
 * Sets the componentStyle of the receiver [UIComponent] to the return value of [func].
 */
fun UIComponent.componentStyle(func: () -> String) {
    componentStyle = func.invoke()
}

/**
 * Sets the backgroundStyle of the receiver [UIComponent] to the return value of [func].
 */
fun UIComponent.backgroundStyle(func: () -> String) {
    backgroundStyle = func.invoke()
}
//endregion


//region Grid
/**
 * Creates a new [GridPane], applies the [func] and adds it to the receiver [MenuScene].
 *
 * @return the new [GridPane].
 */
fun <T> MenuScene.grid(
    rows: Int,
    cols: Int,
    func: (GridPane<T>.() -> Unit)
): GridPane<T> where T : StaticComponentView<T> {
    val gridLayoutView = GridPane<T>(rows, cols).apply(func)
    addComponents(gridLayoutView)
    return gridLayoutView
}
//endregion

//region ToggleGroup
/**
 * Creates a new [ToggleGroup] and sets it for all [ToggleButton]s that get created in the context of the
 * receiver [ToggleGroupBuilder] of [func]. Also adds all those [ToggleButton]s to the receiver [MenuScene].
 *
 * @see [ToggleGroupBuilder]
 *
 * @return the new [ToggleGroup].
 */
fun MenuScene.toggleGroup(func: ToggleGroupBuilder.() -> Unit): ToggleGroup {
    val tgb = ToggleGroupBuilder().apply(func)
    val data = tgb.build()
    data.second.forEach { addComponents(it) }
    return data.first
}

/**
 * [ToggleGroupBuilder] may be used to build [ToggleGroup]s in a semantically more appealing way, than
 * to set the [ToggleGroup] on every [ToggleButton].
 */
class ToggleGroupBuilder {
    internal val buttons = mutableListOf<ToggleButton>()
    internal fun build(): Pair<ToggleGroup, List<ToggleButton>> {
        val toggleGroup = ToggleGroup()
        buttons.forEach { it.toggleGroup = toggleGroup }
        return Pair(toggleGroup, buttons)
    }
}

/**
 * Creates a new [ToggleButton], applies the [func] and adds it to the context of the receiver [ToggleGroupBuilder].
 *
 * @return the new [ToggleButton].
 */
fun ToggleGroupBuilder.toggleButton(func: ToggleButton.() -> Unit): ToggleButton {
    val toggleButton = ToggleButton().apply(func)
    buttons.add(toggleButton)
    return toggleButton
}

/**
 * Creates a new [RadioButton], applies the [func] and adds it to the context of the receiver [ToggleGroupBuilder].
 *
 * @return the new [RadioButton].
 */
fun ToggleGroupBuilder.radioButton(func: RadioButton.() -> Unit): RadioButton {
    val radioButton = RadioButton().apply(func)
    buttons.add(radioButton)
    return radioButton
}
//endregion

//region Button
/**
 * Creates a new [Button], applies the [func] and adds it to the receiver [MenuScene].
 *
 * @return the new [Button].
 */
fun MenuScene.button(func: Button.() -> Unit): Button {
    val btn = Button().apply(func)
    addComponents(btn)
    return btn
}
//endregion

//region ToggleButton
/**
 * Creates a new [ToggleButton], applies the [func] and adds it to the receiver [MenuScene].
 *
 * @return the new [ToggleButton].
 */
fun MenuScene.toggleButton(func: ToggleButton.() -> Unit): ToggleButton {
    val btn = ToggleButton().apply(func)
    addComponents(btn)
    return btn
}
//endregion

//region RadioButton
/**
 * Creates a new [RadioButton], applies the [func] and adds it to the receiver [MenuScene].
 *
 * @return the new [RadioButton].
 */
fun MenuScene.radioButton(func: ToggleButton.() -> Unit): RadioButton {
    val btn = RadioButton().apply(func)
    addComponents(btn)
    return btn
}
//endregion

//region CheckBox
/**
 * Creates a new [CheckBox], applies the [func] and adds it to the receiver [MenuScene].
 *
 * @return the new [CheckBox].
 */
fun MenuScene.checkBox(func: CheckBox.() -> Unit): CheckBox {
    val checkBox = CheckBox().apply(func)
    addComponents(checkBox)
    return checkBox
}
//endregion

//region Label
/**
 * Creates a new [Label], applies the [func] and adds it to the receiver [MenuScene].
 *
 * @return the new [Label].
 */
fun MenuScene.label(func: Label.() -> Unit): Label {
    val label = Label().apply(func)
    addComponents(label)
    return label
}
//endregion

//region ListView
/**
 * Creates a new [ListView], applies the [func] and adds it to the receiver [MenuScene].
 *
 * @return the new [ListView].
 */
fun <T> MenuScene.listView(func: ListView<T>.() -> Unit): ListView<T> {
    val listView = ListView<T>()
    addComponents(listView.apply(func))
    return listView
}
//endregion

//region TableView
/**
 * Creates a new [TableView], applies the [func] and adds it to the receiver [MenuScene].
 *
 * @return the new [TableView].
 */
fun <T> MenuScene.tableView(func: TableView<T>.() -> Unit): TableView<T> {
    val tableView = TableView<T>()
    addComponents(tableView.apply(func))
    return tableView
}

/**
 * Creates a new [TableColumn] and adds it to the receiver [TableView].
 *
 * @param title the title for the new [TableColumn].
 * @param width the width for the new [TableColumn].
 * @param func the new formatFunction for the new [TableColumn].
 */
fun <T> TableView<T>.column(title: String, width: Number, func: T.() -> String) {
    columns.add(TableColumn(title = title, width = width, formatFunction = func))
}

/**
 * Sets the data model for the receiver [TableView].
 *
 * @param data the data to set.
 */
fun <T> TableView<T>.data(data: List<T>) {
    items.clear()
    items.addAll(data)
}

/**
 * Appends the supplied [data] to the data model of the receiver [TableView].
 *
 * @param data the data to append.
 */
fun <T> TableView<T>.appendData(data: List<T>) {
    items.addAll(data)
}
//endregion

//region TextArea
/**
 * Creates a new [TextArea], applies the [func] and adds it to the receiver [MenuScene].
 *
 * @param prompt the prompt for the new [TextArea].
 * @return the new [TextArea].
 */
fun MenuScene.textArea(prompt: String, func: TextArea.() -> Unit): TextArea {
    val textArea = TextArea(prompt = prompt).apply(func)
    addComponents(textArea)
    return textArea
}
//endregion

//region TextField
/**
 * Creates a new [TextField], applies the [func] and adds it to the receiver [MenuScene].
 *
 * @param prompt the prompt for the new [TextField].
 * @return the new [TextField].
 */
fun MenuScene.textField(prompt: String, func: TextField.() -> Unit): TextField {
    val textField = TextField(prompt = prompt).apply(func)
    addComponents(textField)
    return textField
}
//endregion

//region ProgressBar
/**
 * Creates a new [ProgressBar], applies the [func] and adds it to the receiver [MenuScene].
 *
 * @return the new [ProgressBar].
 */
fun MenuScene.progressBar(func: ProgressBar.() -> Unit): ProgressBar {
    val progressBar = ProgressBar().apply(func)
    addComponents(progressBar)
    return progressBar
}
//endregion

//region ComboBox
/**
 * Creates a new [ComboBox], applies the [func] and adds it to the receiver [MenuScene].
 *
 * @param prompt the prompt for the new [ComboBox].
 * @return the new [ComboBox].
 */
fun <T> MenuScene.comboBox(prompt: String, func: ComboBox<T>.() -> Unit): ComboBox<T> {
    val comboBox = ComboBox<T>(prompt = prompt).apply(func)
    addComponents(comboBox)
    return comboBox
}
//endregion

//region ColorPicker
/**
 * Creates a new [ColorPicker], applies the [func] and adds it to the receiver [MenuScene].
 *
 * @param initialColor the initial [Color] for the new [ColorPicker].
 * @return the new [ColorPicker].
 */
fun MenuScene.colorPicker(initialColor : Color, func: ColorPicker.() -> Unit): ColorPicker {
    val colorPicker = ColorPicker(initialColor = initialColor).apply(func)
    addComponents(colorPicker)
    return colorPicker
}
//endregion