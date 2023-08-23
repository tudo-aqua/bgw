package tools.aqua.bgw.elements.visual

import ColorVisualData
import csstype.ClassName
import csstype.Color
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML.data
import react.dom.html.ReactHTML.datalist
import react.dom.html.ReactHTML.span

external interface ColorVisualProps : Props {
    var data: ColorVisualData
}

val ColorVisual = FC<ColorVisualProps> { props ->
    span {
        id = props.data.id
        className = ClassName("visual colorVisual")

        css {
            backgroundColor = Color(props.data.color)
            // TODO...
        }
    }
}