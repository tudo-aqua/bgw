
package data.animation

import ComponentViewData
import kotlinx.serialization.Serializable
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.core.DEFAULT_ANIMATION_DURATION

@Serializable
class RotationAnimationData() : ComponentAnimationData() {
  val fromAngle: Double = 0.0
  val toAngle: Double = 0.0
}
