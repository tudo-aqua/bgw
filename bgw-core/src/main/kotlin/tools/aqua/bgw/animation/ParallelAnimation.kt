package tools.aqua.bgw.animation

data class ParallelAnimation(val animations: List<Animation>): Animation(animations.maxOf(Animation::duration))