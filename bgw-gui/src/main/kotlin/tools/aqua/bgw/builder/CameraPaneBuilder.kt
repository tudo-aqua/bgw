/*
 * Copyright 2023 The BoardGameWork Authors
 * SPDX-License-Identifier: Apache-2.0
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tools.aqua.bgw.builder

import java.io.FileNotFoundException
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
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.max
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.layoutviews.CameraPane
import tools.aqua.bgw.components.layoutviews.LayoutView
import tools.aqua.bgw.core.Scene

/**
 * The [CameraPaneBuilder] object provides a method for building a camera pane in a specified scene
 * and container.
 */
object CameraPaneBuilder {
  /**
   * Builds a camera pane within the specified scene and container.
   * @param scene The scene in which the camera pane will be built.
   * @param container The container that will hold the camera pane.
   * @return The built camera pane as a Region.
   */
  internal fun buildCameraPane(
      scene: Scene<out ComponentView>,
      container: CameraPane<out LayoutView<*>>
  ): Region {
    val node =
        ZoomableScrollPane(
            (LayoutNodeBuilder.buildLayoutView(scene, container.target) as Pane).apply {
              minWidth = container.target.width
              minHeight = container.target.height
            })

    container.interactiveProperty.setGUIListenerAndInvoke(container.interactive) { _, nV ->
      node.isPannable = nV
    }

    container.zoomProperty.setGUIListenerAndInvoke(container.zoom) { _, nV -> node.scaleValue = nV }

    container.anchorPointProperty.setGUIListenerAndInvoke(container.anchorPoint) { _, nV ->
      node.scrollTo(Point2D(nV.xCoord, nV.yCoord))
    }

    return node.apply {
      minWidth = container.width
      minHeight = container.height
    }
  }
}
/**
 * The [ZoomableScrollPane] class represents a scroll pane with zooming capabilities. It provides
 * functionality to zoom in and out, scroll to specific points, and reset the zoom and scroll
 * positions.
 *
 * @property target The pane contained within the scroll pane.
 * @property scaleValue The current scale value of the scroll pane.
 * @property zoomIntensity The intensity factor for zooming.
 * @property zoomNode The node to be zoomed.
 * @property timeline The timeline for animating the reset operation.
 * @property lerpTime The time for linear interpolation during reset animation.
 * @property anchorPoint The anchor point used for scrolling.
 */
internal class ZoomableScrollPane(private val target: Pane) : ScrollPane() {
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
    // isFitToHeight = true //center
    // isFitToWidth = true //center
  }

  /**
   * Scrolls the view to the specified point if it is within the zoomed node's bounds.
   * @param point The point to scroll to.
   */
  fun scrollTo(point: Point2D) {
    if (zoomNode.boundsInLocal.contains(point)) {
      anchorPoint = point
      val scaledWidth = target.width * scaleValue
      val scaledHeight = target.height * scaleValue
      hvalue = point.x * scaleValue / (scaledWidth - width)
      vvalue = point.y * scaleValue / (scaledHeight - height)
    }
  }

  /**
   * Scrolls the view by the specified offset if the resulting point is within the zoomed node's
   * bounds.
   * @param xOffset The horizontal offset.
   * @param yOffset The vertical offset.
   */
  fun scrollBy(xOffset: Double, yOffset: Double) {
    if (zoomNode.boundsInLocal.contains(anchorPoint.add(xOffset, yOffset))) {
      anchorPoint = anchorPoint.add(xOffset, yOffset)
      scrollTo(anchorPoint)
    }
  }

  /**
   * Resets the zoom and scroll positions to their initial values. Performs an animated transition
   * using a timeline.
   */
  fun reset() {
    if (timeline?.status == Animation.Status.RUNNING) return
    timeline =
        Timeline(
                KeyFrame(
                    Duration.millis(10.0),
                    {
                      hvalue += (0.0f - hvalue) * lerpTime
                      vvalue += (0.0f - vvalue) * lerpTime
                      scaleValue += (1.0f - scaleValue) * lerpTime
                      lerpTime += 0.001
                      if (abs(hvalue - 0.0) <= 0.01 &&
                          abs(vvalue - 0.0) <= 0.01 &&
                          abs(scaleValue - 1.0) <= 0.01) {
                        timeline?.stop()
                        hvalue = 0.0
                        vvalue = 0.0
                        scaleValue = 1.0
                        lerpTime = 0.0
                      }
                    }))
            .apply { cycleCount = Timeline.INDEFINITE }
    timeline?.play()
  }

  private fun outerNode(node: Node): Node {
    val outerNode = centeredNode(node)
    outerNode.onScroll = EventHandler { e: ScrollEvent ->
      e.consume()
      if (isPannable) onScroll(e.textDeltaY, Point2D(e.x, e.y))
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
    val adjustment =
        target.localToParentTransform.deltaTransform(posInZoomTarget.multiply(zoomFactor - 1))

    // convert back to [0, 1] range
    // (too large/small values are automatically corrected by ScrollPane)
    val updatedInnerBounds = zoomNode.boundsInLocal
    hvalue = (valX + adjustment.x) / (updatedInnerBounds.width - viewportBounds.width)
    vvalue = (valY + adjustment.y) / (updatedInnerBounds.height - viewportBounds.height)
  }
}
