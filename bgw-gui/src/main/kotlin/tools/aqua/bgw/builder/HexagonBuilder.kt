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

import javafx.scene.layout.Pane
import javafx.scene.layout.Region
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.paint.ImagePattern
import javafx.scene.paint.Paint
import javafx.scene.shape.Polygon
import javafx.scene.shape.StrokeLineCap
import javafx.scene.shape.StrokeLineJoin
import javafx.scene.shape.StrokeType
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import tools.aqua.bgw.builder.FXConverters.toFXColor
import tools.aqua.bgw.builder.FXConverters.toFXImage
import tools.aqua.bgw.components.gamecomponentviews.HexagonView
import tools.aqua.bgw.style.color
import tools.aqua.bgw.style.pixel
import tools.aqua.bgw.visual.*

/**
 * A utility object that generates a hexagon shape and builds a [Region] for the specified
 * [HexagonView].
 */
object HexagonBuilder {
  /** Degrees in a circle. */
  private const val FULL_CIRCLE_DEGREES = 360

  /** Amount of sides in a hexagon. */
  private const val HEXAGON_SIDES = 6

  private const val BORDER_WIDTH = 50.0

  /** Builds [HexagonView]. */
  internal fun buildHexagonView(hexagonView: HexagonView): Region {
    val root = Pane().apply { isPickOnBounds = false }
    val points = generatePoints(hexagonView.size.toDouble())
    hexagonView.visualProperty.setGUIListenerAndInvoke(hexagonView.visual) { _, nV ->
      root.children.clear()
      val component =
          when (nV) {
            is TextVisual -> buildPolygon(points, nV, hexagonView)
            is SingleLayerVisual -> buildPolygon(points, nV, hexagonView)
            is CompoundVisual -> buildPolygon(points, nV, hexagonView)
          }
      root.children.add(component)
    }
    return root
  }

  private fun buildPolygon(
      points: DoubleArray,
      compoundVisual: CompoundVisual,
      hexagonView: HexagonView
  ): Region =
      StackPane(
              *compoundVisual.children
                  .map {
                    when (it) {
                      is TextVisual -> buildPolygon(points, it, hexagonView)
                      else -> buildPolygon(points, it, hexagonView)
                    }
                  }
                  .toTypedArray())
          .apply { isPickOnBounds = false }

  private fun buildPolygon(
      points: DoubleArray,
      visual: SingleLayerVisual,
      hexagonView: HexagonView
  ): Region {
    return Pane(
            Polygon(*points).apply {
              val paint = buildPaint(visual)
              fill = paint
              visual.transparencyProperty.addListenerAndInvoke(visual.transparency) { _, nV ->
                opacity = nV
              }
              visual.borderColorProperty.addListenerAndInvoke(visual.borderColor) { _, nV ->
                stroke = nV?.color?.toFXColor() ?: Color.TRANSPARENT
              }
              strokeType = StrokeType.INSIDE
              visual.borderWidthProperty.addListenerAndInvoke(visual.borderWidth) { _, nV ->
                strokeWidth =
                    if (nV != null && nV.pixel > 0) {
                      nV.pixel.toDouble()
                    } else 0.0
              }
              visual.borderRadiusProperty.addListenerAndInvoke(visual.borderRadius) { _, nV ->
                clip =
                    if (nV != null && nV.pixel > 0) {
                      val size = hexagonView.size.toDouble() - nV.pixel.toDouble()
                      val offsetX = hexagonView.widthProperty.value / 2 - sqrt(3.0) / 2 * size
                      val offsetY = hexagonView.heightProperty.value / 2 - size
                      Polygon(*generatePoints(size, offsetX, offsetY)).apply { roundCorners() }
                    } else null
              }
            })
        .apply { isPickOnBounds = false }
  }
  private fun buildPolygon(
      points: DoubleArray,
      visual: TextVisual,
      hexagonView: HexagonView
  ): Region =
      StackPane(
              buildPolygon(points, visual as SingleLayerVisual, hexagonView),
              VisualBuilder.buildVisual(visual).apply { isPickOnBounds = false })
          .apply { isPickOnBounds = false }

  private fun Polygon.roundCorners() {
    stroke = Color.BLACK
    strokeWidth = BORDER_WIDTH
    strokeType = StrokeType.CENTERED
    strokeLineJoin = StrokeLineJoin.ROUND
    strokeLineCap = StrokeLineCap.ROUND
    strokeMiterLimit = BORDER_WIDTH
  }

  private fun generatePoints(
      size: Double,
      offsetX: Number = 0.0,
      offsetY: Number = 0.0
  ): DoubleArray {
    val points = mutableListOf<Double>()
    var angle = 90.0
    repeat(HEXAGON_SIDES) {
      val x = size * cos(Math.toRadians(angle)) + size / 2 * sqrt(3.0)
      val y = size * sin(Math.toRadians(angle)) + size
      angle += FULL_CIRCLE_DEGREES / HEXAGON_SIDES
      points.add(x + offsetX.toDouble())
      points.add(y + offsetY.toDouble())
    }
    return points.toDoubleArray()
  }

  private fun buildPaint(visual: SingleLayerVisual): Paint =
      when (visual) {
        is ColorVisual -> visual.color.toFXColor()
        is ImageVisual -> ImagePattern(visual.image.toFXImage())
        is TextVisual -> Color.TRANSPARENT
      }
}
