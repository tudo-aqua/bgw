
package data.animation

import GameComponentViewData
import VisualData
import kotlinx.serialization.Serializable
import tools.aqua.bgw.visual.Visual

@Serializable
class FlipAnimationData() : ComponentAnimationData() {
    var gameComponentView: GameComponentViewData? = componentView as? GameComponentViewData
    val fromVisual: VisualData? = null
    val toVisual: VisualData? = null
}
