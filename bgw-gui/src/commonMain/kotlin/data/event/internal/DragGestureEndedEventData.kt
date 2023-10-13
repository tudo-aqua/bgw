package data.event.internal

import data.event.EventData
import kotlinx.serialization.Serializable


@Serializable
class DragGestureEndedEventData(
    var dropped: Boolean
): EventData() {
}