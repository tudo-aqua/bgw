package data.animation

import ComponentViewData
import kotlinx.serialization.Serializable

@Serializable
class ScaleAnimationData() : ComponentAnimationData() {
    var byScaleX = 0.0
    var byScaleY = 0.0
}
