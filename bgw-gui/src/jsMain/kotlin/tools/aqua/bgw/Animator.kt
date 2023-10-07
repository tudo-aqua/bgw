package tools.aqua.bgw

import AnimationData
import ID
import data.animation.FadeAnimationData
import kotlinx.browser.document
import kotlinx.js.timers.setTimeout
import org.w3c.dom.Element
import tools.aqua.bgw.animation.*
import tools.aqua.bgw.components.ComponentView

class Animator {
    private val animations = mutableMapOf<String, Element>()

    fun startAnimation(animation: AnimationData) {
        when(animation) {
            is FadeAnimationData -> startFadeAnimation(animation)
        }
    }

    private fun startFadeAnimation(animation: FadeAnimationData) {
        // Get animation properties from data
        val componentId = animation.componentView?.id.toString()
        println("Starting Fade Animation on ${componentId}")
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
        element.classList.toggle("${componentId}--fade--props", false)

        setTimeout({
            // Create new style element
            val newElement = document.createElement("style")
            newElement.id = "${componentId}--fade"

            // Add new style element to body
            newElement.innerHTML = """
                .${componentId}--fade--props {
                    transition: ${duration}ms opacity ease-in-out;
                }
                
                .${componentId}--fade {
                    opacity: ${animation.toOpacity};
                }
            """.trimIndent()
            document.body?.appendChild(newElement)

            // Toggle new animation on and save style element
            element.classList.toggle("${componentId}--fade--props", true)
            element.classList.toggle("${componentId}--fade", true)
            animations[componentId] = newElement

            setTimeout({
                // Toggle new animation off
                element.classList.toggle("${componentId}--fade--props", false)
            }, duration)
        }, 50)
    }

    private fun startAnimation(type : String) {

    }

    /*
        Animation Blueprint
        ===================
        .${componentId}--move--props {
            transition: ${duration}ms translate;
        }

        .${componentId}--move {
            translate: ${byX}px ${byY}px;
        }
    */
}