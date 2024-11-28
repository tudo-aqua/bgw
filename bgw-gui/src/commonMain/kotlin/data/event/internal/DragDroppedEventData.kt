package data.event.internal

import ComponentViewData
import ID
import data.event.EventData
import kotlinx.serialization.Serializable

@Serializable
internal class DragDroppedEventData(
    var target: ID
) : EventData() {
}