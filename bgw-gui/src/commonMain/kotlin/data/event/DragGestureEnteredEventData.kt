package data.event

import ID
import kotlinx.serialization.Serializable

@Serializable
internal class DragGestureEnteredEventData(
    var target: ID
) : EventData()