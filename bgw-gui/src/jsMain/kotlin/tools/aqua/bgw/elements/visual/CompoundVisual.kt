package tools.aqua.bgw.elements.visual

import CompoundVisualData
import web.cssom.ClassName
import react.*
import react.dom.flushSync
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.bgwVisuals
import tools.aqua.bgw.handlers

external interface CompoundVisualProps : Props {
    var data: CompoundVisualData
}

val CompoundVisual = FC<CompoundVisualProps> { props ->
    props.data.children.forEach {
        +VisualBuilder.build(it)
    }
}