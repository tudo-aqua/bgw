package data.event

import kotlinx.serialization.Serializable

@Serializable
internal enum class DragEventAction {
    START, DROP, END, ENTER, EXIT
}