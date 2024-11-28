package data.event.internal

import data.event.EventData
import kotlinx.serialization.Serializable


@Serializable
internal class DragGestureEndedEventData(
    var dropped: Boolean
): EventData() {
}