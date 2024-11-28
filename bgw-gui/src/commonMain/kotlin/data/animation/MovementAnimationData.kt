package data.animation

import ComponentViewData
import kotlinx.serialization.Serializable
@Serializable
internal class MovementAnimationData() : ComponentAnimationData() {
  var byX : Int = 0
  var byY : Int = 0
}
