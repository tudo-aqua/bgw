package data.animation

import AnimationData
import kotlinx.serialization.Serializable

@Serializable
class SequentialAnimationData() : AnimationData() {
    var animations: List<AnimationData> = listOf()
}
