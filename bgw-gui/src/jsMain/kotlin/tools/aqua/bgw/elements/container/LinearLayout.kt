package tools.aqua.bgw.elements.container

import LinearLayoutData
import csstype.*
import emotion.react.css
import org.w3c.dom.HTMLDivElement
import react.*
import react.dom.html.HTMLAttributes
import tools.aqua.bgw.builder.NodeBuilder
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.bgwContents
import tools.aqua.bgw.elements.bgwVisuals
import tools.aqua.bgw.elements.cssBuilder
import tools.aqua.bgw.elements.fit
import tools.aqua.bgw.handlers

external interface LinearLayoutProps : Props {
    var data : LinearLayoutData
}

fun PropertiesBuilder.cssBuilderIntern(componentViewData: LinearLayoutData) {
    cssBuilder(componentViewData)
}

val LinearLayout = FC<LinearLayoutProps> { props ->
    val (data, setData) = useState(props.data)

    useEffect {
        handlers[props.data.id] = { newData ->
            if(newData is LinearLayoutData) {
                println("Updating LinearLayout ${props.data.id}")
                setData(newData)
            }
        }
    }
    
    bgwLinearLayout {
        id = data.id
        className = ClassName("linearLayout")
        css {
            cssBuilderIntern(data)
        }

        bgwVisuals {
            className = ClassName("visuals")
            VisualBuilder.build(data.visual).forEach {
                +it
            }
        }

        bgwContents {
            className = ClassName("components")
            css {
                width = 100.pct
                height = 100.pct
                display = Display.flex
                flexDirection = if(data.orientation == "horizontal") FlexDirection.row else FlexDirection.column
                if(data.orientation == "horizontal") {
                    justifyContent = when(data.alignment.first) {
                        "left" -> JustifyContent.flexStart
                        "center" -> JustifyContent.center
                        "right" -> JustifyContent.flexEnd
                        else -> JustifyContent.center
                    }
                    alignItems = when(data.alignment.second) {
                        "top" -> AlignItems.flexStart
                        "center" -> AlignItems.center
                        "bottom" -> AlignItems.flexEnd
                        else -> AlignItems.center
                    }
                } else {
                    alignItems = when(data.alignment.first) {
                        "left" -> AlignItems.flexStart
                        "center" -> AlignItems.center
                        "right" -> AlignItems.flexEnd
                        else -> AlignItems.center
                    }
                    justifyContent = when(data.alignment.second) {
                        "top" -> JustifyContent.flexStart
                        "center" -> JustifyContent.center
                        "bottom" -> JustifyContent.flexEnd
                        else -> JustifyContent.center
                    }
                }
                gap = data.spacing.rem
            }

            data.components.forEach {
                +NodeBuilder.build(it)
            }
        }
    }
}

inline val bgwLinearLayout: IntrinsicType<HTMLAttributes<HTMLDivElement>>
    get() = "bgw_linear_layout".unsafeCast<IntrinsicType<HTMLAttributes<HTMLDivElement>>>()