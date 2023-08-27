import kotlinx.serialization.Serializable
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.style.Style
import tools.aqua.bgw.style.StyleDeclaration

typealias ID = String
typealias ToggleGroup = List<ID>

@Serializable
// FIXME - DONE
class SceneData {
    var width : Double = 0.0
    var height : Double = 0.0
    var background : VisualData? = null
    var components: List<ComponentViewData> = emptyList()
}

@Serializable
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
@Serializable
abstract class UIComponentData : ComponentViewData() {
    var font: FontData? = null
}

@Serializable
abstract class LabeledUIComponentData : UIComponentData() {
    var text: String = ""
    var alignment: Pair<String, String> = Pair("", "")
    var isWrapText: Boolean = false
}

@Serializable
abstract class TextInputUIComponentData : UIComponentData() {
    var text: String = ""
    var prompt: String = ""
}
@Serializable
class BinaryStateButtonData : LabeledUIComponentData() {
    var isSelected: Boolean = false
    var buttons : ToggleGroup = emptyList()
}

@Serializable
class ButtonData : LabeledUIComponentData() { }
@Serializable
class CheckBoxData : LabeledUIComponentData() {
    var isChecked: Boolean = false
    var allowIndeterminate: Boolean = false
    var isIndeterminate: Boolean = false
}
@Serializable
class ComboBoxData : UIComponentData() {
    var prompt: String = ""
    var items: List<String> = emptyList()
    var selectedItem: String = ""
}

@Serializable
class LabelData : LabeledUIComponentData() { }
@Serializable
class RadioButtonData : LabeledUIComponentData() {
    var isSelected: Boolean = false
    var buttons : ToggleGroup = emptyList()
}
@Serializable
class ToggleButtonData : LabeledUIComponentData() {
    var isSelected: Boolean = false
    var buttons : ToggleGroup = emptyList()
}
@Serializable
class ColorPickerData : UIComponentData() {
    var selectedColor: String = "rgba(0,0,0,0)"
}
@Serializable
class PasswordFieldData : TextInputUIComponentData() { }
@Serializable
class ProgressBarData : UIComponentData() {
    var progress: Double = 0.0
    var barColor: String = "rgba(0,0,0,0)"
}
@Serializable
class TextAreaData : TextInputUIComponentData() { }
@Serializable
class TextFieldData : TextInputUIComponentData() { }

abstract class StructuredDataViewData : UIComponentData() {
    var items: List<String> = emptyList()
    var selectionMode: String = ""
    var selectionBackground: ColorVisualData? = null
}
@Serializable
class TableColumnData {
    var title : String = ""
    var width : Double = 0.0
    var font : FontData? = null
    var items : List<String> = emptyList()
}
@Serializable
class ListViewData : StructuredDataViewData() {
    var orientation: String = ""
}
@Serializable
class TableViewData : StructuredDataViewData() {
    var columns: List<TableColumnData> = emptyList()
}

// LAYOUT VIEWS
@Serializable
abstract class LayoutViewData : ComponentViewData() { }

@Serializable
class PaneData : LayoutViewData() {
    var components: List<ComponentViewData> = emptyList()
}
@Serializable
class GridPaneData: LayoutViewData() {
    var columns : Int = 0
    var rows : Int = 0
    var spacing : Double = 0.0
    var grid : List<List<ComponentViewData>> = emptyList()
}
@Serializable
class CameraPaneData : ComponentViewData() {
    var target : LayoutViewData? = null
    var zoom : Double = 1.0
    var interactive : Boolean = false
}

// GAME COMPONENT VIEWS
@Serializable
abstract class GameComponentViewData : ComponentViewData() { }
@Serializable
class CardViewData : GameComponentViewData() {
    var currentSide: String = ""
    var front: VisualData? = null
    var back: VisualData? = null
}
@Serializable
class DiceViewData : GameComponentViewData() {
    var currentSide: Int = 0
    var visuals: List<VisualData> = emptyList()
}

@Serializable
class HexagonViewData : GameComponentViewData() {
    var size: Double = 0.0
}
@Serializable
class TokenViewData : GameComponentViewData() { }

// CONTAINER
@Serializable
abstract class GameComponentContainerData: ComponentViewData() {
    var components: List<GameComponentViewData> = emptyList()
}
@Serializable
class AreaData : GameComponentContainerData() { }
@Serializable
class CardStackData : GameComponentContainerData() { }
@Serializable
class HexagonGridData : GameComponentContainerData() {
    var coordinateSystem: String = ""
    var map : Map<String, HexagonViewData> = emptyMap()
    var spacing : Double = 0.0
}
@Serializable
class LinearLayoutData: GameComponentContainerData() {
    var orientation: String = ""
    var alignment: Pair<String, String> = Pair("", "")
    var spacing: Double = 0.0
}
@Serializable
class SatchelData: GameComponentContainerData() { }

// VISUALS
@Serializable
abstract class VisualData {
    var id: ID = ""
}

@Serializable
abstract class SingleLayerVisualData : VisualData() {
    var transparency: Double = 0.0
    var style: Map<String, String> = emptyMap()
    var filters: Map<String, String?> = emptyMap()
    var flipped : String = ""
}

@Serializable
class ColorVisualData : SingleLayerVisualData() {
    var color: String = ""
}

@Serializable
class ImageVisualData : SingleLayerVisualData() {
    var path: String = ""
    var width: Double = 0.0
    var height: Double = 0.0
    var offsetX: Double = 0.0
    var offsetY: Double = 0.0
}

@Serializable
class TextVisualData : SingleLayerVisualData() {
    var text: String = ""
    var font : FontData? = null
    var alignment: Pair<String, String> = Pair("", "")
    var offsetX: Double = 0.0
    var offsetY: Double = 0.0
}

@Serializable
class CompoundVisualData : VisualData() {
    var children: List<SingleLayerVisualData> = emptyList()
}

// FONT
@Serializable
class FontData {
    var size : Int = 16
    var color : String = ""
    var family : String = ""
    var fontWeight : Int = 400
    var fontStyle : String = ""
}