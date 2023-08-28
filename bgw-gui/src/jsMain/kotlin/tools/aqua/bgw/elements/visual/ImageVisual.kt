package tools.aqua.bgw.elements.visual

import ImageVisualData
import csstype.*
import emotion.react.css
import kotlinx.js.Object
import org.w3c.dom.HTMLDivElement
import react.*
import react.dom.html.HTMLAttributes
import tools.aqua.bgw.elements.filterBuilder
import tools.aqua.bgw.elements.flipBuilder
import tools.aqua.bgw.elements.styleBuilder
import tools.aqua.bgw.handlers

external interface ImageVisualProps : Props {
    var data: ImageVisualData
}

val ImageVisual = FC<ImageVisualProps> { props ->
    val (data, setData) = useState(props.data)

    useEffect {
        setData(props.data)
        handlers[props.data.id] = { newData ->
            if(newData is ImageVisualData) {
                //println("Updating ImageVisual ${props.data.id}")
                setData(newData)
            }
        }
    }
    
    bgwImageVisual {
        id = data.id

        css {
            styleBuilder(data.style)
            flipBuilder(data.flipped)
            filterBuilder(data.filters)
            backgroundImage = url(data.path)
            backgroundSize = BackgroundSize.cover
            backgroundRepeat = BackgroundRepeat.noRepeat
            backgroundPosition = BackgroundPosition.center
            opacity = number(data.transparency)
            // TODO...
        }
    }
}

inline val bgwImageVisual: IntrinsicType<HTMLAttributes<HTMLDivElement>>
    get() = "bgw_image_visual".unsafeCast<IntrinsicType<HTMLAttributes<HTMLDivElement>>>()