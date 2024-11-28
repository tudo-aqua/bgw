package data.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import tools.aqua.bgw.event.KeyCode

@Serializable
@SerialName("KeyEventData")
internal class KeyEventData(
    var keyCode: KeyCode,
    var character: String,
    var isControlDown: Boolean,
    var isShiftDown: Boolean,
    var isAltDown: Boolean,
    val action: KeyEventAction
) : InputEventData()

@Serializable
enum class KeyEventAction {
    PRESS, RELEASE, TYPE
}