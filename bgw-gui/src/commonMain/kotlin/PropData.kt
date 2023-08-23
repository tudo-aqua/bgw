typealias ID = String
typealias ToggleGroup = List<ID>
typealias Array2D = List<List<ID>>

abstract class ComponentViewData {
    var id: ID = ""
    var posX: Double = 0.0
    var posY: Double = 0.0
    var width: Double = 0.0
    var height: Double = 0.0
    var visual: VisualData? = null
    var zIndex : Int = 0
    var opacity : Double = 1.0
    var isVisible: Boolean = true
    var isDisabled: Boolean = false
    var isFocusable : Boolean = true
    var scaleX : Double = 1.0
    var scaleY : Double = 1.0
    var rotation : Double = 0.0
    var layoutFromCenter : Boolean = false
    var isDraggable : Boolean = false
}

// UI COMPONENTS

abstract class UIComponentData : ComponentViewData() {
    var font: FontData? = null
}

abstract class LabeledUIComponentData : UIComponentData() {
    var text: String = ""
    var alignment: String = ""
    var isWrapText: Boolean = false
}

abstract class TextInputUIComponentData : UIComponentData() {
    var text: String = ""
    var prompt: String = ""
}

class BinaryStateButtonData : LabeledUIComponentData() {
    var isSelected: Boolean = false
    var buttons : ToggleGroup = emptyList()
}

class ButtonData : LabeledUIComponentData() { }

class CheckBoxData : LabeledUIComponentData() {
    var isChecked: Boolean = false
    var allowIndeterminate: Boolean = false
    var isIndeterminate: Boolean = false
}

class ComboBoxData : UIComponentData() {
    var prompt: String = ""
    var items: List<String> = emptyList()
    var selectedItem: String = ""
}

class LabelData : LabeledUIComponentData() { }

class RadioButtonData : LabeledUIComponentData() {
    var isSelected: Boolean = false
    var buttons : ToggleGroup = emptyList()
}

class ToggleButtonData : LabeledUIComponentData() {
    var isSelected: Boolean = false
    var buttons : ToggleGroup = emptyList()
}

class ColorPickerData : UIComponentData() {
    var selectedColor: String = "#FFFFFF"
}

class PasswordFieldData : TextInputUIComponentData() { }

class ProgressBarData : UIComponentData() {
    var progress: Double = 0.0
    var barColor: String = ""
}

class TextAreaData : TextInputUIComponentData() { }

class TextFieldData : TextInputUIComponentData() { }

abstract class StructuredDataViewData : UIComponentData() {
    var items: List<String> = emptyList()
    var selectionMode: String = ""
    var selectionBackground: ColorVisualData? = null
}

class TableColumnData {
    var title : String = ""
    var width : Double = 0.0
    var font : FontData? = null
    var items : List<String> = emptyList()
}

class ListViewData : StructuredDataViewData() {
    var orientation: String = ""
}

class TableViewData : StructuredDataViewData() {
    var columns: List<TableColumnData> = emptyList()
}

// LAYOUT VIEWS

abstract class LayoutViewData<T : ComponentViewData> : ComponentViewData() { }

class PaneData<T : ComponentViewData> : LayoutViewData<T>() {
    var components: List<T> = emptyList()
}

class GridPaneData<T : ComponentViewData> : LayoutViewData<T>() {
    var columns : Int = 0
    var rows : Int = 0
    var spacing : Double = 0.0
    var grid : Array2D = emptyList()
}

class CameraPaneData<T : LayoutViewData<*>> : LayoutViewData<T>() {
    var target : T? = null
    var zoom : Double = 1.0
    var interactive : Boolean = false
}

// GAME COMPONENT VIEWS

abstract class GameComponentViewData : ComponentViewData() { }

class CardViewData : GameComponentViewData() {
    var currentSide: String = ""
    var front: VisualData? = null
    var back: VisualData? = null
}

class DiceViewData : GameComponentViewData() {
    var currentSide: Int = 0
    var visuals: List<VisualData> = emptyList()
}

class HexagonViewData : GameComponentViewData() {
    var size: Double = 0.0
}

class TokenViewData : GameComponentViewData() { }

// CONTAINER

abstract class GameComponentContainerData<T : ComponentViewData>: ComponentViewData() {
    var components: List<GameComponentViewData> = emptyList()
}

class AreaData<T : ComponentViewData> : GameComponentContainerData<T>() { }

class CardStackData<T : ComponentViewData> : GameComponentContainerData<T>() { }

class HexagonGridData<T : ComponentViewData> : GameComponentContainerData<T>() {
    var coordinateSystem: String = ""
}

class LinearLayoutData<T : ComponentViewData> : GameComponentContainerData<T>() {
    var orientation: String = ""
    var alignment: String = ""
    var spacing: Double = 0.0
}

class SatchelData<T : ComponentViewData> : GameComponentContainerData<T>() { }

// VISUALS

abstract class VisualData {
    var id: ID = ""
}

abstract class SingleLayerVisualData : VisualData() {
    var transparency: Double = 0.0
    var style: String = ""
}

class ColorVisualData : SingleLayerVisualData() {
    var color: String = ""
}

class ImageVisualData : SingleLayerVisualData() {
    var path: String = ""
    var width: Double = 0.0
    var height: Double = 0.0
    var offsetX: Double = 0.0
    var offsetY: Double = 0.0
}

class TextVisualData : SingleLayerVisualData() {
    var text: String = ""
    var font : FontData? = null
    var alignment: String = ""
    var offsetX: Double = 0.0
    var offsetY: Double = 0.0
}

class CompoundVisualData : VisualData() {
    var children: List<SingleLayerVisualData> = emptyList()
}

abstract class FontData {
    var size : Double = 16.0
    var color : String = ""
    var family : String = ""
    var fontWeight : String = ""
    var fontStyle : String = ""
}