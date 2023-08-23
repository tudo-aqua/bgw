package tools.aqua.bgw.elements

import SceneData
import csstype.ClassName
import csstype.Color
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

external interface SceneProps : Props {
    var data: SceneData
}

val Scene = FC<SceneProps> { props ->
    bgwScene {
        div {
            className = ClassName("visuals")
            +VisualBuilder.build(props.data.background)
        }
        section {
            className = ClassName("components")
            props.data.components.forEach {
                +NodeBuilder.build(it)
            }
        }
    }
}

inline val bgwScene: IntrinsicType<HTMLAttributes<HTMLDivElement>>
    get() = "bgw-scene".unsafeCast<IntrinsicType<HTMLAttributes<HTMLDivElement>>>()