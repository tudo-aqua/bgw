package data.event

import kotlinx.serialization.Serializable

@Serializable
enum class DragEventAction {
    START, DROP, END, ENTER, EXIT
}