package tools.aqua.bgw.elements.visual

import ColorVisualData
import csstype.Color
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.ReactHTML.data
import react.dom.html.ReactHTML.span

external interface ColorVisualProps : Props {
    var data: ColorVisualData
}

val ColorVisual = FC<ColorVisualProps> { props ->
    span {
        id = props.data.id
        data {
            "type" to "visual"
            "visual" to "color"
        }

        css {
            backgroundColor = Color(props.data.color)
            // TODO...
        }
    }
}