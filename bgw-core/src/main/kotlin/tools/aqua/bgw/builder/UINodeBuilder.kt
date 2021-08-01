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

package tools.aqua.bgw.builder

import com.jfoenix.controls.JFXComboBox
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.collections.FXCollections
import javafx.geometry.Pos
import javafx.scene.control.Labeled
import javafx.scene.control.ListCell
import javafx.scene.control.TableColumn
import javafx.scene.layout.Region
import tools.aqua.bgw.builder.FXConverters.Companion.toFXColor
import tools.aqua.bgw.builder.FXConverters.Companion.toFXFontCSS
import tools.aqua.bgw.builder.FXConverters.Companion.toJavaFXOrientation
import tools.aqua.bgw.components.uicomponents.*
import tools.aqua.bgw.observable.BooleanProperty
import tools.aqua.bgw.util.Font
import java.awt.Color

/**
 * UINodeBuilder.
 * Factory for all BGW UI components.
 */
internal class UINodeBuilder {
    companion object {
        /**
         * Switches between [UIComponent]s.
         */
        internal fun buildUIComponent(uiComponent: UIComponent): Region =
            when (uiComponent) {
                is Button ->
                    buildButton(uiComponent)
                is CheckBox ->
                    buildCheckBox(uiComponent)
                is ComboBox<*> ->
                    buildComboBox(uiComponent)
                is Label ->
                    buildLabel(uiComponent)
                is ListView<*> ->
                    buildListView(uiComponent)
                is Table<*> ->
                    buildTableView(uiComponent)
                is TextArea ->
                    buildTextArea(uiComponent)
                is TextField ->
                    buildTextField(uiComponent)
                is ToggleButton ->
                    buildToggleButton(uiComponent)
                is ColorPicker ->
                    buildColorPicker(uiComponent)
                is ProgressBar ->
                    buildProgressBar(uiComponent)
            }

        /**
         * Builds [Labeled]
         */
        private fun buildLabel(label: Label): Region {
            val node = javafx.scene.control.Label()
            node.alignment = Pos.CENTER
            node.textProperty().bindLabelProperty(label)
            node.bindFont(label)
            return node
        }

        /**
         * Builds [Button].
         */
        private fun buildButton(button: Button): Region {
            val node = com.jfoenix.controls.JFXButton()
            node.textProperty().bindLabelProperty(button)
            node.bindFont(button)
            return node
        }

        /**
         * Builds [TextArea].
         */
        private fun buildTextArea(textArea: TextArea): Region {
            val node = javafx.scene.control.TextArea(textArea.labelProperty.value)

            node.textProperty().bindLabelProperty(textArea)
            node.promptText = textArea.prompt
            textArea.fontProperty.setGUIListenerAndInvoke(textArea.font) { _, nV ->
                node.style = nV.toFXFontCSS()
            }
            return node
        }

        /**
         * Builds [TextField]
         */
        private fun buildTextField(textField: TextField): Region {
            val node = javafx.scene.control.TextField(textField.labelProperty.value)

            node.textProperty().bindLabelProperty(textField)
            node.promptText = textField.prompt
            textField.fontProperty.setGUIListenerAndInvoke(textField.font) { _, nV ->
                node.style = nV.toFXFontCSS()
            }
            return node
        }

        /**
         * Builds [ComboBox].
         */
        private fun <T> buildComboBox(comboBox: ComboBox<T>): Region {
            val node = JFXComboBox<T>()
            comboBox.observableItemsList.setGUIListenerAndInvoke {
                node.items.clear()
                comboBox.items.forEach { node.items.add(it) }
                node.setCellFactory(comboBox)
            }
            node.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
                comboBox.selectedItem = newValue
            }
            comboBox.selectedItemProperty.setInternalListenerAndInvoke(comboBox.selectedItem) { _, newValue ->
                if (newValue != null) {
                    node.selectionModel.select(newValue)
                }
            }
            node.promptText = comboBox.prompt
            //font
            comboBox.fontProperty.setGUIListenerAndInvoke(comboBox.font) { _, _ ->
                node.buttonCell.style = comboBox.font.toFXFontCSS()
                node.buttonCell.textFill = comboBox.font.color.toFXColor()
                node.setCellFactory(comboBox)
            }
            return node
        }

        /**
         * Sets [ComboBox] cell factory .
         */
        private fun <T> JFXComboBox<T>.setCellFactory(comboBox: ComboBox<T>) {
            cellFactory = javafx.util.Callback {
                object : ListCell<T>() {
                    override fun updateItem(item: T, empty: Boolean) {
                        super.updateItem(item, empty)
                        this.style = comboBox.font.toFXFontCSS()
                        this.textFill = comboBox.font.color.toFXColor()
                        if (!empty) {
                            this.text = comboBox.formatFunction?.invoke(item) ?: item.toString()
                        }
                    }
                }
            }
        }

        /**
         * Builds [CheckBox].
         */
        private fun buildCheckBox(checkBox: CheckBox): Region {
            val node = com.jfoenix.controls.JFXCheckBox()
            node.textProperty().bindLabelProperty(checkBox)
            node.allowIndeterminateProperty().bindBooleanProperty(checkBox.allowIndeterminateProperty)
            node.indeterminateProperty().bindBooleanProperty(checkBox.indeterminateProperty)
            node.selectedProperty().bindBooleanProperty(checkBox.checkedProperty)
            //font size
            node.bindFont(checkBox)
            return node
        }

        /**
         * Builds [ColorPicker].
         */
        private fun buildColorPicker(colorPicker: ColorPicker): Region =
            javafx.scene.control.ColorPicker().apply {
                colorPicker.selectedColorProperty.setGUIListenerAndInvoke(colorPicker.selectedColor) { _, newValue ->
                    this.value = newValue.toFXColor()
                }
                this.valueProperty().addListener { _, _, newValue ->
                    colorPicker.selectedColor = Color(
                        newValue.red.toFloat(),
                        newValue.green.toFloat(),
                        newValue.blue.toFloat(),
                        newValue.opacity.toFloat()
                    )
                }
            }

        /**
         * Builds [ListView].
         */
        private fun <T> buildListView(listView: ListView<T>): Region {
            val node = javafx.scene.control.ListView<T>(FXCollections.observableArrayList())
	
	        listView.apply {
                observableItemsList.setGUIListenerAndInvoke {
                    node.items.clear()
                    listView.items.forEach { node.items.add(it) }
                }
                fontProperty.setGUIListenerAndInvoke(this.font) { _, font ->
                    node.cellFactory = javafx.util.Callback {
                        object : ListCell<T>() {
                            override fun updateItem(item: T, empty: Boolean) {
                                this.style = font.toFXFontCSS()
                                this.textFill = font.color.toFXColor()
                                if (!empty) {
                                    this.text = listView.formatFunction?.invoke(item) ?: item.toString()
                                }
                            }
                        }
                    }
                }
                orientationProperty.setGUIListenerAndInvoke(this.orientation) { _, nV ->
                    node.orientationProperty().value = nV.toJavaFXOrientation()
                }
            }
	
	        return node
        }

        /**
         * Builds [ToggleButton] or [RadioButton].
         */
        private fun buildToggleButton(toggleButton: ToggleButton): Region {
            val node = if (toggleButton is RadioButton)
                com.jfoenix.controls.JFXRadioButton()
            else
                com.jfoenix.controls.JFXToggleButton()

            node.selectedProperty().bindBooleanProperty(toggleButton.selectedProperty)

            return node
        }

        /**
         * Builds [ProgressBar].
         */
        private fun buildProgressBar(progressBar: ProgressBar): Region = javafx.scene.control.ProgressBar().apply {
            progressBar.progressProperty.setGUIListenerAndInvoke(progressBar.progress) { _, nV ->
                this.progress = if (nV < 0.0) 0.0 else nV
            }
            progressBar.barColorProperty.setGUIListenerAndInvoke(progressBar.barColor) { _, nV ->
                progressBar.internalCSS = "-fx-accent: rgba(${nV.red},${nV.green},${nV.blue},${nV.alpha});"
            }
        }
    
        /**
         * Builds [Table].
         */
        private fun <T> buildTableView(table: Table<T>): Region {
            val node = javafx.scene.control.TableView<T>().apply {
                populateTableView(table)
            }
            table.items.guiListener = {
                node.populateTableView(table)
            }
            table.columns.guiListener = {
                node.populateTableView(table)
            }
            node.isEditable = false
            return node
        }
    
        /**
         * Sets [Table] children.
         */
        private fun <T> javafx.scene.control.TableView<T>.populateTableView(table: Table<T>) {
            items.clear()
            items.addAll(table.items)
            columns.clear()
            table.columns.forEach {
                columns.add(TableColumn<T, String>(it.title).apply {
                    this.minWidth = it.width.toDouble()
                    this.isResizable = false
                    setCellValueFactory { data ->
                        ReadOnlyStringWrapper(it.formatFunction(data.value))
                    }
                })
            }
        }
    
        /**
         * Binds [LabeledUIComponent.labelProperty].
         */
        private fun javafx.beans.property.StringProperty.bindLabelProperty(labeled: LabeledUIComponent) {
            //Framework -> JavaFX
            labeled.labelProperty.setGUIListenerAndInvoke(labeled.label) { _, nV -> value = nV }
            //JavaFX -> Framework
            addListener { _, _, new -> labeled.label = new }
        }

        /**
         * Binds [BooleanProperty].
         */
        private fun javafx.beans.property.BooleanProperty.bindBooleanProperty(booleanProperty: BooleanProperty) {
            //Framework -> JavaFX
            booleanProperty.guiListener = { _, nV -> value = nV }

            //JavaFX -> Framework
            value = booleanProperty.value
            addListener { _, _, new -> booleanProperty.value = new }
        }
    
        /**
         * Binds [Font].
         */
        private fun Labeled.bindFont(labeled: LabeledUIComponent) {
            labeled.fontProperty.setGUIListenerAndInvoke(labeled.font) { _, nV ->
                style = nV.toFXFontCSS()
                textFill = nV.color.toFXColor()
            }
        }
    }
}


