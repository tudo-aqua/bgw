package data.animation

import ComponentViewData
import kotlinx.serialization.Serializable
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.core.DEFAULT_ANIMATION_SPEED
import tools.aqua.bgw.core.Scene
import tools.aqua.bgw.util.Coordinate
@Serializable
class MovementAnimationData() : ComponentAnimationData() {
  var fromX: Double = 0.0
  var toX: Double = 0.0
  var fromY: Double = 0.0
  var toY: Double = 0.0
}
