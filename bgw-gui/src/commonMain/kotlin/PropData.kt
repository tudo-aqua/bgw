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

@file:Suppress("unused")

import data.event.EventData
import data.event.InternalCameraPanData
import kotlinx.serialization.Serializable
import tools.aqua.bgw.dialog.DialogType

internal typealias ID = String

// PropData

@Serializable
internal enum class ActionProp {
  DEFAULT,
  SHOW_MENU_SCENE,
  SHOW_GAME_SCENE,
  HIDE_MENU_SCENE,
  HIDE_GAME_SCENE,
  UPDATE_COMPONENT,
  UPDATE_VISUAL,
  UPDATE_APP,
  DRAG_START,
  DRAG_END,
  DRAG_DROP,
}

@Serializable internal class JsonData(var container: String = "bgw-root", var props: PropData)

@Serializable internal class PropData(var data: Data? = null)

@Serializable internal sealed class Data

@Serializable
internal class AppData : Data() {
  var menuScene: SceneData? = null
  var gameScene: SceneData? = null
  var fonts: List<Triple<String, String, Int>> = emptyList()
  var width: Int = 0
  var height: Int = 0
  var background: VisualData? = null
  var alignment: Pair<String, String> = Pair("", "")
  var action: ActionProp = ActionProp.DEFAULT
  var fadeTime: Int = 0
  var blurRadius: Double = 0.0
  var forcedByAnimation: Boolean = false
}

@Serializable
internal class SceneData : Data() {
  var id: ID = ""
  var locked: Boolean = false
  var width: Int = 0
  var height: Int = 0
  var background: VisualData? = null
  var components: List<ComponentViewData> = emptyList()
}

@Serializable
internal abstract class AnimationData : Data() {
  var id: ID = ""
  var duration: Int = 0
  var isRunning: Boolean = false
  var onFinished: ((EventData) -> Unit)? = null
  var animationType: String = ""
  var isStop: Boolean = false
  var interpolation: String = "linear"
}

@Serializable
internal class DialogData : Data() {
  var id: ID = ""
  var dialogType: DialogType = DialogType.INFORMATION
  var title: String = ""
  var header: String = ""
  var message: String = ""
  var exception: String = ""
  var buttons: List<ButtonTypeData> = emptyList()
}

@Serializable
internal class ButtonTypeData : Data() {
  var text: String = ""
  var backgroundColor: String = ""
  var foregroundColor: String = ""
}

@Serializable
internal class FileDialogData : Data() {
  var id: ID = ""
  var mode: String = ""
  var title: String = ""
  var initialFileName: String = ""
  var initialDirectoryPath: String? = null
  var extensionFilters: List<Pair<String, List<String>>> = emptyList()
}

@Serializable
internal class DialogButtonClickData(val dialogId: String, val buttonIndex: Int) : Data()

@Serializable
internal abstract class ComponentViewData : Data() {
  var id: ID = ""
  var posX: Int = 0
  var posY: Int = 0
  var width: Int = 0
  var height: Int = 0
  var visual: VisualData? = null
  var zIndex: Int = 0
  var opacity: Double = 1.0
  var isVisible: Boolean = true
  var isDisabled: Boolean = false
  var isFocusable: Boolean = true
  var scaleX: Double = 1.0
  var scaleY: Double = 1.0
  var rotation: Double = 0.0
  var layoutFromCenter: Boolean = false
  var isDraggable: Boolean = false
  var isDragged: Boolean = false
  var isDroppable: Boolean = false

  var hasMouseEnteredEvent: Boolean = false
  var hasMouseExitedEvent: Boolean = false
}

// UI COMPONENTS
@Serializable
internal abstract class UIComponentData : ComponentViewData() {
  var font: FontData = FontData(16, "rgba(0,0,0,1)", "Arial", 400, "normal")
}

@Serializable
internal abstract class LabeledUIComponentData : UIComponentData() {
  var text: String = ""
  var alignment: Pair<String, String> = Pair("", "")
  var isWrapText: Boolean = false
}

@Serializable
internal abstract class TextInputUIComponentData : UIComponentData() {
  var text: String = ""
  var prompt: String = ""
  var isReadonly: Boolean = false
}

@Serializable
internal class BinaryStateButtonData : LabeledUIComponentData() {
  var isSelected: Boolean = false
  var group: String = ""
}

@Serializable internal class ButtonData : LabeledUIComponentData() {}

@Serializable
internal class CheckBoxData : LabeledUIComponentData() {
  var isChecked: Boolean = false
  var allowIndeterminate: Boolean = false
  var isIndeterminate: Boolean = false
}

@Serializable
internal class ComboBoxData : UIComponentData() {
  var prompt: String = ""
  var items: List<Pair<Int, String>> = emptyList()
  var itemVisuals: List<VisualData> = emptyList()
  var selectedItem: Pair<Int, String>? = null
  var disallowUnselect: Boolean = false
}

@Serializable internal class LabelData : LabeledUIComponentData() {}

@Serializable
internal class RadioButtonData : LabeledUIComponentData() {
  var isSelected: Boolean = false
  var group: String = ""
}

@Serializable
internal class ToggleButtonData : LabeledUIComponentData() {
  var isSelected: Boolean = false
  var group: String = ""
}

@Serializable
internal class ColorPickerData : UIComponentData() {
  var selectedColor: String = "#000000"
}

@Serializable internal class PasswordFieldData : TextInputUIComponentData() {}

@Serializable
internal class ProgressBarData : UIComponentData() {
  var progress: Double = 0.0
  var barVisual: VisualData? = null
}

@Serializable internal class TextAreaData : TextInputUIComponentData() {}

@Serializable internal class TextFieldData : TextInputUIComponentData() {}

@Serializable
internal abstract class StructuredDataViewData : UIComponentData() {
  var items: List<String> = emptyList()
  var selectionMode: String = ""
  var selectionBackground: String = "#FF0000"
  var selectedItems: List<Int> = emptyList()
}

@Serializable
internal class TableColumnData {
  var title: String = ""
  var width: Int = 0
  var font: FontData = FontData(16, "rgba(0,0,0,1)", "Arial", 400, "normal")
  var items: List<String> = emptyList()
}

@Serializable
internal class ListViewData : StructuredDataViewData() {
  var orientation: String = ""
}

@Serializable
internal class TableViewData : StructuredDataViewData() {
  var columns: List<TableColumnData> = emptyList()
}

// LAYOUT VIEWS
@Serializable internal abstract class LayoutViewData : ComponentViewData() {}

@Serializable
internal class PaneData : LayoutViewData() {
  var components: List<ComponentViewData> = emptyList()
}

@Serializable
internal class GridPaneData : LayoutViewData() {
  var columns: Int = 0
  var rows: Int = 0
  var spacing: Int = 0
  var grid: List<GridElementData> = emptyList()
}

@Serializable
internal class GridElementData(
    var column: Int,
    var row: Int,
    var component: ComponentViewData?,
    var alignment: Pair<String, String>
)

@Serializable
internal class CameraPaneData : ComponentViewData() {
  var target: LayoutViewData? = null
  var interactive: Boolean = false
  var panButton: String = "left_button"
  var limitBounds: Boolean = true
  var internalPanData: InternalCameraPanData = InternalCameraPanData()
}

@Serializable internal class CoordinateData(val xCoord: Double = 0.0, val yCoord: Double = 0.0)

// GAME COMPONENT VIEWS
@Serializable internal abstract class GameComponentViewData : ComponentViewData() {}

@Serializable
internal class CardViewData : GameComponentViewData() {
  var front: VisualData? = null
  var back: VisualData? = null
  var currentVisual: VisualData? = null
}

@Serializable
internal class DiceViewData : GameComponentViewData() {
  var currentSide: Int = 0
  var visuals: List<VisualData> = emptyList()
}

@Serializable
internal class HexagonViewData : GameComponentViewData() {
  var size: Int = 0
  var orientation: String = ""
}

@Serializable internal class TokenViewData : GameComponentViewData() {}

// CONTAINER
@Serializable
internal abstract class GameComponentContainerData : ComponentViewData() {
  var components: List<GameComponentViewData> = emptyList()
}

@Serializable internal class AreaData : GameComponentContainerData() {}

@Serializable
internal class CardStackData : GameComponentContainerData() {
  var alignment: Pair<String, String> = Pair("", "")
}

@Serializable
internal class HexagonGridData : GameComponentContainerData() {
  var coordinateSystem: String = ""
  var map: Map<String, HexagonViewData> = emptyMap()
  var spacing: Int = 0
  var orientation: String = ""
}

@Serializable
internal class LinearLayoutData : GameComponentContainerData() {
  var orientation: String = ""
  var alignment: Pair<String, String> = Pair("", "")
  var spacing: Int = 0
}

@Serializable internal class SatchelData : GameComponentContainerData() {}

// VISUALS
@Serializable internal sealed class VisualData(var id: ID = "") : Data()

@Serializable
internal sealed class SingleLayerVisualData(
    var transparency: Double = 0.0,
    var style: Map<String, String> = emptyMap(),
    var filters: Map<String, String> = emptyMap(),
    var flipped: String = "",
    var rotation: Double = 0.0
) : VisualData()

@Serializable internal data class ColorVisualData(var color: String = "") : SingleLayerVisualData()

@Serializable
internal data class ImageVisualData(
    var path: String = "",
    var width: Int = 0,
    var height: Int = 0,
    var offsetX: Int = 0,
    var offsetY: Int = 0
) : SingleLayerVisualData()

@Serializable
internal data class TextVisualData(
    var text: String = "",
    var font: FontData = FontData(16, "rgba(0,0,0,1)", "Arial", 400, "normal"),
    var alignment: Pair<String, String> = Pair("", ""),
    var offsetX: Int = 0,
    var offsetY: Int = 0
) : SingleLayerVisualData()

@Serializable
internal data class CompoundVisualData(var children: List<SingleLayerVisualData> = emptyList()) :
    VisualData()

// FONT
@Serializable
internal data class FontData(
    var size: Int = 16,
    var color: String = "",
    var family: String = "",
    var fontWeight: Int = 400,
    var fontStyle: String = ""
)
