/*
 * Copyright 2021-2023 The BoardGameWork Authors
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

import javafx.animation.Animation
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.animation.Transition
import javafx.event.EventHandler
import javafx.geometry.Point2D
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.control.ScrollPane
import javafx.scene.input.KeyEvent
import javafx.scene.input.ScrollEvent
import javafx.scene.layout.Background
import javafx.scene.layout.Pane
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.util.Duration
import tools.aqua.bgw.builder.NodeBuilder.buildChildren
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.DynamicComponentView
import tools.aqua.bgw.components.container.Board
import tools.aqua.bgw.components.container.GameComponentContainer
import tools.aqua.bgw.core.Scene
import java.io.FileNotFoundException
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.max
import kotlin.math.min

/** ContainerNodeBuilder. Factory for all BGW containers. */
object ContainerNodeBuilder {
    /** Switches between Containers. */
    internal fun buildContainer(
        scene: Scene<out ComponentView>,
        container: GameComponentContainer<out DynamicComponentView>
    ): Region {
        return when (container) {
            is Board -> buildBoard(scene, container)
            else -> Pane().apply {
                container.observableComponents.setGUIListenerAndInvoke(emptyList()) { oldValue, _ ->
                    buildChildren(scene, container.observableComponents, oldValue.toSet())
                }
            }
        }
    }

    private fun buildBoard(
        scene: Scene<out ComponentView>,
        container: GameComponentContainer<out DynamicComponentView>
    ): Region = ZoomableScrollPane(Pane().apply {
        this.minWidth = container.width
        this.minHeight = container.height
        this.background = Background.fill(Color.TRANSPARENT)
        container.observableComponents.setGUIListenerAndInvoke(emptyList()) { oldValue, _ ->
            buildChildren(scene, container.observableComponents, oldValue.toSet())
        }
    }).apply {

        container.onKeyPressed = {
            println(it.keyCode)
            reset()
        }
    }
}


class ZoomableScrollPane(private val target: Node) : ScrollPane() {
    private var scaleValue : Double = 1.0
        set(value) {
            field = max(value, 1.0)
            target.scaleX = scaleValue
            target.scaleY = scaleValue
        }
    private val zoomIntensity = 0.02
    private val zoomNode: Node
    private var timeline : Timeline? = null
    private var lerpTime = 0.0

    init {
        val resource = this::class.java.getResource("/style.css") ?: throw FileNotFoundException()
        stylesheets.add(resource.toExternalForm())
        background = Background.fill(Color.TRANSPARENT)
        zoomNode = Group(target)
        content = outerNode(zoomNode)
        isPannable = true
        hbarPolicy = ScrollBarPolicy.NEVER
        vbarPolicy = ScrollBarPolicy.NEVER
        isFitToHeight = true //center
        isFitToWidth = true //center
    }

    fun reset() {
        if(timeline?.status == Animation.Status.RUNNING) return
        timeline = Timeline(KeyFrame(Duration.millis(10.0), {
            hvalue += (0.0f - hvalue) * lerpTime
            vvalue += (0.0f - vvalue) * lerpTime
            scaleValue += (1.0f - scaleValue) * lerpTime
            lerpTime += 0.001
            if(abs(hvalue - 0.0) <= 0.01 && abs(vvalue - 0.0) <= 0.01 && abs(scaleValue - 1.0) <= 0.01) {
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
        vBox.alignment = Pos.CENTER
        return vBox
    }
    private fun onScroll(wheelDelta: Double, mousePoint: Point2D) {
        if(timeline?.status == Animation.Status.RUNNING) return
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


