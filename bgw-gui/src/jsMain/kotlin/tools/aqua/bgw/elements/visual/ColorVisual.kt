package tools.aqua.bgw.elements.visual

import ColorVisualData
import csstype.*
import emotion.react.css
import kotlinx.browser.document
import kotlinx.js.Object
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import react.*
import react.dom.html.HTMLAttributes
import tools.aqua.bgw.elements.filterBuilder
import tools.aqua.bgw.elements.flipBuilder
import tools.aqua.bgw.elements.styleBuilder
import tools.aqua.bgw.handlers

external interface ColorVisualProps : Props {
    var data: ColorVisualData
}

val ColorVisual = FC<ColorVisualProps> { props ->
    val (data, setData) = useState(props.data)

    useEffect {
        setData(props.data)
        handlers[props.data.id] = { newData ->
            if(newData is ColorVisualData) {
                println("Updating ColorVisual ${props.data.id}")
                setData(newData)
            }
        }
    }
    
    bgwColorVisual {
        id = data.id
        css {
            styleBuilder(data.style)
            flipBuilder(data.flipped)
            filterBuilder(data.filters)
            backgroundColor = data.color.unsafeCast<Color>()
            opacity = number(data.transparency)
        }
    }
}

inline val bgwColorVisual: IntrinsicType<HTMLAttributes<HTMLDivElement>>
    get() = "bgw_color_visual".unsafeCast<IntrinsicType<HTMLAttributes<HTMLDivElement>>>()