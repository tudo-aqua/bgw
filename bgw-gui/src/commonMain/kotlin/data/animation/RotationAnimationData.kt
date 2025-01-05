package data.animation

import kotlinx.serialization.Serializable

@Serializable
internal class RotationAnimationData : ComponentAnimationData() {
    var byAngle = 0.0
}
