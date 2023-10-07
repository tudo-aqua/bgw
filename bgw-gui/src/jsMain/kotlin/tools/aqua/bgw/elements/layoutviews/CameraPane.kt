package tools.aqua.bgw.elements.layoutviews

import CameraPaneData
import InternalCameraPaneData
import csstype.ClassName
import csstype.PropertiesBuilder
import csstype.number
import emotion.react.css
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.js.timers.setInterval
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLUnknownElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import org.w3c.dom.events.WheelEvent
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML.div
import tools.aqua.bgw.builder.LayoutNodeBuilder
import tools.aqua.bgw.builder.NodeBuilder
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.bgwContents
import tools.aqua.bgw.elements.bgwVisuals
import tools.aqua.bgw.elements.cssBuilder
import tools.aqua.bgw.elements.uicomponents.ButtonProps
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.round
import kotlin.random.Random
import csstype.*
import data.event.KeyEventAction
import data.event.internal.ScrollChangedEventData
import data.event.internal.ZoomChangedEventData
import react.*
import react.dom.html.ReactHTML.h1
import tools.aqua.bgw.builder.ReactConverters.toKeyEventData
import tools.aqua.bgw.builder.ReactConverters.toMouseEventData
import tools.aqua.bgw.elements.fit
import tools.aqua.bgw.event.JCEFEventDispatcher
import tools.aqua.bgw.handlers

external interface CameraPaneProps : Props {
    var data: CameraPaneData
}

fun PropertiesBuilder.cssBuilderIntern(componentViewData: CameraPaneData) {
    cssBuilder(componentViewData)
}

val CameraPane = FC<CameraPaneProps> { props ->
    var container: HTMLElement? = null
    var target: HTMLElement? = null

    //TODO: Gets reset
    var startY: Int = props.data.internalData.startX
    var startX: Int = props.data.internalData.startY

    var scrollLeft: Double = props.data.scroll.xCoord
    var scrollTop: Double = props.data.scroll.yCoord
    var isDown: Boolean = false

    var zoomLevel: Double = props.data.zoom
    var maxZoom: Double = 10.0
    var minZoom: Double = 0.01
    var interactive: Boolean = true
    val zoomLevels = listOf(0.01,0.012309185338821003,0.01720750265929715,0.024055056416761967,0.03362752577585457,0.047009263674717305,0.06571613046910042,0.09186720799786698,0.12842484554521405,0.1795302296951816,0.25097249085697504,0.35084448604504254,0.49045954386434326,0.6856330190030036,1.0,1.339888912168399,1.8730843174962588,2.6184595070441774,3.660449305984716,5.1170885345518275,7.153382790366966,10.0)

    //TODO: Gets reset
    var lastMousePosition: Pair<Double, Double> = props.data.internalData.lastMousePosition
    var anchorPoint: Pair<Double, Double> = props.data.internalData.anchorPoint
    var startPos: Pair<Double, Double> = props.data.internalData.startPos
    var endPos: Pair<Double, Double> = props.data.internalData.endPos
    var animValue: Double = props.data.internalData.animValue

    // Handle functions for callback to Kotlin
    fun handleZoomChange(zoomChange: Double) {
        zoomLevel = zoomChange
        JCEFEventDispatcher.dispatchEvent(ZoomChangedEventData(zoomLevel).apply { id = props.data.id })
        JCEFEventDispatcher.dispatchEvent(ScrollChangedEventData(scrollLeft, scrollTop).apply { id = props.data.id })
        JCEFEventDispatcher.dispatchEvent(InternalCameraPaneData(
            lastMousePosition = lastMousePosition,
            anchorPoint = anchorPoint,
            startPos = startPos,
            endPos = endPos,
            animValue = animValue,
            startY = startY,
            startX = startX
        ).apply { id = props.data.id })
    }

    fun getPositionInPane(posX: Int, posY: Int): Pair<Double, Double> {
        val x = (posX + container?.scrollLeft!!) / zoomLevel
        val y = (posY + container?.scrollTop!!) / zoomLevel
        return Pair(x, y)
    }

    fun convertToPane(pos: Pair<Double, Double>): Pair<Double, Double> {
        val targetW = target!!.clientWidth
        val targetH = target!!.clientHeight

        val posX = pos.first / props.data.target!!.width * targetW
        val posY = pos.second / props.data.target!!.height * targetH

        return Pair(posX, posY)
    }

    fun mouseIsDown(e: MouseEvent) {
        if (!interactive)
            return
        isDown = true
        startY = e.clientY - container!!.offsetTop
        startX = e.clientX - container!!.offsetLeft
        scrollLeft = container?.scrollLeft ?: 0.0
        scrollTop = container?.scrollTop ?: 0.0
        JCEFEventDispatcher.dispatchEvent(InternalCameraPaneData(
            lastMousePosition = lastMousePosition,
            anchorPoint = anchorPoint,
            startPos = startPos,
            endPos = endPos,
            animValue = animValue,
            startY = startY,
            startX = startX
        ).apply { id = props.data.id })
    }

    fun mouseIsUp(e: MouseEvent) {
        if (!interactive)
            return
        isDown = false
        JCEFEventDispatcher.dispatchEvent(ScrollChangedEventData(scrollLeft, scrollTop).apply { id = props.data.id })
        JCEFEventDispatcher.dispatchEvent(InternalCameraPaneData(
            lastMousePosition = lastMousePosition,
            anchorPoint = anchorPoint,
            startPos = startPos,
            endPos = endPos,
            animValue = animValue,
            startY = startY,
            startX = startX
        ).apply { id = props.data.id })
    }

    fun mouseLeave(e: MouseEvent) {
        if (!interactive)
            return
        isDown = false
    }

    fun mouseMove(e: MouseEvent) {
        if (!interactive)
            return
        if (isDown) {
            e.preventDefault()
            val y = e.pageY - container!!.offsetTop
            val walkY = y - startY
            container!!.scrollTop = scrollTop - walkY

            val x = e.pageX - container!!.offsetLeft
            val walkX = x - startX
            container!!.scrollLeft = scrollLeft - walkX

            anchorPoint = getPositionInPane(container!!.clientWidth, container!!.clientHeight)
        } else {
            val bounds = container!!.getBoundingClientRect()
            val pos = getPositionInPane(e.clientX - bounds.x.toInt(), e.clientY - bounds.y.toInt())
            lastMousePosition = pos
        }
    }

    fun mouseScroll(e: WheelEvent) {
        if (!interactive || isDown)
            return
        e.preventDefault()

        if (e.deltaY < 0) {
            val index = zoomLevels.indexOfFirst { it > zoomLevel }
            if (index != -1) {
                handleZoomChange(zoomLevels[index])
            } else {
                handleZoomChange(maxZoom)
            }
        } else {
            val index = zoomLevels.indexOfLast { it < zoomLevel }
            if (index != -1) {
                handleZoomChange(zoomLevels[index])
            } else {
                handleZoomChange(minZoom)
            }
        }
        target?.setAttribute("style", "zoom: $zoomLevel;")

        val bounds = container!!.getBoundingClientRect()
        val newPos = getPositionInPane(e.clientX - bounds.x.toInt(), e.clientY - bounds.y.toInt())
        val diffX = newPos.first - lastMousePosition.first
        val diffY = newPos.second - lastMousePosition.second
        container?.scrollLeft = container?.scrollLeft?.minus(diffX * zoomLevel)!!
        container?.scrollTop = container?.scrollTop?.minus(diffY * zoomLevel)!!
        anchorPoint = getPositionInPane(container!!.clientWidth / 2, container!!.clientHeight / 2)
    }

    fun paneScrollTo(x: Double, y: Double) {
        val pos = convertToPane(Pair(x, y))
        var posX = pos.first
        if (posX > target!!.clientWidth) posX = target!!.clientWidth.toDouble()

        var posY = pos.second
        if (posY > target!!.clientHeight) posY = target!!.clientHeight.toDouble()

        container?.scrollLeft = (posX * zoomLevel) - (container!!.clientWidth / 2)
        container?.scrollTop = (posY * zoomLevel) - (container!!.clientHeight / 2)

        anchorPoint = getPositionInPane(container!!.clientWidth / 2, container!!.clientHeight / 2)
        lastMousePosition = getPositionInPane(container!!.clientWidth / 2, container!!.clientHeight / 2)
    }

    // TODO: Might need fixing
    fun paneScrollBy(x: Double, y: Double) {
        container?.scrollLeft = container?.scrollLeft?.plus(x * zoomLevel)!!
        container?.scrollTop = container?.scrollTop?.plus(y * zoomLevel)!!
    }

    fun easeInOutCubic(x: Double): Double {
        return if (x < 0.5) 4 * x * x * x else 1 - (-2 * x + 2).pow(3) / 2
    }

    fun animationLoop() {
        val x = easeInOutCubic(animValue) * (endPos.first - startPos.first)
        val y = easeInOutCubic(animValue) * (endPos.second - startPos.second)

        paneScrollTo(startPos.first + x, startPos.second + y)

        if (animValue < 1) {
            animValue += 0.005
        } else {
            animValue = 0.0
            anchorPoint = endPos
            return;
        }

        window.requestAnimationFrame { animationLoop() }
    }

    fun paneSmoothScrollTo(x: Double, y: Double) {
        if (animValue > 0) return

        startPos = anchorPoint
        endPos = Pair(x, y)

        animationLoop()
    }

    fun testScroll(e: MouseEvent) {
        val x = Random.nextDouble(0.0, 4000.0)
        val y = Random.nextDouble(0.0, 4000.0)
        paneSmoothScrollTo(x, y)
    }

    fun mountCamera() {
        container?.onmousedown = { e -> mouseIsDown(e) }
        container?.onmouseup = { e -> mouseIsUp(e) }
        container?.onmouseleave = { e -> mouseLeave(e) }
        container?.onmousemove = { e -> mouseMove(e) }
        container?.onwheel = { e -> mouseScroll(e) }

        // target?.onclick = { e -> testScroll(e) }
    }

    useEffect {
        //println("Before mounting camera...")
        container = document.getElementById(props.data.id) as HTMLElement
        target = document.getElementById(props.data.target?.id + "Target") as HTMLElement

        //println("Camera mounting...")
        if (container != null && target != null)
            mountCamera()
    }

    bgwCameraPane {
        tabIndex = 0
        id = props.data.id
        className = ClassName("cameraPane")
        css {
            cssBuilderIntern(props.data)
        }

        bgwCameraContent {
            css {
                width = fit()
                height = fit()
                minWidth = 100.pct
                minHeight = 100.pct
                // padding = 100.vh
                display = Display.flex
                justifyContent = JustifyContent.center
                alignItems = AlignItems.center
                position = Position.absolute
                left = 0.px
                top = 0.px
            }

            +VisualBuilder.build(props.data.visual)

            if (props.data.target != null) {
                bgwCameraTarget {
                    id = props.data.target?.id + "Target"
                    className = ClassName("target")
                    +props.data.target?.let { LayoutNodeBuilder.build(it) }
                }
            }
        }
        onContextMenu = {
            it.preventDefault()
            JCEFEventDispatcher.dispatchEvent(it.toMouseEventData(id))
        }
        onClick = { JCEFEventDispatcher.dispatchEvent(it.toMouseEventData(id)) }
        onKeyDown = { JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(id, KeyEventAction.PRESS)) }
        onKeyUp = { JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(id, KeyEventAction.RELEASE)) }
        onKeyPress = { JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(id, KeyEventAction.TYPE)) }
    }
}

inline val bgwCameraPane: IntrinsicType<HTMLAttributes<HTMLDivElement>>
    get() = "bgw_camera_pane".unsafeCast<IntrinsicType<HTMLAttributes<HTMLDivElement>>>()

inline val bgwCameraTarget: IntrinsicType<HTMLAttributes<HTMLDivElement>>
    get() = "bgw_camera_target".unsafeCast<IntrinsicType<HTMLAttributes<HTMLDivElement>>>()

inline val bgwCameraContent: IntrinsicType<HTMLAttributes<HTMLDivElement>>
    get() = "bgw_camera_content".unsafeCast<IntrinsicType<HTMLAttributes<HTMLDivElement>>>()