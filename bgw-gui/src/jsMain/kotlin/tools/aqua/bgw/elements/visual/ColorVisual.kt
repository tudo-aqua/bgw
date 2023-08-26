package tools.aqua.bgw.elements.visual

import ColorVisualData
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
import tools.aqua.bgw.elements.styleBuilder

external interface ColorVisualProps : Props {
    var data: ColorVisualData
}

val ColorVisual = FC<ColorVisualProps> { props ->
    bgwColorVisual {
        id = props.data.id
        css {
            styleBuilder(props.data.style)
            backgroundColor = Color(props.data.color)
            opacity = number(props.data.transparency)
        }
    }
}

inline val bgwColorVisual: IntrinsicType<HTMLAttributes<HTMLDivElement>>
    get() = "bgw_color_visual".unsafeCast<IntrinsicType<HTMLAttributes<HTMLDivElement>>>()