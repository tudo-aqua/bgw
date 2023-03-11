package tools.aqua.bgw.net.server.view.components.editor

import elemental.json.impl.JreJsonFactory
import elemental.json.impl.JreJsonObject

data class Annotation(
    val row: Int,
    val column: Int,
    val text: String,
    val type: String,
) : JreJsonObject(JreJsonFactory()) {
    init {
        this.put("row", row.toDouble())
        this.put("column", column.toDouble())
        this.put("text", text)
        this.put("type", type)
    }
}