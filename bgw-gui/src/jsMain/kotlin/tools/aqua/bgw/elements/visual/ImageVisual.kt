package tools.aqua.bgw.elements.visual

import ImageVisualData
import csstype.*
import emotion.react.css
import org.w3c.dom.HTMLDivElement
import react.FC
import react.IntrinsicType
import react.Props
import react.dom.html.HTMLAttributes

external interface ImageVisualProps : Props {
    var data: ImageVisualData
}

val ImageVisual = FC<ImageVisualProps> { props ->
    bgwImageVisual {
        id = props.data.id

        css {
            backgroundImage = url(props.data.path)
            backgroundSize = BackgroundSize.cover
            backgroundRepeat = BackgroundRepeat.noRepeat
            backgroundPosition = BackgroundPosition.center
            opacity = number(props.data.transparency)
            // TODO...
        }
    }
}

inline val bgwImageVisual: IntrinsicType<HTMLAttributes<HTMLDivElement>>
    get() = "bgw_image_visual".unsafeCast<IntrinsicType<HTMLAttributes<HTMLDivElement>>>()