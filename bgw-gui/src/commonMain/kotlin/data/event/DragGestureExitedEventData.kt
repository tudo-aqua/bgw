package data.event

import ID
import kotlinx.serialization.Serializable

@Serializable
internal class DragGestureExitedEventData(
    var target: ID
) : EventData()