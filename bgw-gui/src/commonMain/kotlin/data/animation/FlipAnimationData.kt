package data.animation

import VisualData
import kotlinx.serialization.Serializable

@Serializable
internal class FlipAnimationData : ComponentAnimationData() {
    var fromVisual: VisualData? = null
    var toVisual: VisualData? = null
}
