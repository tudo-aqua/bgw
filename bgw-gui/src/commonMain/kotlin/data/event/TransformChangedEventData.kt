package data.event

import kotlinx.serialization.Serializable

@Serializable
internal class TransformChangedEventData(
    val zoomLevel: Double = 1.0,
    val anchor: Pair<Double, Double> = Pair(0.0, 0.0)
) : EventData()