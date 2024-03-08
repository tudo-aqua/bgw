package tools.aqua.bgw

import AnimationData
import DiceViewData
import ID
import data.animation.*
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.dom.createElement
import kotlinx.js.timers.clearInterval
import kotlinx.js.timers.setInterval
import kotlinx.js.timers.setTimeout
import org.w3c.dom.Element
import org.w3c.dom.get
import react.dom.render
import tools.aqua.bgw.builder.VisualBuilder
import kotlin.js.Date

class Animator {
    private val animations = mutableMapOf<String, Element>()

    companion object {
        const val DELTA_MS = 20
    }

    fun startAnimation(
        animationData: AnimationData,
        parallelAnimations : List<AnimationData> = listOf(),
        callback: (ID) -> Unit
    ) {
        when(animationData) {
            is ComponentAnimationData -> {
                when(animationData) {
                    is FadeAnimationData -> startComponentAnimation("fade", animationData, parallelAnimations, callback)
                    is MovementAnimationData -> startComponentAnimation("move", animationData, parallelAnimations, callback)
                    is RotationAnimationData -> startComponentAnimation("rotate", animationData, parallelAnimations, callback)
                    is ScaleAnimationData -> startComponentAnimation("scale", animationData, parallelAnimations, callback)
                    is FlipAnimationData -> startFlipAnimation(animationData, callback)
                    // is ShakeAnimation<*> -> TODO()

                    is SteppedComponentAnimationData -> {
                        when(animationData) {
                            is RandomizeAnimationData -> startRandomizeAnimation(animationData, callback)
                            is DiceAnimationData -> startDiceAnimation(animationData, callback)
                            else -> throw IllegalArgumentException("Unknown animation type")
                        }
                    }
                    else -> throw IllegalArgumentException("Unknown animation type")
                }
            }

            is DelayAnimationData -> startDelayAnimation(animationData, callback)
            is ParallelAnimationData -> startParallelAnimation(animationData, callback)
            is SequentialAnimationData -> startSequentialAnimation(animationData, callback)

            else -> throw IllegalArgumentException("Unknown animation type")
        }
    }

    private fun clearComponentAnimations(componentId: ID, types : List<String> = mutableListOf()) {
        if(types.isEmpty()) {
            this.animations.keys.filter { it.startsWith(componentId) }.forEach {
                try {
                    document.body?.removeChild(this.animations[it]!!)
                    this.animations.remove(it)
                } catch (_: Exception) { }
            }
        } else {
            types.forEach {
                try {
                    document.body?.removeChild(this.animations["$componentId--$it"]!!)
                    this.animations.remove("$componentId--$it")
                } catch (_: Exception) { }
            }
        }
    }

    private fun startDelayAnimation(animation: DelayAnimationData, callback: (ID) -> Unit) {
        setTimeout({
            callback.invoke(animation.id)
        }, animation.duration)
    }

    private fun startSequentialAnimation(animation: SequentialAnimationData, callback: (ID) -> Unit) {
        val animations = animation.animations

        val component = animations[0] as? ComponentAnimationData ?: return
        val componentId = component.componentView?.id.toString()

        clearComponentAnimations(componentId)

        var currentDuration = 0
        for (anim in animations) {
            setTimeout({
                startAnimation(anim, animations, callback=callback)
            }, currentDuration)
            currentDuration += anim.duration
            if(anim == animations.last()) {
                val totalDuration = currentDuration + DELTA_MS
                setTimeout({ callback.invoke(animation.id) }, totalDuration)
            }
        }
    }

    private fun startParallelAnimation(animation: ParallelAnimationData, callback: (ID) -> Unit) {
        val animations = animation.animations

        val component = animations[0] as? ComponentAnimationData ?: return
        val componentId = component.componentView?.id.toString()

        clearComponentAnimations(componentId)

        for (anim in animations) {
            startAnimation(anim, animations, callback)
            if(anim == animations.last()) {
                val maxDuration = animations.maxOfOrNull { it.duration } ?: 0
                setTimeout({ callback.invoke(animation.id) }, maxDuration + DELTA_MS)
            }
        }
    }

    private fun startComponentAnimation(type : String, animation: ComponentAnimationData, parallelAnimation : List<AnimationData> = listOf(), callback: (ID) -> Unit) {
        // Get animation properties from data
        val componentId = animation.componentView?.id.toString()
        println("Starting $type Animation on ${componentId}")
        val duration = animation.duration

        // Get matching component element
        val element = document.getElementById(componentId) ?: return

        // Get old style element (if exists) and remove it
        clearComponentAnimations(componentId, listOf(type))

        // Toggle old animation off
        element.classList.toggle("${componentId}--$type--props", false)

        setTimeout({
            // Create new style element
            val newElement = document.createElement("style")
            newElement.id = "${componentId}--$type"

            // Add new style element to body
            newElement.innerHTML = getAnimationCSS(type, componentId, animation, parallelAnimation)
            document.body?.appendChild(newElement)

            // Toggle new animation on and save style element
            element.classList.toggle("${componentId}--$type--props", true)
            element.classList.toggle("${componentId}--$type", true)
            animations["$componentId--$type"] = newElement

            setTimeout({
                // Toggle new animation off
                element.classList.toggle("${componentId}--$type--props", false)
                callback.invoke(animation.id)
            }, duration)
        }, 50)
    }

    private fun startFlipAnimation(animation: FlipAnimationData, callback: (ID) -> Unit) {
        val type = "flip"
        // Get animation properties from data
        val componentId = animation.componentView?.id.toString()
        println("Starting $type Animation on ${componentId}")
        val duration = animation.duration

        // Get matching component element
        val element = document.getElementById(componentId) ?: return

        // Get old style element (if exists) and remove it
        clearComponentAnimations(componentId, listOf(type))

        // Toggle old animation off
        element.classList.toggle("${componentId}--$type--props", false)

        setTimeout({
            // Create new style element
            val newElement = document.createElement("style")
            newElement.id = "${componentId}--$type"

            // Add new style element to body
            newElement.innerHTML = getAnimationCSS(type, componentId, animation)
            document.body?.appendChild(newElement)

            // Toggle new animation on and save style element
            element.classList.toggle("${componentId}--$type--props", true)
            element.classList.toggle("${componentId}--$type", true)
            animations["$componentId--$type"] = newElement

            val oldVisuals = document.querySelector("#${componentId} > bgw_visuals")
            if (oldVisuals != null) {
                println("Rendering start visual for flip")
                render(VisualBuilder.build(animation.fromVisual), oldVisuals)
            }

            setTimeout({
                val oldVisuals = document.querySelector("#${componentId} > bgw_visuals")
                if (oldVisuals != null) {
                    println("Rendering end visual for flip")
                    render(VisualBuilder.build(animation.toVisual), oldVisuals)
                }
            }, duration / 2)

            setTimeout({
                // Toggle new animation off
                element.classList.toggle("${componentId}--$type--props", false)
                callback.invoke(animation.id)
            }, duration)
        }, 50)
    }

    private fun startRandomizeAnimation(animation: RandomizeAnimationData, callback: (ID) -> Unit) {
        val type = "random"
        // Get animation properties from data
        val componentId = animation.componentView?.id.toString()
        println("Starting $type Animation on ${componentId}")
        val duration = animation.duration

        val interval = setInterval({
            val oldVisuals = document.querySelector("#${componentId} > bgw_visuals")
            if (oldVisuals != null) {
                println("Rendering new visual for random")
                render(VisualBuilder.build(animation.visuals.random()), oldVisuals)
            }
        }, duration / animation.speed)

        setTimeout({
            clearInterval(interval)
            val oldVisuals = document.querySelector("#${componentId} > bgw_visuals")
            if (oldVisuals != null) {
                println("Rendering end visual for random")
                render(VisualBuilder.build(animation.toVisual), oldVisuals)
            }
            println("Stopping $type Animation on ${componentId}")
            callback.invoke(animation.id)
        }, duration)
    }

    private fun startDiceAnimation(animation: DiceAnimationData, callback: (ID) -> Unit) {
        val type = "dice"
        // Get animation properties from data
        val componentId = animation.componentView?.id.toString()
        val dice = animation.componentView as? DiceViewData ?: return
        println("Starting $type Animation on ${componentId}")
        val duration = animation.duration

        val interval = setInterval({
            val oldVisuals = document.querySelector("#${componentId} > bgw_visuals")
            if (oldVisuals != null) {
                println("Rendering new visual for dice")
                render(VisualBuilder.build(dice.visuals.random()), oldVisuals)
            }
        }, duration / animation.speed)

        setTimeout({
            clearInterval(interval)
            val oldVisuals = document.querySelector("#${componentId} > bgw_visuals")
            if (oldVisuals != null) {
                println("Rendering end visual for dice")
                render(VisualBuilder.build(dice.visuals[animation.toSide]), oldVisuals)
            }
            println("Stopping $type Animation on ${componentId}")
            callback.invoke(animation.id)
        }, duration)
    }

    private fun getTransitionCSS(animationList : List<AnimationData>) : String {
        val transitions = animationList.map {
            when(it) {
                is FadeAnimationData -> "opacity ${it.duration}ms ease-in-out"
                is MovementAnimationData -> "translate ${it.duration}ms ease-in-out"
                is RotationAnimationData -> "rotate ${it.duration}ms ease-in-out"
                is ScaleAnimationData -> "scale ${it.duration}ms ease-in-out"
                else -> ""
            }
        }.joinToString(", ")

        return """
            transition: $transitions;
        """.trimIndent()
    }

    private fun getAnimationCSS(type : String, componentId : String, animationData: AnimationData, parallelAnimations : List<AnimationData> = listOf()) : String {
        return when(animationData) {
            is FadeAnimationData -> """
                .${componentId}--${type}--props {
                    ${getTransitionCSS(parallelAnimations)}
                }
                
                .${componentId}--${type} {
                    opacity: ${animationData.toOpacity};
                }
            """.trimIndent()

            is MovementAnimationData -> """
                .${componentId}--${type}--props {
                    ${getTransitionCSS(parallelAnimations)}
                }
                
                .${componentId}--${type} {
                    translate: ${animationData.byX}rem ${animationData.byY}rem;
                }
            """.trimIndent()

            is RotationAnimationData -> """
                .${componentId}--${type}--props {
                    ${getTransitionCSS(parallelAnimations)}
                }
                
                .${componentId}--${type} {
                    rotate: ${animationData.byAngle}deg;
                }
            """.trimIndent()

            is ScaleAnimationData -> """
                .${componentId}--${type}--props {
                    ${getTransitionCSS(parallelAnimations)}
                }
                
                .${componentId}--${type} {
                    scale: ${animationData.byScaleX} ${animationData.byScaleY};
                }
            """.trimIndent()

            is FlipAnimationData -> """                
                .${componentId}--${type}--props {
                    animation: ${componentId}--${type}--flip ${animationData.duration}ms linear;
                }
                
                @keyframes ${componentId}--${type}--flip {
                    0% {
                        transform: rotateY(0deg);
                    }
                    50% {
                        transform: rotateY(90deg);
                    }
                    100% {
                        transform: rotateY(0deg);
                    }
                }
            """.trimIndent()

            else -> ""
        }
    }
}