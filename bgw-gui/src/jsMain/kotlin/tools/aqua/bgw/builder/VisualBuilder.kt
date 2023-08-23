package tools.aqua.bgw.builder

import ColorVisualData
import ImageVisualData
import VisualData
import react.ReactElement
import react.create
import react.dom.html.ReactHTML.div
import tools.aqua.bgw.elements.visual.ColorVisual as ReactColorVisual
import tools.aqua.bgw.elements.visual.ImageVisual as ReactImageVisual

object VisualBuilder {

    fun build(visual: VisualData?): ReactElement<*> {
        println("VisualBuilder.build(visual: $visual)")
        when(visual) {
            is ColorVisualData -> {
                println("ColorVisualData: ${visual.id} -> ${visual.color}")
                return ReactColorVisual.create {
                    data = visual
                }
            }
            is ImageVisualData -> {
                return ReactImageVisual.create {
                    data = visual
                }
            }
            // TODO - Compound / Text Visual
            else -> {
                return div.create()
            }
        }
    }
}