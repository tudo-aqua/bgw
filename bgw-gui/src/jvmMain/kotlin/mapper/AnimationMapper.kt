package mapper

import AnimationData
import data.animation.*
import tools.aqua.bgw.animation.*

object AnimationMapper {
    fun AnimationData.fillData(animation: Animation) : AnimationData {
        return this.apply {
            duration = animation.duration
        }
    }

    fun ComponentAnimationData.fillData(componentAnimation: ComponentAnimation<*>) : ComponentAnimationData {
        return this.apply {
            componentView = ComponentMapper.map(componentAnimation.componentView)
            duration = componentAnimation.duration
        }
    }

    private fun mapSpecific(animation: Animation) : AnimationData {
        return when(animation) {
            is ComponentAnimation<*> -> {
                when(animation) {
                    is FadeAnimation<*> -> FadeAnimationData().fillData(animation)
                    is MovementAnimation<*> -> MovementAnimationData().fillData(animation)
                    is RotationAnimation<*> -> RotationAnimationData().fillData(animation)
                    is ScaleAnimation<*> -> ScaleAnimationData().fillData(animation)
                    is FlipAnimation<*> -> FlipAnimationData().fillData(animation)
                    // is ShakeAnimation<*> -> TODO()

                    is SteppedComponentAnimation<*> -> {
                        when(animation) {
                            is DiceAnimation<*> -> DiceAnimationData().fillData(animation) as SteppedComponentAnimationData
                            is RandomizeAnimation<*> -> RandomizeAnimationData().fillData(animation) as SteppedComponentAnimationData
                            else -> throw IllegalArgumentException("Unknown animation type: ${animation::class.simpleName}")
                        }
                    }
                    else -> throw IllegalArgumentException("Unknown animation type: ${animation::class.simpleName}")
                }
            }

            is DelayAnimation -> DelayAnimationData().fillData(animation)
            is ParallelAnimation -> ParallelAnimationData().fillData(animation)
            is SequentialAnimation -> SequentialAnimationData().fillData(animation)

            else -> throw IllegalArgumentException("Unknown animation type: ${animation::class.simpleName}")
        }
    }

    fun map(animation: Animation) : AnimationData {
        return when (animation) {
            is FadeAnimation<*> -> (mapSpecific(animation) as FadeAnimationData).apply {
                fromOpacity = animation.fromOpacity
                toOpacity = animation.toOpacity
            }

            is MovementAnimation<*> -> (mapSpecific(animation) as MovementAnimationData).apply {
                byX = (animation.toX - animation.fromX).toInt()
                byY = (animation.toY - animation.fromY).toInt()
            }

            is RotationAnimation<*> -> (mapSpecific(animation) as RotationAnimationData).apply {
                byAngle = animation.toAngle - animation.fromAngle
            }

            is ScaleAnimation<*> -> (mapSpecific(animation) as ScaleAnimationData).apply {
                byScaleX = animation.toScaleX
                byScaleY = animation.toScaleY
            }

            is FlipAnimation<*> -> (mapSpecific(animation) as FlipAnimationData).apply {
                fromVisual = VisualMapper.map(animation.fromVisual)
                toVisual = VisualMapper.map(animation.toVisual)
            }

            else -> TODO("Not implemented")
        }
    }
}