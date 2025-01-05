package data.event

import kotlinx.serialization.Serializable


@Serializable
internal class DragGestureEndedEventData(
    var dropped: Boolean
) : EventData()