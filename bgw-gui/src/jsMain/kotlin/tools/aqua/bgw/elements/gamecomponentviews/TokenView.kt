package tools.aqua.bgw.elements.gamecomponentviews

import TokenViewData
import csstype.*
import emotion.react.css
import org.w3c.dom.HTMLDivElement
import react.*
import react.dom.html.HTMLAttributes
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.bgwText
import tools.aqua.bgw.elements.bgwVisuals
import tools.aqua.bgw.elements.cssBuilder
import tools.aqua.bgw.handlers

external interface TokenViewProps : Props {
    var data: TokenViewData
}

fun PropertiesBuilder.cssBuilderIntern(componentViewData: TokenViewData) {
    cssBuilder(componentViewData)
}

val TokenView = FC<TokenViewProps> { props ->
    val (data, setData) = useState(props.data)

    useEffect {
        handlers[props.data.id] = { newData ->
            if(newData is TokenViewData) {
                println("Updating TokenView ${props.data.id}")
                setData(newData)
            }
        }
    }
    
    bgwTokenView {
        id = data.id
        className = ClassName("tokenView")
        css {
            cssBuilderIntern(data)
        }

        +VisualBuilder.build(data.visual)
    }
}

inline val bgwTokenView: IntrinsicType<HTMLAttributes<HTMLDivElement>>
    get() = "bgw_token_view".unsafeCast<IntrinsicType<HTMLAttributes<HTMLDivElement>>>()