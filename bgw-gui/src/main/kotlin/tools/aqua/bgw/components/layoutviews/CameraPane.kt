package tools.aqua.bgw.components.layoutviews

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.core.DEFAULT_BOARD_HEIGHT
import tools.aqua.bgw.core.DEFAULT_BOARD_WIDTH
import tools.aqua.bgw.observable.properties.DoubleProperty
import tools.aqua.bgw.observable.properties.Property
import tools.aqua.bgw.util.Coordinate
import tools.aqua.bgw.visual.Visual

//TODO: Support ComponentView
//TODO: ZoomTo MoveTo Methods
open class CameraPane<T : LayoutView<*>>(
    posX: Number = 0,
    posY: Number = 0,
    width: Number = DEFAULT_BOARD_WIDTH,
    height: Number = DEFAULT_BOARD_HEIGHT,
    visual: Visual = Visual.EMPTY,
    internal val target: T
) :
    ComponentView(
        posX = posX, posY = posY, width = width, height = height, visual = visual
    ) {
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
            if(value.xCoord in 0.0..target.width && value.yCoord in 0.0..target.height) {
                anchorPointProperty.value = value
            }
        }

    fun scrollTo(x: Number, y: Number) {
        anchorPoint = Coordinate(x, y)
    }

    override fun removeChild(component: ComponentView) {
        target.removeChild(component)
    }
}