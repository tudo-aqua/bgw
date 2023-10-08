
package data.animation

import GameComponentViewData
import VisualData
import kotlinx.serialization.Serializable

@Serializable
class FlipAnimationData() : ComponentAnimationData() {
    var gameComponentView: GameComponentViewData? = componentView as? GameComponentViewData
    val fromVisual: VisualData? = null
    val toVisual: VisualData? = null
}
