package tools.aqua.bgw.elements.container

import ComponentViewData
import HexagonGridData
import HexagonViewData
import PaneData
import csstype.*
import data.event.KeyEventAction
import emotion.react.css
import kotlinx.browser.document
import org.w3c.dom.HTMLDivElement
import react.*
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.div
import react.dom.svg.ReactSVG
import tools.aqua.bgw.builder.NodeBuilder
import tools.aqua.bgw.builder.ReactConverters.toKeyEventData
import tools.aqua.bgw.builder.ReactConverters.toMouseEventData
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.bgwContents
import tools.aqua.bgw.elements.bgwVisuals
import tools.aqua.bgw.elements.cssBuilder
import tools.aqua.bgw.event.JCEFEventDispatcher
import tools.aqua.bgw.handlers
import kotlin.math.abs
import kotlin.math.sqrt

external interface HexagonGridProps : Props {
    var data : HexagonGridData
}

fun PropertiesBuilder.cssBuilderIntern(componentViewData: HexagonGridData) {
    cssBuilder(componentViewData)
    justifyContent = JustifyContent.flexStart
    alignItems = AlignItems.flexStart
}

val HexagonGrid = FC<HexagonGridProps> { props ->
    val (data, setData) = useState(props.data)

    useEffect {
        handlers[props.data.id] = { newData ->
            if(newData is HexagonGridData) {
                println("Updating HexagonGrid ${props.data.id}")
                setData(newData)
            }
        }
    }
    
    bgwHexagonGrid {
        tabIndex = 0
        id = data.id
        className = ClassName("hexagonGrid")
        css {
            cssBuilderIntern(data)
        }

        +VisualBuilder.build(data.visual)

        bgwContents {
            className = ClassName("components")

            var minX = 0.0
            var maxX = 0.0
            var minY = 0.0
            var maxY = 0.0

            data.map.forEach {
                if(data.coordinateSystem == "offset") {
                    bgwHexagonContent {
                        val size = it.value.size
                        val w = (size * sqrt(3.0)).toString().substring(0, 3).toDouble()
                        val h = 2 * size
                        val q = it.key.split("/")[0].toInt()
                        val r = it.key.split("/")[1].toInt()

                        val x = if(r % 2 == 0)
                            w * q + data.spacing * (q - 1)
                        else
                            w * q + data.spacing * (q - 1) + w / 2

                        val y = h * 0.75 * r + data.spacing * (r - 1)

                        if(x < minX)
                            minX = x
                        if(x + w > maxX)
                            maxX = x + w
                        if(y < minY)
                            minY = y
                        if(y + h > maxY)
                            maxY = y + h

                        css {
                            position = Position.absolute
                            left = x.rem
                            top = y.rem
                        }
                        +NodeBuilder.build(it.value)
                    }
                } else {
                    bgwHexagonContent {
                        val size = it.value.size
                        val w = (size * sqrt(3.0)).toString().substring(0, 3).toDouble()
                        val h = 2 * size
                        var q = it.key.split("/")[0].toInt()
                        var r = it.key.split("/")[1].toInt()

                        q = q + (r - (r and 1)) / 2

                        val x = if(r % 2 == 0)
                            w * q + data.spacing * (q - 1)
                        else
                            w * q + data.spacing * (q - 1) + w / 2

                        val y = h * 0.75 * r + data.spacing * (r - 1)

                        if(x < minX)
                            minX = x
                        if(x + w > maxX)
                            maxX = x + w
                        if(y < minY)
                            minY = y
                        if(y + h > maxY)
                            maxY = y + h

                        css {
                            position = Position.absolute
                            left = x.rem
                            top = y.rem
                        }
                        +NodeBuilder.build(it.value)
                    }
                }
                css {
                    position = Position.absolute
                    width = (maxX + abs(minX)).rem
                    height = (maxY + abs(minY)).rem
                    left = (-minX).rem
                    top = (-minY).rem
                }
            }
        }
        onClick = { JCEFEventDispatcher.dispatchEvent(it.toMouseEventData(id)) }
        onKeyDown = { JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(id, KeyEventAction.PRESS)) }
        onKeyUp = { JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(id, KeyEventAction.RELEASE)) }
        onKeyPress = { JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(id, KeyEventAction.TYPE)) }
    }
}

inline val bgwHexagonGrid: IntrinsicType<HTMLAttributes<HTMLDivElement>>
    get() = "bgw_hexagon_grid".unsafeCast<IntrinsicType<HTMLAttributes<HTMLDivElement>>>()

inline val bgwHexagonContent: IntrinsicType<HTMLAttributes<HTMLDivElement>>
    get() = "bgw_hexagon_content".unsafeCast<IntrinsicType<HTMLAttributes<HTMLDivElement>>>()