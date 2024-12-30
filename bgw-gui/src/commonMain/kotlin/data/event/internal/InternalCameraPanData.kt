import data.event.EventData
import kotlinx.serialization.Serializable

@Serializable
internal data class InternalCameraPanData(
    val panTo: Pair<Double, Double>? = null,
    val panSmooth: Boolean = true,
    val panBy : Boolean = false,
    val zoom : Double? = null,
    val zoomOnly : Boolean = false
) : EventData()