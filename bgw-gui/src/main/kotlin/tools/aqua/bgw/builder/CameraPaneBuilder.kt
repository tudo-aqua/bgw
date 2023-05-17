package tools.aqua.bgw.builder

import javafx.animation.Animation
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.event.EventHandler
import javafx.geometry.Point2D
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.control.ScrollPane
import javafx.scene.input.ScrollEvent
import javafx.scene.layout.Background
import javafx.scene.layout.Pane
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.util.Duration
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.layoutviews.CameraPane
import tools.aqua.bgw.components.layoutviews.LayoutView
import tools.aqua.bgw.core.Scene
import tools.aqua.bgw.event.KeyCode
import java.io.FileNotFoundException
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.max

object CameraPaneBuilder {
    internal fun buildCameraPane(
        scene: Scene<out ComponentView>,
        container: CameraPane<out LayoutView<*>>
    ): Region {
        val node = ZoomableScrollPane(LayoutNodeBuilder.buildLayoutView(scene, container.target) as Pane)

        container.zoomProperty.setGUIListenerAndInvoke(container.zoom) { _, nV ->
            node.scaleValue = nV
        }

        container.anchorPointProperty.setGUIListenerAndInvoke(container.anchorPoint) { _, nV ->
            node.scrollTo(Point2D(nV.xCoord, nV.yCoord))
        }

        return node.apply {
            minWidth = container.width
            minHeight = container.height
            container.onKeyPressed = {
                when(it.keyCode) {
                    KeyCode.LEFT -> scrollBy(-130.0, 0.0)
                    KeyCode.RIGHT -> scrollBy(130.0, 0.0)
                    KeyCode.UP -> scrollBy(0.0, -200.0)
                    KeyCode.DOWN -> scrollBy(0.0, 200.0)
                    else -> {
                        scrollTo(Point2D(130.0, 200.0))
                    }
                }
            }
        }
    }
}

class ZoomableScrollPane(private val target: Pane) : ScrollPane() {
    var scaleValue: Double = 1.0
        set(value) {
            field = max(value, 1.0)
            target.scaleX = scaleValue
            target.scaleY = scaleValue
        }
    private val zoomIntensity = 0.02
    private val zoomNode: Node
    private var timeline: Timeline? = null
    private var lerpTime = 0.0
    var anchorPoint = Point2D(0.0, 0.0)

    init {
        val resource = this::class.java.getResource("/style.css") ?: throw FileNotFoundException()
        stylesheets.add(resource.toExternalForm())
        background = Background.fill(Color.TRANSPARENT)
        zoomNode = Group(target)
        content = outerNode(zoomNode)
        isPannable = true
        hbarPolicy = ScrollBarPolicy.NEVER
        vbarPolicy = ScrollBarPolicy.NEVER
        //isFitToHeight = true //center
        //isFitToWidth = true //center
    }

    fun scrollTo(point: Point2D) {
        if (zoomNode.boundsInLocal.contains(point)) {
            anchorPoint = point
            val scaledWidth = target.width * scaleValue
            val scaledHeight = target.height * scaleValue
            hvalue = point.x * scaleValue / (scaledWidth - width)
            vvalue = point.y * scaleValue / (scaledHeight - height)
        }
    }

    fun scrollBy(xOffset: Double, yOffset: Double) {
        if (zoomNode.boundsInLocal.contains(anchorPoint.add(xOffset, yOffset))) {
            anchorPoint = anchorPoint.add(xOffset, yOffset)
            scrollTo(anchorPoint)
        }
    }

    fun reset() {
        if (timeline?.status == Animation.Status.RUNNING) return
        timeline = Timeline(KeyFrame(Duration.millis(10.0), {
            hvalue += (0.0f - hvalue) * lerpTime
            vvalue += (0.0f - vvalue) * lerpTime
            scaleValue += (1.0f - scaleValue) * lerpTime
            lerpTime += 0.001
            if (abs(hvalue - 0.0) <= 0.01 && abs(vvalue - 0.0) <= 0.01 && abs(scaleValue - 1.0) <= 0.01) {
                timeline?.stop()
                hvalue = 0.0
                vvalue = 0.0
                scaleValue = 1.0
                lerpTime = 0.0
            }
        })).apply { cycleCount = Timeline.INDEFINITE }
        timeline?.play()
    }

    private fun outerNode(node: Node): Node {
        val outerNode = centeredNode(node)
        outerNode.onScroll = EventHandler { e: ScrollEvent ->
            e.consume()
            onScroll(e.textDeltaY, Point2D(e.x, e.y))
        }
        return outerNode
    }

    private fun centeredNode(node: Node): Node {
        val vBox = VBox(node)
        vBox.alignment = Pos.TOP_LEFT
        return vBox
    }

    private fun onScroll(wheelDelta: Double, mousePoint: Point2D) {
        if (timeline?.status == Animation.Status.RUNNING) return
        val zoomFactor = exp(wheelDelta * zoomIntensity)
        val innerBounds = zoomNode.layoutBounds
        val viewportBounds = viewportBounds

        // calculate pixel offsets from [0, 1] range
        val valX = hvalue * (innerBounds.width - viewportBounds.width)
        val valY = vvalue * (innerBounds.height - viewportBounds.height)
        scaleValue *= zoomFactor
        layout() // refresh ScrollPane scroll positions & target bounds
        // convert target coordinates to zoomTarget coordinates
        val posInZoomTarget = target.parentToLocal(zoomNode.parentToLocal(mousePoint))

        // calculate adjustment of scroll position (pixels)
        val adjustment = target.localToParentTransform.deltaTransform(posInZoomTarget.multiply(zoomFactor - 1))

        // convert back to [0, 1] range
        // (too large/small values are automatically corrected by ScrollPane)
        val updatedInnerBounds = zoomNode.boundsInLocal
        hvalue = (valX + adjustment.x) / (updatedInnerBounds.width - viewportBounds.width)
        vvalue = (valY + adjustment.y) / (updatedInnerBounds.height - viewportBounds.height)
    }
}