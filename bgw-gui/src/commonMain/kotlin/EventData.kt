import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import tools.aqua.bgw.event.InputEvent
import tools.aqua.bgw.event.KeyCode
import tools.aqua.bgw.event.MouseButtonType

@Serializable
abstract class EventData

@Serializable
abstract class InputEventData : EventData()

@Serializable
@SerialName("MouseEventData")
class MouseEventData(
    val id: ID?,
    val button: MouseButtonType,
    val posX: Double,
    val posY: Double,
) : InputEventData() {
}

@Serializable
@SerialName("KeyEventData")
class KeyEventData(
    val id: ID,
    var keyCode: KeyCode,
    var character: String,
    var isControlDown: Boolean,
    var isShiftDown: Boolean,
    var isAltDown: Boolean,
) : InputEventData()

@Serializable
class EventsData {
    var eventData : List<EventData> = emptyList()
}
@Serializable
internal class LoadEventData : EventData()