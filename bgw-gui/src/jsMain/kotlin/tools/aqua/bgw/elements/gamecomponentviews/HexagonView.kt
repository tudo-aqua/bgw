package tools.aqua.bgw.elements.gamecomponentviews

import ComponentViewData
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
import tools.aqua.bgw.elements.bgwVisuals
import tools.aqua.bgw.elements.cssBuilder
import tools.aqua.bgw.event.JCEFEventDispatcher
import tools.aqua.bgw.handlers
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
    val (data, setData) = useState(props.data)

    useEffect {
        handlers[props.data.id] = { newData ->
            if(newData is HexagonViewData) {
                println("Updating HexagonView ${props.data.id}")
                setData(newData)
            }
        }
    }
    
    bgwHexagonView {
        tabIndex = 0
        id = data.id
        className = ClassName("hexagonView")
        css {
            cssBuilderIntern(data)
            width = (sqrt(3.0) * data.size).rem
            height = 2 * data.size.rem
        }

        bgwVisuals {
            className = ClassName("visuals")
            VisualBuilder.build(data.visual).forEach {
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