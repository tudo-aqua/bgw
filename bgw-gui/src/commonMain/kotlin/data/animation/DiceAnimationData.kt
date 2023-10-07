package data.animation

import DiceViewData
import kotlinx.serialization.Serializable
import tools.aqua.bgw.components.gamecomponentviews.DiceView
import tools.aqua.bgw.core.DEFAULT_ANIMATION_DURATION
import tools.aqua.bgw.core.DEFAULT_ANIMATION_SPEED

@Serializable
class DiceAnimationData() : SteppedComponentAnimationData() {
    var dice: DiceViewData? = componentView as? DiceViewData
    var toSide: Int = 0
}
