package tools.aqua.bgw.elements.layoutviews

import CameraPaneData
import csstype.PropertiesBuilder
import emotion.react.css
import js.objects.jso
import react.FC
import react.IntrinsicType
import react.Props
import react.dom.html.HTMLAttributes
import react.useEffect
import tools.aqua.bgw.TransformComponent
import tools.aqua.bgw.TransformWrapper
import tools.aqua.bgw.builder.LayoutNodeBuilder
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.bgwVisuals
import tools.aqua.bgw.elements.cssBuilder
import tools.aqua.bgw.elements.fit
import web.cssom.*
import web.dom.Element

internal external interface CameraPaneProps : Props {
    var data: CameraPaneData
}

internal fun PropertiesBuilder.cssBuilderIntern(componentViewData: CameraPaneData) {
    cssBuilder(componentViewData)
}

internal val CameraPane = FC<CameraPaneProps> { props ->
    TransformWrapper {
        centerZoomedOut = false
        disablePadding = true
        smooth = false
        limitToBounds = true
        centerOnInit = true
        minScale = 0.1
        maxScale = 4.0
        initialScale = props.data.zoom
        disabled = !props.data.interactive
        wheel = jso {
            step = 0.1
        }

        TransformComponent {
            wrapperStyle = jso {
                width = props.data.width.em
                height = props.data.height.em
                left = props.data.posX.em
                top = props.data.posY.em
                position = Position.absolute
            }

            bgwVisuals {
                className = ClassName("visuals")
                +VisualBuilder.build(props.data.visual)
            }

            if (props.data.target != null) {
                bgwCameraTarget {
                    id = props.data.target?.id + "Target"
                    className = ClassName("target")
                    +props.data.target?.let { LayoutNodeBuilder.build(it) }
                }
            }
        }
    }
}

inline val bgwCameraTarget: IntrinsicType<HTMLAttributes<Element>>
    get() = "bgw_camera_target".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()