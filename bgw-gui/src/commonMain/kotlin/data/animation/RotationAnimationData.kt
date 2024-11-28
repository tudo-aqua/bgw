
package data.animation

import ComponentViewData
import kotlinx.serialization.Serializable

@Serializable
internal class RotationAnimationData() : ComponentAnimationData() {
  var byAngle = 0.0
}
