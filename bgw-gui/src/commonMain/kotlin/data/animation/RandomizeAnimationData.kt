
package data.animation


import VisualData
import kotlinx.serialization.Serializable
@Serializable
class RandomizeAnimationData() : SteppedComponentAnimationData() {
    val visuals: List<VisualData> = listOf()
    val toVisual: VisualData? = null
}
