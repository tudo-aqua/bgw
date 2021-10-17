package tools.aqua.bgw.animation

data class SequentialAnimation(val animations: List<Animation>): Animation(animations.sumOf(Animation::duration))