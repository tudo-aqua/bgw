package data.animation

import data.event.AnimationFinishedEventData
import kotlinx.serialization.Serializable
import tools.aqua.bgw.core.DEFAULT_ANIMATION_DURATION

@Serializable
abstract class AnimationData() {
  var duration: Int = DEFAULT_ANIMATION_DURATION
  var isRunning: Boolean = false
  var onFinished: ((AnimationFinishedEventData) -> Unit)? = null
}
