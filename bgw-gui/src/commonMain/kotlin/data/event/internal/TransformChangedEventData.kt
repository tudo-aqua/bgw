package data.event.internal

import data.event.EventData
import kotlinx.serialization.Serializable

@Serializable
internal class TransformChangedEventData(
    val zoomLevel : Double = 1.0,
    val anchor : Pair<Double, Double> = Pair(0.0, 0.0)
) : EventData()