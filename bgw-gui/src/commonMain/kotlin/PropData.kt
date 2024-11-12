import data.event.EventData
import kotlinx.serialization.Serializable
import tools.aqua.bgw.dialog.DialogType

typealias ID = String

// PropData

@Serializable
enum class ActionProp {
    DEFAULT,
    SHOW_MENU_SCENE,
    SHOW_GAME_SCENE,
    HIDE_MENU_SCENE,
    HIDE_GAME_SCENE,
    UPDATE_COMPONENT,
    UPDATE_VISUAL,
    DRAG_START,
    DRAG_END,
    DRAG_DROP,
}

@Serializable
class JsonData(
    var container : String = "bgw-root",
    var props : PropData
)

@Serializable
class PropData(
    var data: Data? = null
)

@Serializable
sealed class Data
@Serializable
class AppData : Data() {
    var menuScene: SceneData? = null
    var gameScene: SceneData? = null
    var fonts : List<Triple<String, String, Int>> = emptyList()
    var width : Int = 0
    var height : Int = 0
    var action : ActionProp = ActionProp.DEFAULT
}

@Serializable
class SceneData : Data() {
    var width : Int = 0
    var height : Int = 0
    var background : VisualData? = null
    var components: List<ComponentViewData> = emptyList()
}

@Serializable
abstract class AnimationData : Data() {
    var id: ID = ""
    var duration: Int = 0
    var isRunning: Boolean = false
    var onFinished: ((EventData) -> Unit)? = null
}

@Serializable
class DialogData : Data() {
    var dialogType : DialogType = DialogType.INFORMATION
    var title : String = ""
    var header : String = ""
    var message : String = ""
    var exception : String = ""
}

@Serializable
abstract class ComponentViewData : Data() {
    var id: ID = ""
    var posX: Int = 0
    var posY: Int = 0
    var width: Int = 0
    var height: Int = 0
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
    var isDragged : Boolean = false
}

// UI COMPONENTS
@Serializable
abstract class UIComponentData : ComponentViewData() {
    var font: FontData = FontData(16, "rgba(0,0,0,1)", "Arial", 400, "normal")
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
    var group : String = ""
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
    var items: List<Pair<Int, String>> = emptyList()
    var selectedItem: Pair<Int, String>? = null
}

@Serializable
class LabelData : LabeledUIComponentData() { }
@Serializable
class RadioButtonData : LabeledUIComponentData() {
    var isSelected: Boolean = false
    var group : String = ""
}
@Serializable
class ToggleButtonData : LabeledUIComponentData() {
    var isSelected: Boolean = false
    var group : String = ""
}
@Serializable
class ColorPickerData : UIComponentData() {
    var selectedColor: String = "#000000"
}
@Serializable
class PasswordFieldData : TextInputUIComponentData() { }
@Serializable
class ProgressBarData : UIComponentData() {
    var progress: Double = 0.0
    var barColor: String = "#000000"
}
@Serializable
class TextAreaData : TextInputUIComponentData() { }
@Serializable
class TextFieldData : TextInputUIComponentData() { }

@Serializable
abstract class StructuredDataViewData : UIComponentData() {
    var items: List<String> = emptyList()
    var selectionMode: String = ""
    var selectionBackground: String = "#FF0000"
    var selectedItems: List<Int> = emptyList()
}
@Serializable
class TableColumnData {
    var title : String = ""
    var width : Int = 0
    var font : FontData = FontData(16, "rgba(0,0,0,1)", "Arial", 400, "normal")
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
    var spacing : Int = 0
    var grid : List<GridElementData> = emptyList()
}

@Serializable
class GridElementData(
    var column : Int,
    var row : Int,
    var component : ComponentViewData?,
    var alignment: Pair<String, String>
)

@Serializable
class CameraPaneData : ComponentViewData() {
    var target : LayoutViewData? = null
    var zoom : Double = 1.0
    var interactive : Boolean = false
    var scroll : CoordinateData = CoordinateData(0.0,0.0)
    var internalData : InternalCameraPaneData = InternalCameraPaneData()
}

@Serializable
class CoordinateData(val xCoord: Double = 0.0, val yCoord: Double = 0.0)

// GAME COMPONENT VIEWS
@Serializable
abstract class GameComponentViewData : ComponentViewData() { }
@Serializable
class CardViewData : GameComponentViewData() {
    var front: VisualData? = null
    var back: VisualData? = null
    var currentVisual : VisualData? = null
}
@Serializable
class DiceViewData : GameComponentViewData() {
    var currentSide: Int = 0
    var visuals: List<VisualData> = emptyList()
}

@Serializable
class HexagonViewData : GameComponentViewData() {
    var size: Int = 0
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
class CardStackData : GameComponentContainerData() {
    var alignment: Pair<String, String> = Pair("", "")
}
@Serializable
class HexagonGridData : GameComponentContainerData() {
    var coordinateSystem: String = ""
    var map : Map<String, HexagonViewData> = emptyMap()
    var spacing : Int = 0
}
@Serializable
class LinearLayoutData: GameComponentContainerData() {
    var orientation: String = ""
    var alignment: Pair<String, String> = Pair("", "")
    var spacing: Int = 0
}
@Serializable
class SatchelData: GameComponentContainerData() { }

// VISUALS
@Serializable
sealed class VisualData(
    var id: ID = ""
) : Data()

@Serializable
sealed class SingleLayerVisualData(
    var transparency: Double = 0.0,
    var style: Map<String, String> = emptyMap(),
    var filters: Map<String, String?> = emptyMap(),
    var flipped : String = ""
) : VisualData()

@Serializable
data class ColorVisualData(
    var color: String = ""
) : SingleLayerVisualData()

@Serializable
data class ImageVisualData(
    var path: String = "",
    var width: Int = 0,
    var height: Int = 0,
    var offsetX: Int = 0,
    var offsetY: Int = 0
) : SingleLayerVisualData()

@Serializable
data class TextVisualData(
    var text: String = "",
    var font : FontData = FontData(16, "rgba(0,0,0,1)", "Arial", 400, "normal"),
    var alignment: Pair<String, String> = Pair("", ""),
    var offsetX: Int = 0,
    var offsetY: Int = 0
) : SingleLayerVisualData()

@Serializable
data class CompoundVisualData(
    var children: List<SingleLayerVisualData> = emptyList()
) : VisualData()

// FONT
@Serializable
data class FontData(
    var size : Int = 16,
    var color : String = "",
    var family : String = "",
    var fontWeight : Int = 400,
    var fontStyle : String = ""
)