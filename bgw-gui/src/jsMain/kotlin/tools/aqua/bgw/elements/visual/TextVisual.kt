package tools.aqua.bgw.elements.visual

import ColorVisualData
import TextVisualData
import csstype.*
import emotion.react.css
import kotlinx.browser.document
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import react.*
import react.dom.html.HTMLAttributes
import tools.aqua.bgw.elements.filterBuilder
import tools.aqua.bgw.elements.flipBuilder
import tools.aqua.bgw.elements.styleBuilder
import tools.aqua.bgw.handlers

external interface TextVisualProps : Props {
    var data: TextVisualData
}

val TextVisual = FC<TextVisualProps> { props ->
    val (data, setData) = useState(props.data)

    useEffect {
        handlers[props.data.id] = { newData ->
            if(newData is TextVisualData) {
                println("Updating TextVisual ${props.data.id}")
                setData(newData)
            }
        }
    }

    bgwTextVisual {
        id = data.id
        css {
            styleBuilder(data.style)
            flipBuilder(data.flipped)
            filterBuilder(data.filters)
            fontFamily = (data.font?.family ?: "Arial") as FontFamily?
            fontWeight = (data.font?.fontWeight ?: "normal") as FontWeight?
            fontStyle = (data.font?.fontStyle ?: "normal") as FontStyle?
            fontSize = data.font?.size?.rem
            color = Color(data.font?.color ?: "black")
            justifyContent = when(data.alignment.first) {
                "left" -> JustifyContent.flexStart
                "center" -> JustifyContent.center
                "right" -> JustifyContent.flexEnd
                else -> JustifyContent.center
            }
            alignItems = when(data.alignment.second) {
                "top" -> AlignItems.flexStart
                "center" -> AlignItems.center
                "bottom" -> AlignItems.flexEnd
                else -> AlignItems.center
            }
        }
        +data.text
    }
}

inline val bgwTextVisual: IntrinsicType<HTMLAttributes<HTMLDivElement>>
    get() = "bgw_text_visual".unsafeCast<IntrinsicType<HTMLAttributes<HTMLDivElement>>>()