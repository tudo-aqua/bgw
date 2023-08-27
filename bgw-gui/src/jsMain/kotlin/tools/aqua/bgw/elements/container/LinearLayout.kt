package tools.aqua.bgw.elements.container

import LinearLayoutData
import csstype.*
import emotion.react.css
import org.w3c.dom.HTMLDivElement
import react.FC
import react.IntrinsicType
import react.Props
import react.dom.html.HTMLAttributes
import tools.aqua.bgw.builder.NodeBuilder
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.bgwContents
import tools.aqua.bgw.elements.bgwVisuals
import tools.aqua.bgw.elements.cssBuilder
import tools.aqua.bgw.elements.fit

external interface LinearLayoutProps : Props {
    var data : LinearLayoutData
}

fun PropertiesBuilder.cssBuilderIntern(componentViewData: LinearLayoutData) {
    cssBuilder(componentViewData)
}

val LinearLayout = FC<LinearLayoutProps> { props ->
    bgwLinearLayout {
        id = props.data.id
        className = ClassName("linearLayout")
        css {
            cssBuilderIntern(props.data)
        }

        bgwVisuals {
            className = ClassName("visuals")
            VisualBuilder.build(props.data.visual).forEach {
                +it
            }
        }

        bgwContents {
            className = ClassName("components")
            css {
                width = 100.pct
                height = 100.pct
                display = Display.flex
                flexDirection = if(props.data.orientation == "horizontal") FlexDirection.row else FlexDirection.column
                if(props.data.orientation == "horizontal") {
                    justifyContent = when(props.data.alignment.first) {
                        "left" -> JustifyContent.flexStart
                        "center" -> JustifyContent.center
                        "right" -> JustifyContent.flexEnd
                        else -> JustifyContent.center
                    }
                    alignItems = when(props.data.alignment.second) {
                        "top" -> AlignItems.flexStart
                        "center" -> AlignItems.center
                        "bottom" -> AlignItems.flexEnd
                        else -> AlignItems.center
                    }
                } else {
                    alignItems = when(props.data.alignment.first) {
                        "left" -> AlignItems.flexStart
                        "center" -> AlignItems.center
                        "right" -> AlignItems.flexEnd
                        else -> AlignItems.center
                    }
                    justifyContent = when(props.data.alignment.second) {
                        "top" -> JustifyContent.flexStart
                        "center" -> JustifyContent.center
                        "bottom" -> JustifyContent.flexEnd
                        else -> JustifyContent.center
                    }
                }
                gap = props.data.spacing.rem
            }

            props.data.components.forEach {
                +NodeBuilder.build(it)
            }
        }
    }
}

inline val bgwLinearLayout: IntrinsicType<HTMLAttributes<HTMLDivElement>>
    get() = "bgw_linear_layout".unsafeCast<IntrinsicType<HTMLAttributes<HTMLDivElement>>>()