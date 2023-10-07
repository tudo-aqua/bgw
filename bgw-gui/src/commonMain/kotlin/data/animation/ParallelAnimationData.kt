
package data.animation

import AnimationData
import kotlinx.serialization.Serializable

@Serializable
class ParallelAnimationData() : AnimationData() {
    var animations: List<AnimationData> = listOf()
}
