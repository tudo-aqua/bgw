package tools.aqua.bgw.builder

import com.jfoenix.controls.JFXComboBox
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.collections.FXCollections
import javafx.geometry.Pos
import javafx.scene.control.Labeled
import javafx.scene.control.ListCell
import javafx.scene.control.TableColumn
import javafx.scene.layout.Region
import tools.aqua.bgw.elements.uielements.*
import tools.aqua.bgw.observable.BooleanProperty
import tools.aqua.bgw.util.Font

/**
 * UINodeBuilder.
 * Factory for all BGW UI elements.
 */
internal class UINodeBuilder {
	companion object {
		/**
		 * Switches between [UIElementView]s.
		 */
		internal fun buildUIElement(uiElementView: UIElementView): Region =
			when (uiElementView) {
				is Button ->
					buildButton(uiElementView)
				is CheckBox ->
					buildCheckBox(uiElementView)
				is ComboBox<*> ->
					buildComboBox(uiElementView)
				is Label ->
					buildLabel(uiElementView)
				is ListView<*> ->
					buildListView(uiElementView)
				is TableView<*> ->
					buildTableView(uiElementView)
				is TextArea ->
					buildTextArea(uiElementView)
				is ToggleButton ->
					buildToggleButton(uiElementView)
				is ColorPicker ->
					buildColorPicker(uiElementView)
				is ProgressBar ->
					buildProgressBar(uiElementView)
			}
		
		private fun buildLabel(label: Label): Region {
			val node = javafx.scene.control.Label()
			node.alignment = Pos.CENTER
			node.textProperty().bindTextProperty(label)
			node.bindFont(label)
			return node
		}
		
		/**
		 * Builds [Button].
		 */
		private fun buildButton(button: Button): Region {
			val node = com.jfoenix.controls.JFXButton()
			node.textProperty().bindTextProperty(button)
			node.bindFont(button)
			return node
		}
		
		/**
		 * Builds [TextArea].
		 */
		private fun buildTextArea(textArea: TextArea): Region {
			val node = javafx.scene.control.TextArea(textArea.labelProperty.value)
			
			node.textProperty().bindTextProperty(textArea)
			node.promptText = textArea.prompt
			textArea.fontProperty.setGUIListenerAndInvoke(textArea.font) { _, nV ->
				node.font = nV.toFXFont()
				//TODO text color
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
				node.buttonCell.font = comboBox.font.toFXFont()
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
						this.font = comboBox.font.toFXFont()
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
			val node = com.jfoenix.controls.JFXCheckBox(checkBox.label)
			node.textProperty().bindTextProperty(checkBox)
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
			javafx.scene.control.ColorPicker(colorPicker.initialColor.toFXColor())
		
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
				//TODO remove css usage
				style = "-fx-accent: rgba(${nV.red},${nV.green},${nV.blue},${nV.alpha});"
			}
		}
		
		/**
		 * Builds [TableView].
		 */
		private fun <T> buildTableView(uiElementView: TableView<T>): Region {
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
		
		/**
		 * Sets [TableView] children.
		 */
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
		
		/**
		 * Binds [LabeledUIElementView.labelProperty].
		 */
		private fun javafx.beans.property.StringProperty.bindTextProperty(labeled: LabeledUIElementView) {
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
		private fun Labeled.bindFont(labeled: LabeledUIElementView) {
			labeled.fontProperty.setGUIListenerAndInvoke(labeled.font) { _, nV ->
				font = nV.toFXFont()
				textFill = nV.color.toFXColor()
			}
		}
	}
}


