package data.animation

import kotlinx.serialization.Serializable

@Serializable
internal class ScaleAnimationData : ComponentAnimationData() {
    var byScaleX = 0.0
    var byScaleY = 0.0
}
