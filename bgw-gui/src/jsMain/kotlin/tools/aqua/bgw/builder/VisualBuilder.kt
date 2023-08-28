package tools.aqua.bgw.builder

import ColorVisualData
import CompoundVisualData
import ImageVisualData
import TextVisualData
import VisualData
import react.*
import react.dom.html.ReactHTML.div
import tools.aqua.bgw.elements.visual.CompoundVisual
import tools.aqua.bgw.handlers
import tools.aqua.bgw.elements.visual.ColorVisual as ReactColorVisual
import tools.aqua.bgw.elements.visual.ImageVisual as ReactImageVisual
import tools.aqua.bgw.elements.visual.TextVisual as ReactTextVisual


external interface VisualProps : Props {
    var data: VisualData
}
object VisualBuilder {
    fun build(visual: VisualData?): ReactElement<*> {
        when(visual) {
            is ColorVisualData -> {
                return ReactColorVisual.create {
                    data = visual
                }
            }

            is ImageVisualData -> {
                return ReactImageVisual.create {
                    data = visual
                }
            }

            is TextVisualData -> {
                return ReactTextVisual.create {
                    data = visual
                }
            }
            is CompoundVisualData -> {
                //println("Building CompoundVisual ${visual.id}")
                return CompoundVisual.create {
                    data = visual
                }
            }
            else -> {
                return div.create()
            }
        }
    }
}