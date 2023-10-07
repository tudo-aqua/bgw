package tools.aqua.bgw

import ID
import data.animation.AnimationData
import data.animation.FadeAnimationData
import tools.aqua.bgw.animation.*
import tools.aqua.bgw.components.ComponentView

class Animator {
    fun startAnimation(animation: AnimationData) {
        when(animation) {
            is FadeAnimationData -> startFadeAnimation(animation)
        }
    }

    private fun startFadeAnimation(animation: FadeAnimationData) {
    }
}