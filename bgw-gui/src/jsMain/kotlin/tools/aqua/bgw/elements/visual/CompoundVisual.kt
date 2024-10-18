package tools.aqua.bgw.elements.visual

import CompoundVisualData
import react.*
import tools.aqua.bgw.builder.VisualBuilder

external interface CompoundVisualProps : Props {
    var data: CompoundVisualData
}

val CompoundVisual = FC<CompoundVisualProps> { props ->
    props.data.children.forEach {
        +VisualBuilder.build(it)
    }
}