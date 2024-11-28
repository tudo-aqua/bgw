
package data.animation

import AnimationData
import kotlinx.serialization.Serializable

@Serializable
internal class ParallelAnimationData() : AnimationData() {
    var animations: List<AnimationData> = listOf()
}
