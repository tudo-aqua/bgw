
package data.animation

import ComponentViewData
import kotlinx.serialization.Serializable


@Serializable
abstract class ComponentAnimationData() : AnimationData() {
    var componentView: ComponentViewData? = null
}
