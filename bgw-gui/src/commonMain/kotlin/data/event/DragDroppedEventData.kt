package data.event

import ID
import kotlinx.serialization.Serializable

@Serializable
internal class DragDroppedEventData(
    var target: ID
) : EventData()