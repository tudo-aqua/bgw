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

import javafx.scene.Node
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
import tools.aqua.bgw.builder.FXConverters.toFXColor
import tools.aqua.bgw.builder.FXConverters.toFXImage
import tools.aqua.bgw.components.gamecomponentviews.HexagonView
import tools.aqua.bgw.visual.*

object HexagonBuilder {
  /** Degrees in a circle. */
  private const val FULL_CIRCLE_DEGREES = 360

  /** Amount of sides in a hexagon. */
  private const val HEXAGON_SIDES = 6

  private const val BORDER_WIDTH = 50.0

  /** Builds [HexagonView]. */
  internal fun buildHexagonView(hexagonView: HexagonView): Node {
    val points = generatePoints(hexagonView.size.toDouble())
    return when (val visual = hexagonView.visual) {
      is TextVisual -> buildPolygon(points, visual)
      is SingleLayerVisual -> buildPolygon(points, visual)
      is CompoundVisual -> buildPolygon(points, visual)
    }.also { hexagonView.visual = Visual.EMPTY }
  }

  private fun buildPolygon(points: DoubleArray, compoundVisual: CompoundVisual): Node =
      StackPane(
              *compoundVisual.children
                  .map {
                    when (it) {
                      is TextVisual -> buildPolygon(points, it)
                      else -> buildPolygon(points, it)
                    }
                  }
                  .toTypedArray())
          .apply { isPickOnBounds = false }

  private fun buildPolygon(points: DoubleArray, visual: SingleLayerVisual): Node =
      Polygon(*points).apply {
        val paint = buildPaint(visual)
        fill = paint
        visual.transparencyProperty.addListenerAndInvoke(visual.transparency) { _, nV ->
          opacity = nV
        }
        stroke = Color.BLACK
        strokeType = StrokeType.INSIDE
        // roundCorners(paint)
      }

  private fun buildPolygon(points: DoubleArray, visual: TextVisual): Node =
      StackPane(
              buildPolygon(points, visual as SingleLayerVisual),
              VisualBuilder.buildVisual(visual).apply { isPickOnBounds = false })
          .apply { isPickOnBounds = false }

  private fun Polygon.roundCorners(paint: Paint) {
    stroke = paint
    strokeWidth = BORDER_WIDTH
    strokeType = StrokeType.CENTERED
    strokeLineJoin = StrokeLineJoin.ROUND
    strokeLineCap = StrokeLineCap.ROUND
    strokeMiterLimit = BORDER_WIDTH
  }

  private fun generatePoints(size: Double): DoubleArray {
    val points = mutableListOf<Double>()
    var angle = 90.0
    for (i in 0 until HEXAGON_SIDES) {
      val x = size * cos(Math.toRadians(angle)) + size
      val y = size * sin(Math.toRadians(angle)) + size
      angle += FULL_CIRCLE_DEGREES / HEXAGON_SIDES
      points.add(x)
      points.add(y)
    }
    return points.toDoubleArray()
  }

  private fun buildPaint(visual: SingleLayerVisual): Paint {
    return when (visual) {
      is ColorVisual -> visual.color.toFXColor()
      is ImageVisual -> ImagePattern(visual.image.toFXImage())
      is TextVisual -> Color.TRANSPARENT
    }
  }
}
