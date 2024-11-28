package data.event

import ID
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal abstract class EventData {
    open var id: ID? = null
}