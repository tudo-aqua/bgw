package tools.aqua.bgw

import AnimationData
import ID
import data.animation.*
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.dom.createElement
import kotlinx.js.timers.setTimeout
import org.w3c.dom.Element
import org.w3c.dom.get
import react.dom.render
import tools.aqua.bgw.builder.VisualBuilder

class Animator {
    private val animations = mutableMapOf<String, Element>()

    fun startAnimation(animationData: AnimationData) {
        when(animationData) {
            is ComponentAnimationData -> {
                when(animationData) {
                    is FadeAnimationData -> startComponentAnimation("fade", animationData)
                    is MovementAnimationData -> startComponentAnimation("move", animationData)
                    is RotationAnimationData -> startComponentAnimation("rotate", animationData)
                    is ScaleAnimationData -> startComponentAnimation("scale", animationData)
                    is FlipAnimationData -> startFlipAnimation(animationData)
                    // is ShakeAnimation<*> -> TODO()

                    is SteppedComponentAnimationData -> {

                    }
                    else -> throw IllegalArgumentException("Unknown animation type")
                }
            }

            /*is DelayAnimation -> DelayAnimationData().fillData(animation)
            is ParallelAnimation -> ParallelAnimationData().fillData(animation)
            is SequentialAnimation -> SequentialAnimationData().fillData(animation)*/

            else -> throw IllegalArgumentException("Unknown animation type")
        }
    }

    private fun startComponentAnimation(type : String, animation: ComponentAnimationData) {
        // Get animation properties from data
        val componentId = animation.componentView?.id.toString()
        println("Starting $type Animation on ${componentId}")
        val duration = animation.duration

        // Get matching component element
        val element = document.getElementById(componentId) ?: return

        // Get old style element (if exists) and remove it
        try {
            val oldElement = animations[componentId]
            if(oldElement != null)
                document.body?.removeChild(oldElement)
        } catch (_: Exception) { }
        animations.remove(componentId)

        // Toggle old animation off
        element.classList.toggle("${componentId}--$type--props", false)

        if(type == "scale") {
            window.open("https://www.youtube.com/watch?v=dQw4w9WgXcQ", "_blank")
        }

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
            animations[componentId] = newElement

            setTimeout({
                // Toggle new animation off
                element.classList.toggle("${componentId}--$type--props", false)
            }, duration)
        }, 50)
    }


    private fun startFlipAnimation(animation: FlipAnimationData) {
        val type = "flip"
        // Get animation properties from data
        val componentId = animation.componentView?.id.toString()
        println("Starting $type Animation on ${componentId}")
        val duration = animation.duration

        // Get matching component element
        val element = document.getElementById(componentId) ?: return

        // Get old style element (if exists) and remove it
        try {
            val oldElement = animations[componentId]
            if(oldElement != null)
                document.body?.removeChild(oldElement)
        } catch (_: Exception) { }
        animations.remove(componentId)

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
            animations[componentId] = newElement

            setTimeout({
                val oldVisuals = document.querySelector("#${componentId} > bgw_visuals")
                val tempObj = document.createElement("div")
                if (oldVisuals != null) {
                    println("Rendering new visual for flip")
                    render(VisualBuilder.build(animation.toVisual), tempObj)
                    oldVisuals.replaceWith(tempObj.children[0])
                }
            }, duration / 2)

            setTimeout({
                // Toggle new animation off
                element.classList.toggle("${componentId}--$type--props", false)
            }, duration)
        }, 50)
    }


    private fun getAnimationCSS(type : String, componentId : String, animationData: AnimationData) : String {
        return when(animationData) {
            is FadeAnimationData -> """
                .${componentId}--${type}--props {
                    transition: ${animationData.duration}ms opacity ease-in-out;
                }
                
                .${componentId}--${type} {
                    opacity: ${animationData.toOpacity};
                }
            """.trimIndent()

            is MovementAnimationData -> """
                .${componentId}--${type}--props {
                    transition: ${animationData.duration}ms translate ease-in-out;
                }
                
                .${componentId}--${type} {
                    translate: ${animationData.byX}rem ${animationData.byY}rem;
                }
            """.trimIndent()

            is RotationAnimationData -> """
                .${componentId}--${type}--props {
                    transition: ${animationData.duration}ms rotate ease-in-out;
                }
                
                .${componentId}--${type} {
                    rotate: ${animationData.byAngle}deg;
                }
            """.trimIndent()

            is ScaleAnimationData -> """
                .${componentId}--${type}--props {
                    transition: ${animationData.duration}ms scale ease-in-out;
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