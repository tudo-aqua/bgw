
package data.animation

import ComponentViewData
import kotlinx.serialization.Serializable

@Serializable
class RotationAnimationData() : ComponentAnimationData() {
  val fromAngle: Double = 0.0
  val toAngle: Double = 0.0
}
