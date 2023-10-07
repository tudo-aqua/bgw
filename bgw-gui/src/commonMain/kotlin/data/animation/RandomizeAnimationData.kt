
package data.animation


import VisualData
import kotlinx.serialization.Serializable
import tools.aqua.bgw.core.DEFAULT_ANIMATION_DURATION
import tools.aqua.bgw.core.DEFAULT_ANIMATION_SPEED
import tools.aqua.bgw.visual.Visual
@Serializable
class RandomizeAnimationData() : SteppedComponentAnimationData() {
    val visuals: List<VisualData> = listOf()
    val toVisual: VisualData? = null
}
