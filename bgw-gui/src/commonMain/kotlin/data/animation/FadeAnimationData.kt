package data.animation

import kotlinx.serialization.Serializable

@Serializable
internal class FadeAnimationData : ComponentAnimationData() {
    var fromOpacity: Double = componentView?.opacity ?: 0.0
    var toOpacity: Double = componentView?.opacity ?: 1.0
}
