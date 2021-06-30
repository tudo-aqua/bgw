package tools.aqua.bgw.builder

import javafx.beans.property.ReadOnlyStringWrapper
import javafx.collections.FXCollections
import javafx.geometry.Pos
import javafx.scene.control.Labeled
import javafx.scene.control.ListCell
import javafx.scene.control.TableColumn
import javafx.scene.layout.Region
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontPosture
import javafx.scene.text.FontWeight
import tools.aqua.bgw.builder.VisualBuilder.Companion.MAX_HEX
import tools.aqua.bgw.elements.uielements.*
import tools.aqua.bgw.observable.BooleanProperty
import tools.aqua.bgw.util.FontStyle

/**
 * UINodeBuilder.
 * Factory for all BGW UI elements.
 */
internal class UINodeBuilder {
	companion object {
		internal fun buildLabel(label: Label): Region {
			val node = javafx.scene.control.Label()
			node.alignment = Pos.CENTER
			node.textProperty().bindTextProperty(label)
			node.bindFont(label)
			return node
		}
		
		internal fun buildButton(button: Button): Region {
			val node = com.jfoenix.controls.JFXButton()
			node.textProperty().bindTextProperty(button)
			node.bindFont(button)
			return node
		}
		
		internal fun buildTextArea(textArea: TextArea): Region {
			val node = javafx.scene.control.TextArea(textArea.labelProperty.value)
			
			node.textProperty().bindTextProperty(textArea)
			node.promptText = textArea.prompt
			textArea.fontProperty.setGUIListenerAndInvoke(textArea.font) { _, nV ->
				node.font = nV.toFXFont()
				//TODO text color
			}
			return node
		}
		
		internal fun <T> buildComboBox(comboBox: ComboBox<T>): Region {
			val node = com.jfoenix.controls.JFXComboBox<T>(FXCollections.observableArrayList(comboBox.items))
			comboBox.setGUIListenerAndInvoke {
				node.items.clear()
				comboBox.items.forEach { node.items.add(it) }
			}
			node.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
				comboBox.selectedItem = newValue
			}
			//font size
			comboBox.fontProperty.setGUIListenerAndInvoke(comboBox.font) { _, nV ->
				node.editor.font = nV.toFXFont()
				//TODO text color
			}
			return node
		}
		
		internal fun buildCheckBox(checkBox: CheckBox): Region {
			val node = com.jfoenix.controls.JFXCheckBox(checkBox.label)
			node.textProperty().bindTextProperty(checkBox)
			node.allowIndeterminateProperty().bindBooleanProperty(checkBox.allowIndeterminateProperty)
			node.indeterminateProperty().bindBooleanProperty(checkBox.indeterminateProperty)
			node.selectedProperty().bindBooleanProperty(checkBox.checkedProperty)
			//font size
			node.bindFont(checkBox)
			return node
		}
		
		internal fun buildColorPicker(colorPicker: ColorPicker): Region =
			javafx.scene.control.ColorPicker(colorPicker.initialColor.toFXColor())
		
		internal fun <T> buildListView(listView: ListView<T>): Region {
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
								this.font = font.toFXFont()
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
		
		internal fun buildToggleButton(toggleButton: ToggleButton): Region {
			val node = if (toggleButton is RadioButton)
				com.jfoenix.controls.JFXRadioButton()
			else
				com.jfoenix.controls.JFXToggleButton()
			
			node.selectedProperty().bindBooleanProperty(toggleButton.selectedProperty)
			
			return node
		}
		
		fun buildProgressBar(progressBar: ProgressBar): Region = javafx.scene.control.ProgressBar().apply {
			progressBar.progressProperty.setGUIListenerAndInvoke(progressBar.progress) { _, nV ->
				this.progress = if (nV < 0.0) 0.0 else nV
			}
			progressBar.barColorProperty.setGUIListenerAndInvoke(progressBar.barColor) { _, nV ->
				//TODO remove css usage
				style = "-fx-accent: rgba(${nV.red},${nV.green},${nV.blue},${nV.alpha});"
			}
		}
		
		internal fun <T> buildTableView(uiElementView: TableView<T>): Region {
			val node = javafx.scene.control.TableView<T>().apply {
				populateTableView(uiElementView)
			}
			uiElementView.items.guiListener = {
				node.populateTableView(uiElementView)
			}
			uiElementView.columns.guiListener = {
				node.populateTableView(uiElementView)
			}
			node.isEditable = false
			return node
		}
		
		private fun <T> javafx.scene.control.TableView<T>.populateTableView(tableView: TableView<T>) {
			items.clear()
			items.addAll(tableView.items)
			columns.clear()
			tableView.columns.forEach {
				columns.add(TableColumn<T, String>(it.title).apply {
					this.minWidth = it.width.toDouble()
					this.isResizable = false
					setCellValueFactory { data ->
						ReadOnlyStringWrapper(it.formatFunction(data.value))
					}
				})
			}
		}
		
		private fun javafx.beans.property.StringProperty.bindTextProperty(labeled: LabeledUIElementView) {
			//Framework -> JavaFX
			labeled.labelProperty.setGUIListenerAndInvoke(labeled.label) { _, nV -> value = nV }
			//JavaFX -> Framework
			addListener { _, _, new -> labeled.label = new }
		}
		
		private fun javafx.beans.property.BooleanProperty.bindBooleanProperty(booleanProperty: BooleanProperty) {
			//Framework -> JavaFX
			booleanProperty.guiListener = { _, nV -> value = nV }
			
			//JavaFX -> Framework
			value = booleanProperty.value
			addListener { _, _, new -> booleanProperty.value = new }
		}
		
		private fun Orientation.toJavaFXOrientation(): javafx.geometry.Orientation {
			return when (this) {
				Orientation.HORIZONTAL -> javafx.geometry.Orientation.HORIZONTAL
				Orientation.VERTICAL -> javafx.geometry.Orientation.VERTICAL
			}
		}
		
		private fun java.awt.Color.toFXColor(): Color = Color(
			red / MAX_HEX,
			green / MAX_HEX,
			blue / MAX_HEX,
			alpha / MAX_HEX,
		)
		
		private fun Labeled.bindFont(labeled: LabeledUIElementView) {
			labeled.fontProperty.setGUIListenerAndInvoke(labeled.font) { _, nV ->
				font = nV.toFXFont()
				textFill = nV.color.toFXColor()
			}
		}
		
		private fun tools.aqua.bgw.util.Font.toFXFont(): Font {
			val fontWeight: FontWeight
			val fontPosture: FontPosture
			when (fontStyle) {
				FontStyle.BOLD -> {
					fontWeight = FontWeight.BOLD; fontPosture = FontPosture.REGULAR
				}
				FontStyle.REGULAR -> {
					fontWeight = FontWeight.NORMAL; fontPosture = FontPosture.REGULAR
				}
				FontStyle.SEMI_BOLD -> {
					fontWeight = FontWeight.SEMI_BOLD; fontPosture = FontPosture.REGULAR
				}
				FontStyle.Italic -> {
					fontWeight = FontWeight.NORMAL; fontPosture = FontPosture.ITALIC
				}
			}
			return Font.font(family, fontWeight, fontPosture, size.toDouble())
		}
	}
}


