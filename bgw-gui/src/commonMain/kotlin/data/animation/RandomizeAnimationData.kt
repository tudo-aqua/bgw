
package data.animation


import VisualData
import kotlinx.serialization.Serializable
@Serializable
class RandomizeAnimationData() : SteppedComponentAnimationData() {
    var visuals: List<VisualData> = listOf()
    var toVisual: VisualData? = null
}
