package data.event.internal

import data.event.EventData
import kotlinx.serialization.Serializable

@Serializable
internal class ZoomChangedEventData(
    val zoomLevel : Double = 1.0
) : EventData()