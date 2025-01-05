package data.animation

import AnimationData
import kotlinx.serialization.Serializable

@Serializable
internal class SequentialAnimationData : AnimationData() {
    var animations: List<AnimationData> = listOf()
}
