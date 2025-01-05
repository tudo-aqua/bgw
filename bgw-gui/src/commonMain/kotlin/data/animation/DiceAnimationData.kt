package data.animation

import kotlinx.serialization.Serializable

@Serializable
internal class DiceAnimationData : SteppedComponentAnimationData() {
    var toSide: Int = 0
}
