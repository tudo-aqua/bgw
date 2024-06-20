package tools.aqua.bgw.elements.gamecomponentviews

import ComponentViewData
import HexagonViewData
import PaneData
import csstype.PropertiesBuilder
import web.cssom.*
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
import tools.aqua.bgw.elements.bgwVisuals
import tools.aqua.bgw.elements.cssBuilder
import tools.aqua.bgw.event.JCEFEventDispatcher
import tools.aqua.bgw.handlers
import web.dom.Element
import kotlin.math.sqrt

external interface HexagonViewProps : Props {
    var data : HexagonViewData
}

fun PropertiesBuilder.cssBuilderIntern(componentViewData: HexagonViewData) {
    cssBuilder(componentViewData)
    justifyContent = JustifyContent.center
    alignItems = AlignItems.center
}

val HexagonView = FC<HexagonViewProps> { props ->
    bgwHexagonView {
        tabIndex = 0
        id = props.data.id
        className = ClassName("hexagonView")
        css {
            cssBuilderIntern(props.data)
            width = (sqrt(3.0) * props.data.size).rem
            height = 2 * props.data.size.rem
        }

        bgwVisuals {
            className = ClassName("visuals")
            +VisualBuilder.build(props.data.visual)
        }

        onContextMenu = {
            it.preventDefault()
            JCEFEventDispatcher.dispatchEvent(it.toMouseEventData(id)) 
        }
        onClick = { JCEFEventDispatcher.dispatchEvent(it.toMouseEventData(id)) }
        onKeyDown = { JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(id, KeyEventAction.PRESS)) }
        onKeyUp = { JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(id, KeyEventAction.RELEASE)) }
    }
}

inline val bgwHexagonView: IntrinsicType<HTMLAttributes<Element>>
    get() = "bgw_hexagon_view".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()