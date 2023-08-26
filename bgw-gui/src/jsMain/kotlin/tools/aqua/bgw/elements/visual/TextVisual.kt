package tools.aqua.bgw.elements.visual

import ColorVisualData
import TextVisualData
import csstype.*
import emotion.react.css
import kotlinx.browser.document
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import react.FC
import react.IntrinsicType
import react.Props
import react.dom.html.HTMLAttributes
import react.useEffect

external interface TextVisualProps : Props {
    var data: TextVisualData
}

val TextVisual = FC<TextVisualProps> { props ->
    bgwTextVisual {
        id = props.data.id
        css {
            fontFamily = (props.data.font?.family ?: "Arial") as FontFamily?
            fontWeight = (props.data.font?.fontWeight ?: "normal") as FontWeight?
            fontStyle = (props.data.font?.fontStyle ?: "normal") as FontStyle?
            fontSize = props.data.font?.size?.rem
            color = Color(props.data.font?.color ?: "black")
            // TODO: text alignment
        }
        +props.data.text
    }
}

inline val bgwTextVisual: IntrinsicType<HTMLAttributes<HTMLDivElement>>
    get() = "bgw_text_visual".unsafeCast<IntrinsicType<HTMLAttributes<HTMLDivElement>>>()