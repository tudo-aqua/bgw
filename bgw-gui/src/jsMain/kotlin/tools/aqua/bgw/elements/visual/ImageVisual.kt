package tools.aqua.bgw.elements.visual

import ImageVisualData
import web.cssom.*
import emotion.react.Global
import emotion.react.css
import kotlinx.browser.document
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLImageElement
import react.FC
import react.IntrinsicType
import react.Props
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML.img
import react.useEffect
import tools.aqua.bgw.elements.filterBuilder
import tools.aqua.bgw.elements.flipBuilder
import tools.aqua.bgw.elements.styleBuilder
import web.dom.Element

external interface ImageVisualProps : Props {
    var data: ImageVisualData
}

val ImageVisual = FC<ImageVisualProps> { props ->
    if(props.data.width != -1 || props.data.height != -1) {
        img {
            id = props.data.id
            src = props.data.path

            css {
                styleBuilder(props.data.style)
                flipBuilder(props.data.flipped)
                filterBuilder(props.data.filters)

                position = Position.absolute
                width = Auto.auto
                height = Auto.auto

                left = -props.data.offsetX.rem
                top = -props.data.offsetY.rem

                opacity = number(props.data.transparency)
                // TODO...
            }

            useEffect {
                val img = document.getElementById(props.data.id) as HTMLImageElement
                img.addEventListener("load", {
                    val bounds = img.getBoundingClientRect()
                    img.style.width = bounds.width.toString() + "rem"
                    img.style.height = bounds.height.toString() + "rem"
                })
            }
        }
    } else {
        bgwImageVisual {
            id = props.data.id

            css {
                styleBuilder(props.data.style)
                flipBuilder(props.data.flipped)
                filterBuilder(props.data.filters)
                backgroundImage = url(props.data.path)

                backgroundSize = BackgroundSize.cover
                backgroundRepeat = BackgroundRepeat.noRepeat
                backgroundPosition = Background.center as BackgroundPosition

                left = props.data.offsetX.rem
                top = props.data.offsetY.rem

                opacity = number(props.data.transparency)
                // TODO...
            }
        }
    }
}

inline val bgwImageVisual: IntrinsicType<HTMLAttributes<Element>>
    get() = "bgw_image_visual".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()

inline val bgwImageVisualOffset: IntrinsicType<HTMLAttributes<Element>>
    get() = "bgw_image_visual_offset".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()