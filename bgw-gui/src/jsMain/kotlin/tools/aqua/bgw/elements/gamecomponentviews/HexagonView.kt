package tools.aqua.bgw.elements.gamecomponentviews

import ComponentViewData
import HexagonViewData
import PaneData
import csstype.*
import data.event.KeyEventAction
import emotion.react.css
import kotlinx.browser.document
import org.w3c.dom.HTMLDivElement
import react.FC
import react.IntrinsicType
import react.Props
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.div
import react.dom.svg.ReactSVG
import react.useEffect
import tools.aqua.bgw.builder.NodeBuilder
import tools.aqua.bgw.builder.ReactConverters.toKeyEventData
import tools.aqua.bgw.builder.ReactConverters.toMouseEventData
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.bgwVisuals
import tools.aqua.bgw.elements.cssBuilder
import tools.aqua.bgw.event.JCEFEventDispatcher
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
        id = props.data.id
        className = ClassName("hexagonView")
        css {
            cssBuilderIntern(props.data)
            width = (sqrt(3.0) * props.data.size).rem
            height = 2 * props.data.size.rem
        }

        bgwVisuals {
            className = ClassName("visuals")
            VisualBuilder.build(props.data.visual).forEach {
                +it
            }
        }
        onClick = { JCEFEventDispatcher.dispatchEvent(it.toMouseEventData(id)) }
        onKeyDown = { JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(id, KeyEventAction.PRESS)) }
        onKeyUp = { JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(id, KeyEventAction.RELEASE)) }
        onKeyPress = { JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(id, KeyEventAction.TYPE)) }
    }
}

inline val bgwHexagonView: IntrinsicType<HTMLAttributes<HTMLDivElement>>
    get() = "bgw_hexagon_view".unsafeCast<IntrinsicType<HTMLAttributes<HTMLDivElement>>>()