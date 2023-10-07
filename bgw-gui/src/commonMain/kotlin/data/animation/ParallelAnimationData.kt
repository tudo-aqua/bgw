
package data.animation

import kotlinx.serialization.Serializable

@Serializable
class ParallelAnimationData() : AnimationData() {
    var animations: List<AnimationData> = listOf()
}
