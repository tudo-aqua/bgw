package tools.aqua.bgw.elements.gamecomponentviews

import TokenViewData
import csstype.*
import emotion.react.css
import org.w3c.dom.HTMLDivElement
import react.FC
import react.IntrinsicType
import react.Props
import react.dom.html.HTMLAttributes
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.bgwText
import tools.aqua.bgw.elements.bgwVisuals
import tools.aqua.bgw.elements.cssBuilder

external interface TokenViewProps : Props {
    var data: TokenViewData
}

fun PropertiesBuilder.cssBuilderIntern(componentViewData: TokenViewData) {
    cssBuilder(componentViewData)
}

val TokenView = FC<TokenViewProps> { props ->
    bgwTokenView {
        id = props.data.id
        className = ClassName("tokenView")
        css {
            cssBuilderIntern(props.data)
        }

        bgwVisuals {
            className = ClassName("visuals")
            VisualBuilder.build(props.data.visual).forEach {
                +it
            }
        }
    }
}

inline val bgwTokenView: IntrinsicType<HTMLAttributes<HTMLDivElement>>
    get() = "bgw_token_view".unsafeCast<IntrinsicType<HTMLAttributes<HTMLDivElement>>>()