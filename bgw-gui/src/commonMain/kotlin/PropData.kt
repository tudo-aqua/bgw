class ComponentViewData {
    var id: String = ""
    var posX: Int = 0
    var posY: Int = 0
    var width: Int = 0
    var height: Int = 0
}

abstract class VisualData {
    var id: String = ""
    var posX: Int = 0
    var posY: Int = 0
    var width: Int = 0
    var height: Int = 0
    var visual: VisualData? = null
}

class ColorVisualData : VisualData() {
    var color: String = ""
}

class ImageVisualData : VisualData() {
    var path: String = ""
}

class TextVisualData : VisualData() {
    var text: String = ""
    var fontSize: Int = 0
    var fontColor: String = ""
}

class CompoundVisualData : VisualData() {
    var children: List<VisualData> = emptyList()
}