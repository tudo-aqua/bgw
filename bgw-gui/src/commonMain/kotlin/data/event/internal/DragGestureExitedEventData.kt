package data.event.internal

import ID
import data.event.EventData
import kotlinx.serialization.Serializable

@Serializable
internal class DragGestureExitedEventData(
    var target: ID
) : EventData() {}