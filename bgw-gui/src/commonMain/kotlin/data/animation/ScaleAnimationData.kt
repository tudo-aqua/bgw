package data.animation

import ComponentViewData
import kotlinx.serialization.Serializable
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.core.DEFAULT_ANIMATION_SPEED

@Serializable
class ScaleAnimationData() : ComponentAnimationData() {
    val fromScaleX: Double = 0.0
    val toScaleX: Double = 0.0
    val fromScaleY: Double = 0.0
    val toScaleY: Double = 0.0
}
