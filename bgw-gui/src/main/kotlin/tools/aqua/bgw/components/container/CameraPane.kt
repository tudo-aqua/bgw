package tools.aqua.bgw.components.container

import javafx.geometry.Point2D
import tools.aqua.bgw.components.DynamicComponentView
import tools.aqua.bgw.core.DEFAULT_BOARD_HEIGHT
import tools.aqua.bgw.core.DEFAULT_BOARD_WIDTH
import tools.aqua.bgw.observable.properties.DoubleProperty
import tools.aqua.bgw.observable.properties.Property
import tools.aqua.bgw.util.Coordinate
import tools.aqua.bgw.visual.Visual

//TODO: Support ComponentView
//TODO: ZoomTo MoveTo Methods
open class CameraPane<T : DynamicComponentView>(
    posX: Number = 0,
    posY: Number = 0,
    width: Number = DEFAULT_BOARD_WIDTH,
    height: Number = DEFAULT_BOARD_HEIGHT,
    visual: Visual = Visual.EMPTY,
) :
    GameComponentContainer<T>(
        posX = posX, posY = posY, width = width, height = height, visual = visual
    ) {

    val contentWidth: Double = 5000.0
    val contentHeight: Double = 5000.0

    val zoomProperty: DoubleProperty = DoubleProperty(1)

    var zoom: Double
        get() = zoomProperty.value
        set(value) {
            if(value >= 1) zoomProperty.value = value
        }

    val scroll: Coordinate
        get() = anchorPoint

    internal val anchorPointProperty: Property<Coordinate> = Property(Coordinate())

    internal var anchorPoint: Coordinate
        get() = anchorPointProperty.value
        set(value) {
            if(value.xCoord in 0.0..contentWidth && value.yCoord in 0.0..contentHeight) {
                anchorPointProperty.value = value
            }
        }

    fun scrollTo(x: Number, y: Number) {
        anchorPoint = Coordinate(x, y)
    }

    /** Internal onRemove handler. */
    override fun T.onRemove() = Unit

    /** Internal onAdd handler. */
    override fun T.onAdd() = Unit
}