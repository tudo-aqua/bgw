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
import tools.aqua.bgw.builder.FXConverters.toFXColor
import tools.aqua.bgw.builder.FXConverters.toFXImage
import tools.aqua.bgw.components.gamecomponentviews.HexagonView
import tools.aqua.bgw.visual.*
import kotlin.math.cos
import kotlin.math.sin

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
        StackPane(*compoundVisual.children.map { buildPolygon(points, it) }.toTypedArray())

    private fun buildPolygon(points: DoubleArray, visual: SingleLayerVisual): Node = Polygon(*points).apply {
        val paint = buildPaint(visual)
        fill = paint
        roundCorners(paint)
    }

    private fun buildPolygon(points: DoubleArray, visual: TextVisual): Node = StackPane(
        buildPolygon(points, visual as SingleLayerVisual),
        VisualBuilder.buildVisual(visual)
    )

    private fun Polygon.roundCorners(paint: Paint) {
        stroke = paint
        strokeWidth = BORDER_WIDTH
        strokeType = StrokeType.OUTSIDE
        strokeLineJoin = StrokeLineJoin.ROUND
        strokeLineCap = StrokeLineCap.ROUND
        strokeMiterLimit = BORDER_WIDTH
    }

    private fun generatePoints(size: Double): DoubleArray {
        val points = mutableListOf<Double>()
        val r = size / 2 - BORDER_WIDTH
        var angle = 90.0
        for (i in 0 until HEXAGON_SIDES) {
            val x = r * cos(Math.toRadians(angle)) + r
            val y = r * sin(Math.toRadians(angle)) + r
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