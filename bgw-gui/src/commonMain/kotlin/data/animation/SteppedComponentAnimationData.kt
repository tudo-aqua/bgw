package data.animation

import kotlinx.serialization.Serializable
import tools.aqua.bgw.core.DEFAULT_ANIMATION_SPEED

@Serializable
sealed class SteppedComponentAnimationData() : ComponentAnimationData() {
    var speed: Int = DEFAULT_ANIMATION_SPEED
}
