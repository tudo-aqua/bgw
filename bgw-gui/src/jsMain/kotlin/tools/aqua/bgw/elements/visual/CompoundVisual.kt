package tools.aqua.bgw.elements.visual

import CompoundVisualData
import react.*
import tools.aqua.bgw.builder.VisualBuilder

internal external interface CompoundVisualProps : Props {
    var data: CompoundVisualData
}

internal val CompoundVisual = FC<CompoundVisualProps> { props ->
    props.data.children.forEach {
        +VisualBuilder.build(it)
    }
}