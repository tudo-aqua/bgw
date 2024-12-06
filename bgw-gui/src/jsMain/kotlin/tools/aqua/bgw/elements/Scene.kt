package tools.aqua.bgw.elements

import SceneData
import web.cssom.*
import emotion.react.css
import org.w3c.dom.HTMLDivElement
import react.FC
import react.IntrinsicType
import react.Props
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.section
import tools.aqua.bgw.builder.NodeBuilder
import tools.aqua.bgw.builder.VisualBuilder
import web.dom.Element

internal external interface SceneProps : Props {
    var data: SceneData
}

internal val Scene = FC<SceneProps> { props ->
    bgwScene {
        css {
            transition = menuTransition()
        }

        bgwVisuals {
            className = ClassName("visuals")
            +VisualBuilder.build(props.data.background)
        }

        bgwContents {
            className = ClassName("components")
            css {
                width = 100.pct
                height = 100.pct
                position = Position.absolute
                left = 0.px
                top = 0.px
            }
            props.data.components.forEach {
                +NodeBuilder.build(it)
            }
        }
    }
}

internal inline val bgwScene: IntrinsicType<HTMLAttributes<Element>>
    get() = "bgw_scene".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()