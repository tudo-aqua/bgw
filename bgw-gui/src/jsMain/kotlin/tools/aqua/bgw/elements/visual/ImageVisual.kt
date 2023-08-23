package tools.aqua.bgw.elements.visual

import ImageVisualData
import csstype.*
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.ReactHTML.data
import react.dom.html.ReactHTML.span

external interface ImageVisualProps : Props {
    var data: ImageVisualData
}

val ImageVisual = FC<ImageVisualProps> { props ->
    span {
        id = props.data.id
        className = ClassName("visual imageVisual")

        css {
            backgroundImage = url(props.data.path)
            backgroundSize = BackgroundSize.cover
            backgroundRepeat = BackgroundRepeat.noRepeat
            backgroundPosition = BackgroundPosition.center
            // TODO...
        }
    }
}