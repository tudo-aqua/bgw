
package data.animation

import AnimationData
import ComponentViewData
import kotlinx.serialization.Serializable


@Serializable
abstract class ComponentAnimationData() : AnimationData() {
    var componentView: ComponentViewData? = null
}
