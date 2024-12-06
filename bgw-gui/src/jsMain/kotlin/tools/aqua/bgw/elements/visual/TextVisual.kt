package tools.aqua.bgw.elements.visual

import ColorVisualData
import TextVisualData
import web.cssom.*
import emotion.react.css
import kotlinx.browser.document
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import react.FC
import react.IntrinsicType
import react.Props
import react.dom.html.HTMLAttributes
import react.useEffect
import tools.aqua.bgw.elements.filterBuilder
import tools.aqua.bgw.elements.flipBuilder
import tools.aqua.bgw.elements.styleBuilder
import web.dom.Element

internal external interface TextVisualProps : Props {
    var data: TextVisualData
}

internal val TextVisual = FC<TextVisualProps> { props ->
    bgwTextVisual {
        id = props.data.id
        css {
            styleBuilder(props.data.style)
            flipBuilder(props.data.flipped)
            filterBuilder(props.data.filters)
            fontFamily = (props.data.font?.family ?: "Arial") as FontFamily?
            fontWeight = (props.data.font?.fontWeight ?: "normal") as FontWeight?
            fontStyle = (props.data.font?.fontStyle ?: "normal") as FontStyle?
            fontSize = props.data.font?.size?.em
            color = Color(props.data.font?.color ?: "black")
            justifyContent = when(props.data.alignment.first) {
                "left" -> JustifyContent.flexStart
                "center" -> JustifyContent.center
                "right" -> JustifyContent.flexEnd
                else -> JustifyContent.center
            }
            textAlign = when(props.data.alignment.first) {
                "left" -> TextAlign.left
                "center" -> TextAlign.center
                "right" -> TextAlign.right
                else -> TextAlign.center
            }
            alignItems = when(props.data.alignment.second) {
                "top" -> AlignItems.flexStart
                "center" -> AlignItems.center
                "bottom" -> AlignItems.flexEnd
                else -> AlignItems.center
            }

            left = props.data.offsetX.em
            top = props.data.offsetY.em
            opacity = number(props.data.transparency)
        }
        +props.data.text
    }
}

internal inline val bgwTextVisual: IntrinsicType<HTMLAttributes<Element>>
    get() = "bgw_text_visual".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()