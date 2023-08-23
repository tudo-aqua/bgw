package tools.aqua.bgw.elements

import SceneData
import csstype.ClassName
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
    scene {
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

inline val scene: IntrinsicType<HTMLAttributes<HTMLDivElement>>
    get() = "scene".unsafeCast<IntrinsicType<HTMLAttributes<HTMLDivElement>>>()