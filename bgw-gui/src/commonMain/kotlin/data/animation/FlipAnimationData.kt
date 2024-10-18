
package data.animation

import GameComponentViewData
import VisualData
import kotlinx.serialization.Serializable

@Serializable
class FlipAnimationData() : ComponentAnimationData() {
    var fromVisual: VisualData? = null
    var toVisual: VisualData? = null
}
