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

package tools.aqua.bgw.components.layoutviews

import Data
import InternalCameraPaneData
import kotlinx.serialization.Serializable
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.observable.properties.BooleanProperty
import tools.aqua.bgw.observable.properties.DoubleProperty
import tools.aqua.bgw.observable.properties.Property
import tools.aqua.bgw.util.Coordinate
import tools.aqua.bgw.visual.Visual

/**
 * A pane representing a camera view that can be used to display and manipulate a target layout
 * view.
 * @param posX The x-coordinate of the camera pane's position on the screen. Default is 0.
 * @param posY The y-coordinate of the camera pane's position on the screen. Default is 0.
 * @param width The width of the camera pane.
 * @param height The height of the camera pane.
 * @param visual The visual representation of the camera pane. Default is an empty visual.
 * @param target The target layout view that this camera pane will display.
 * @param T The type of the target layout view. Must extend the LayoutView class.
 */
open class CameraPane<T : LayoutView<*>>(
    posX: Number = 0,
    posY: Number = 0,
    width: Number,
    height: Number,
    visual: Visual = Visual.EMPTY,
    internal val target: T
) : ComponentView(posX = posX, posY = posY, width = width, height = height, visual = visual) {
    /**
     * [Property] for the [zoom] state of the [CameraPane].
     *
     * @see zoom
     */
    val zoomProperty: DoubleProperty = DoubleProperty(1)

    internal var internalData: InternalCameraPaneData = InternalCameraPaneData()

    /** Zoom factor of the camera starting from 1. */
    var zoom: Double
        get() = zoomProperty.value
        set(value) {
            zoomProperty.value = value
        }

    /**
     * [Property] for the [interactive] state of the [CameraPane].
     *
     * @see zoom
     */
    val interactiveProperty: BooleanProperty = BooleanProperty(false)

    /**
     * Determines if the camera pane is interactive, which means that you can scroll to zoom and drag
     * to move around.
     */
    var interactive: Boolean
        get() = interactiveProperty.value
        set(value) {
            interactiveProperty.value = value
        }

    /** Upper-left corner of the current scrolling window. It is initialized at (0,0) */
    val scroll: Coordinate
        get() = anchorPoint

    internal val anchorPointProperty: Property<Coordinate> = Property(Coordinate())

    internal var anchorPoint: Coordinate
        get() = anchorPointProperty.value
        set(value) {
            if (value.xCoord in 0.0..target.width && value.yCoord in 0.0..target.height) {
                anchorPointProperty.value = value
            }
        }

    /**
     * pans the view of the camera to the specified coordinates. The coordinates specified represent
     * the upper-left corner of the view.
     * @param x The x-coordinate to scroll to.
     * @param y The y-coordinate to scroll to.
     */
    fun pan(x: Number, y: Number) {
        anchorPoint = Coordinate(x, y)
    }

    /**
     * pans the view of the camera by the given offsets.
     * @param xOffset The amount to pan the view horizontally.
     * @param yOffset The amount to pan the view vertically.
     */
    fun panBy(xOffset: Number, yOffset: Number) {
        anchorPoint =
            Coordinate(anchorPoint.xCoord + xOffset.toDouble(), anchorPoint.yCoord + yOffset.toDouble())
    }

    override fun removeChild(component: ComponentView) {
        throw UnsupportedOperationException("This $this ComponentView has no children.")
    }
}