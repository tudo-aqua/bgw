package data.animation

import AnimationData
import kotlinx.serialization.Serializable

@Serializable
class SequentialAnimationData() : AnimationData() {
    val animations: List<AnimationData> = listOf()
}
