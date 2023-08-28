package tools.aqua.bgw.elements.visual

import CompoundVisualData
import csstype.ClassName
import kotlinx.js.Object
import react.*
import react.dom.flushSync
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.bgwVisuals
import tools.aqua.bgw.handlers

external interface CompoundVisualProps : Props {
    var data: CompoundVisualData
}

val CompoundVisual = FC<CompoundVisualProps> { props ->
    var (data, setData) = useState(props.data)

    useEffect {
        setData(props.data)
        handlers[props.data.id] = { newData ->
            if (newData is CompoundVisualData) {
                println("Updating CompoundVisual ${props.data.id}")
                data = newData
            }
        }
    }

    bgwVisuals {
        className = ClassName("visuals")
        data.children.forEach {
            +VisualBuilder.build(it)
        }
    }
}