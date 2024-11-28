import data.event.EventData
import kotlinx.serialization.Serializable

@Serializable
internal data class InternalCameraPaneData(
    var lastMousePosition: Pair<Double, Double> = Pair(0.0, 0.0),
    var anchorPoint: Pair<Double, Double> = Pair(0.0, 0.0),
    var startPos: Pair<Double, Double> = Pair(0.0, 0.0),
    var endPos: Pair<Double, Double> = Pair(0.0, 0.0),
    var animValue: Double = 0.0,
    var startY : Int = 0,
    var startX : Int = 0
) : EventData()