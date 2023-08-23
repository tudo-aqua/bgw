package tools.aqua.bgw.elements.visual

import ColorVisualData
import csstype.Color
import emotion.react.css
import kotlinx.browser.document
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import react.FC
import react.IntrinsicType
import react.Props
import react.dom.html.HTMLAttributes
import react.useEffect

external interface ColorVisualProps : Props {
    var data: ColorVisualData
}

val ColorVisual = FC<ColorVisualProps> { props ->
    colorVisual {
        id = props.data.id
        css {
            backgroundColor = Color(props.data.color)
            // TODO...
        }
    }
}

inline val colorVisual: IntrinsicType<HTMLAttributes<HTMLDivElement>>
    get() = "colorvisual".unsafeCast<IntrinsicType<HTMLAttributes<HTMLDivElement>>>()