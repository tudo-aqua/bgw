package data.event.internal

import ID
import data.event.EventData
import kotlinx.serialization.Serializable

@Serializable
class DragGestureExitedEventData(
    var target: ID
) : EventData() {}