package data.animation

import DiceViewData
import kotlinx.serialization.Serializable

@Serializable
internal class DiceAnimationData() : SteppedComponentAnimationData() {
    var dice: DiceViewData? = componentView as? DiceViewData
    var toSide: Int = 0
}
