package data.event.internal

import data.event.EventData
import kotlinx.serialization.Serializable

@Serializable
class ScrollChangedEventData(
    val scrollLeft : Double = 0.0,
    val scrollTop : Double = 0.0
) : EventData()