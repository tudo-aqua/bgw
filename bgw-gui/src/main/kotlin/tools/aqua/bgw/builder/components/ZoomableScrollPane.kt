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

package tools.aqua.bgw.builder.components

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
import javafx.scene.input.MouseButton
import javafx.scene.input.ScrollEvent
import javafx.scene.layout.Background
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.util.Duration
import kotlin.math.abs
import kotlin.math.exp
import tools.aqua.bgw.components.layoutviews.CameraPane
import tools.aqua.bgw.components.layoutviews.LayoutView

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
internal class ZoomableScrollPane(
    private val target: Pane,
    private val cameraPane: CameraPane<out LayoutView<*>>
) : ScrollPane() {
  var scaleValue: Double = 1.0
    set(value) {
      field = value.coerceIn(0.01, 10.0)
      target.scaleX = scaleValue
      target.scaleY = scaleValue
    }
  private val zoomIntensity = 0.02
  private val zoomNode: Node
  private var timeline: Timeline? = null
  private var lerpTime = 0.0
  var anchorPoint = Point2D(0.0, 0.0)
  var smooth: Boolean = false

  init {
    val resource = this::class.java.getResource("/style.css") ?: throw FileNotFoundException()
    stylesheets.add(resource.toExternalForm())
    background = Background.fill(Color.TRANSPARENT)
    zoomNode = Group(target)
    content = outerNode(zoomNode)
    isPannable = false
    hbarPolicy = ScrollBarPolicy.NEVER
    vbarPolicy = ScrollBarPolicy.NEVER
    // isFitToHeight = true //center
    // isFitToWidth = true //center
    hvalueProperty().addListener { _, _, nV ->
      if (hPanLocked && nV != hValueLocked) hvalue = hValueLocked
      val viewWidth = cameraPane.width / scaleValue
      anchorPoint = Point2D(hvalue * (cameraPane.target.width - viewWidth), anchorPoint.y)
    }
    vvalueProperty().addListener { _, _, nV ->
      if (vPanLocked && nV != vValueLocked) vvalue = vValueLocked
      val viewHeight = cameraPane.height / scaleValue
      anchorPoint = Point2D(anchorPoint.x, vvalue * (cameraPane.target.height - viewHeight))
    }
  }

  private fun inTargetBounds(point: Point2D): Boolean =
      point.x in 0.0..cameraPane.target.width && point.y in 0.0..cameraPane.target.height

  private fun inFullHorizontalView(): Boolean = cameraPane.target.width == cameraPane.width
  private fun inFullVerticalView(): Boolean = cameraPane.target.height == cameraPane.height

  /**
   * Scrolls the view to the specified point if it is within the zoomed node's bounds.
   * @param point The point to scroll to.
   */
  fun scrollTo(point: Point2D) {
    if (inTargetBounds(point)) {
      anchorPoint = point
      val viewWidth = cameraPane.width / scaleValue
      val viewHeight = cameraPane.height / scaleValue
      hvalue = point.x / (cameraPane.target.width - viewWidth)
      vvalue = point.y / (cameraPane.target.height - viewHeight)
    }
  }

  fun smoothScrollTo(point: Point2D) {
    if (inTargetBounds(point)) {
      val viewWidth = cameraPane.width / scaleValue
      val viewHeight = cameraPane.height / scaleValue
      val newHValue = point.x / (cameraPane.target.width - viewWidth)
      val newVValue = point.y / (cameraPane.target.height - viewHeight)
      val clampedHValue = newHValue.coerceIn(0.0, 1.0)
      val clampedVValue = newVValue.coerceIn(0.0, 1.0)

      if (timeline?.status == Animation.Status.RUNNING) return
      timeline =
          Timeline(
                  KeyFrame(
                      Duration.millis(10.0),
                      {
                        hvalue += (clampedHValue - hvalue) * lerpTime
                        vvalue += (clampedVValue - vvalue) * lerpTime
                        lerpTime += 0.001
                        if (abs(hvalue - clampedHValue) <= 0.01 &&
                            abs(vvalue - clampedVValue) <= 0.01) {
                          timeline?.stop()
                          hvalue = clampedHValue
                          vvalue = clampedVValue
                          lerpTime = 0.0
                        }
                      }))
              .apply { cycleCount = Timeline.INDEFINITE }
      timeline?.play()
    }
  }

  /**
   * Scrolls the view by the specified offset if the resulting point is within the zoomed node's
   * bounds.
   * @param xOffset The horizontal offset.
   * @param yOffset The vertical offset.
   */
  fun scrollBy(xOffset: Double, yOffset: Double) {
    if (inTargetBounds(anchorPoint.add(xOffset, yOffset))) {
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

  private var startX = 0.0
  private var startY = 0.0

  var panMouseButton = MouseButton.MIDDLE

  var hValueLocked = 0.0
  var vValueLocked = 0.0

  var hPanLocked = false
  var vPanLocked = false
  var scrollLocked = false

  var interactive = false

  private fun outerNode(node: Node): Node {
    val outerNode = centeredNode(node)
    outerNode.onScroll = EventHandler { e: ScrollEvent ->
      e.consume()
      if (scrollLocked) return@EventHandler
      if (interactive) {
        val delta =
            when (e.textDeltaXUnits) {
              ScrollEvent.HorizontalTextScrollUnits.NONE -> e.deltaY
              ScrollEvent.HorizontalTextScrollUnits.CHARACTERS -> e.textDeltaY
              null -> 0.0
            }
        onScroll(delta, Point2D(e.x, e.y))
      }
      e.deltaY
    }
    outerNode.onMousePressed = EventHandler { e ->
      if (e.button == panMouseButton && interactive) isPannable = true
    }
    outerNode.onMouseReleased = EventHandler { e ->
      if (e.button == panMouseButton && interactive) isPannable = false
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
