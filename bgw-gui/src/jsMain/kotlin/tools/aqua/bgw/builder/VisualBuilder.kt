package tools.aqua.bgw.builder

import ColorVisualData
import CompoundVisualData
import ImageVisualData
import VisualData
import react.ReactElement
import react.create
import react.dom.html.ReactHTML.div
import tools.aqua.bgw.elements.visual.ColorVisual as ReactColorVisual
import tools.aqua.bgw.elements.visual.ImageVisual as ReactImageVisual

object VisualBuilder {

    fun build(visual: VisualData?): List<ReactElement<*>> {
        println("VisualBuilder.build(visual: $visual)")
        when(visual) {
            is ColorVisualData -> {
                println("ColorVisualData: ${visual.id} -> ${visual.color}")
                return listOf(ReactColorVisual.create {
                    data = visual
                })
            }

            is ImageVisualData -> {
                return listOf(ReactImageVisual.create {
                    data = visual
                })
            }

            is CompoundVisualData -> {
                val visuals = mutableListOf<ReactElement<*>>()
                visual.children.forEach {
                    visuals.addAll(build(it))
                }
                return visuals
            }

            // TODO - Text Visual
            else -> {
                return listOf(div.create())
            }
        }
    }
}