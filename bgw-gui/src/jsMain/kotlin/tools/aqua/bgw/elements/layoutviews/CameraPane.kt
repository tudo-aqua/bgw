package tools.aqua.bgw.elements.layoutviews

import CameraPaneData
import csstype.PropertiesBuilder
import data.event.internal.TransformChangedEventData
import emotion.react.css
import js.objects.jso
import react.*
import react.dom.html.HTMLAttributes
import tools.aqua.bgw.ReactZoomPanPinchContentRef
import tools.aqua.bgw.TransformComponent
import tools.aqua.bgw.TransformWrapper
import tools.aqua.bgw.builder.LayoutNodeBuilder
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.bgwVisuals
import tools.aqua.bgw.elements.cssBuilder
import tools.aqua.bgw.event.JCEFEventDispatcher
import web.cssom.*
import web.dom.Element
import web.dom.document
import web.dom.getComputedStyle

internal external interface CameraPaneProps : Props {
    var data: CameraPaneData
}

internal fun PropertiesBuilder.cssBuilderIntern(componentViewData: CameraPaneData) {
    cssBuilder(componentViewData)
}

internal fun convertToRem(px: Double): Double {
    val currentRem = document.getElementById("bgw-root")
        ?.let {
            getComputedStyle(it).fontSize.replace("px", "").toDouble()
        }
    return px * currentRem!!
}

internal fun convertToPx(rem: Double): Double {
    val currentRem = document.getElementById("bgw-root")
        ?.let {
            getComputedStyle(it).fontSize.replace("px", "").toDouble()
        }
    return rem / currentRem!!
}

internal val CameraPane = FC<CameraPaneProps> { props ->
    val cameraPaneRef = useRef<ReactZoomPanPinchContentRef>(null)

    useEffect {
        val panSmooth = props.data.internalPanData.panSmooth
        val panTime = if(panSmooth) 300 else 0
        val panBy = props.data.internalPanData.panBy

        if(cameraPaneRef.current != null && props.data.internalPanData.zoomOnly) {
            val ctx = cameraPaneRef.current!!
            val currentX = ctx.instance.transformState.positionX
            val currentY = ctx.instance.transformState.positionY

            ctx.setTransform(currentX, currentY, props.data.internalPanData.zoom!!, panTime)

            val actualXOffset = convertToPx(currentX)
            val actualYOffset = convertToPx(currentY)

            JCEFEventDispatcher.dispatchEvent(TransformChangedEventData(
                zoomLevel = ctx.instance.transformState.scale,
                anchor = Pair(actualXOffset, actualYOffset)
            ).apply { id = props.data.id })
        } else if(cameraPaneRef.current != null && props.data.internalPanData.panTo != null) {
            val ctx = cameraPaneRef.current!!
            val currentScale = ctx.instance.transformState.scale

            val panZoom = props.data.internalPanData.zoom ?: currentScale

            if(panBy) {
                val panByX = (convertToRem(props.data.internalPanData.panTo!!.first) * panZoom)
                val panByY = (convertToRem(props.data.internalPanData.panTo!!.second) * panZoom)
                ctx.setTransform(ctx.instance.transformState.positionX + panByX, ctx.instance.transformState.positionY + panByY, panZoom, panTime)
            } else {
                val panToX = (convertToRem(props.data.internalPanData.panTo!!.first) * panZoom + convertToRem(props.data.width / 2.0))
                val panToY = (convertToRem(props.data.internalPanData.panTo!!.second) * panZoom + convertToRem(props.data.height / 2.0))
                ctx.setTransform(panToX, panToY, panZoom, panTime)
            }

            val currentX = ctx.instance.transformState.positionX
            val currentY = ctx.instance.transformState.positionY

            val actualXOffset = convertToPx(currentX)
            val actualYOffset = convertToPx(currentY)

            JCEFEventDispatcher.dispatchEvent(TransformChangedEventData(
                zoomLevel = ctx.instance.transformState.scale,
                anchor = Pair(actualXOffset, actualYOffset)
            ).apply { id = props.data.id })
        }
    }

    bgwCameraTarget {
        css {
            cssBuilderIntern(props.data)
        }

        bgwVisuals {
            className = ClassName("visuals")
            +VisualBuilder.build(props.data.visual)
        }
    }

    TransformWrapper {
        centerZoomedOut = false // Could be set to true
        disablePadding = true
        smooth = false
        limitToBounds = false // Could be set to true
        centerOnInit = true
        minScale = 0.1
        maxScale = 4.0
        initialScale = 1.0
        disabled = !props.data.interactive
        wheel = jso {
            step = 0.1
        }
        panning = jso {
            disabled = false
            wheelPanning = false
            velocityDisabled = true
            allowLeftClickPan = props.data.panButton == "left_button"
            allowMiddleClickPan = props.data.panButton == "mouse_wheel"
            allowRightClickPan = props.data.panButton == "right_button"
        }
        pinch = jso {
            disabled = true
        }
        doubleClick = jso {
            disabled = true
        }

//        onTransformed = { ctx, state ->
//            val currentX = state.positionX
//            val currentY = state.positionY
//
//            val actualXOffset = convertToPx(currentX)
//            val actualYOffset = convertToPx(currentY)
//
//            JCEFEventDispatcher.dispatchEvent(TransformChangedEventData(
//                zoomLevel = state.scale,
//                anchor = Pair(actualXOffset, actualYOffset)
//            ).apply { id = props.data.id })
//        }

        onZoomStop = { ctx ->
            val currentX = ctx.instance.transformState.positionX
            val currentY = ctx.instance.transformState.positionY

            val actualXOffset = convertToPx(currentX)
            val actualYOffset = convertToPx(currentY)

            JCEFEventDispatcher.dispatchEvent(TransformChangedEventData(
                zoomLevel = ctx.instance.transformState.scale,
                anchor = Pair(actualXOffset, actualYOffset)
            ).apply { id = props.data.id })
        }

        onPanningStop = { ctx ->
            val currentX = ctx.instance.transformState.positionX
            val currentY = ctx.instance.transformState.positionY

            val targetWidth = props.data.target?.width?.let { convertToRem(it.toDouble()) * ctx.instance.transformState.scale } ?: 0.0
            val targetHeight = props.data.target?.height?.let { convertToRem(it.toDouble()) * ctx.instance.transformState.scale } ?: 0.0

            val paneWidth = convertToRem(props.data.width.toDouble())
            val paneHeight = convertToRem(props.data.height.toDouble())

            val minX = -targetWidth + convertToRem(50.0)
            val minY = -targetHeight + convertToRem(50.0)
            val maxX = paneWidth - convertToRem(50.0)
            val maxY = paneHeight - convertToRem(50.0)

            if(currentX < minX && currentY < minY) {
                ctx.setTransform(minX, minY, ctx.instance.transformState.scale, 300)
            } else if(currentX > maxX && currentY > maxY) {
                ctx.setTransform(maxX, maxY, ctx.instance.transformState.scale, 300)
            } else if(currentX < minX && currentY > maxY) {
                ctx.setTransform(minX, maxY, ctx.instance.transformState.scale, 300)
            } else if(currentX > maxX && currentY < minY) {
                ctx.setTransform(maxX, minY, ctx.instance.transformState.scale, 300)
            } else {
                if(currentX < minX) {
                    ctx.setTransform(minX, currentY, ctx.instance.transformState.scale, 300)
                } else if(currentX > maxX) {
                    ctx.setTransform(maxX, currentY, ctx.instance.transformState.scale, 300)
                }

                if(currentY < minY) {
                    ctx.setTransform(currentX, minY, ctx.instance.transformState.scale, 300)
                } else if(currentY > maxY) {
                    ctx.setTransform(currentX, maxY, ctx.instance.transformState.scale, 300)
                }
            }

            JCEFEventDispatcher.dispatchEvent(TransformChangedEventData(
                zoomLevel = ctx.instance.transformState.scale,
                anchor = Pair(convertToPx(ctx.instance.transformState.positionX), convertToPx(ctx.instance.transformState.positionY))
            ).apply { id = props.data.id })
        }

        ref = cameraPaneRef

        TransformComponent {
            wrapperStyle = jso {
                width = props.data.width.em
                height = props.data.height.em
                left = props.data.posX.em
                top = props.data.posY.em
                position = Position.absolute
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

internal inline val bgwCameraTarget: IntrinsicType<HTMLAttributes<Element>>
    get() = "bgw_camera_target".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()