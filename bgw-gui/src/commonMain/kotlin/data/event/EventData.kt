package data.event

import ID
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import tools.aqua.bgw.event.KeyCode
import tools.aqua.bgw.event.MouseButtonType

@Serializable
abstract class EventData {
    open var id: ID? = null
}