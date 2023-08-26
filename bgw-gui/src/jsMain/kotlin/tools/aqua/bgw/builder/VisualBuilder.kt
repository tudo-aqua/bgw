package tools.aqua.bgw.builder

import ColorVisualData
import CompoundVisualData
import ImageVisualData
import TextVisualData
import VisualData
import react.ReactElement
import react.create
import react.dom.html.ReactHTML.div
import tools.aqua.bgw.elements.visual.ColorVisual as ReactColorVisual
import tools.aqua.bgw.elements.visual.ImageVisual as ReactImageVisual
import tools.aqua.bgw.elements.visual.TextVisual as ReactTextVisual

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

            is TextVisualData -> {
                return listOf(ReactTextVisual.create {
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

            else -> {
                return listOf(div.create())
            }
        }
    }
}