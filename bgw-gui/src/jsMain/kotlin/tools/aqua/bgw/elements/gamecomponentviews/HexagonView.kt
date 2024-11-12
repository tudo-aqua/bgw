package tools.aqua.bgw.elements.gamecomponentviews

import ComponentViewData
import HexagonViewData
import PaneData
import csstype.PropertiesBuilder
import data.event.DragEventAction
import web.cssom.*
import data.event.KeyEventAction
import emotion.react.css
import kotlinx.browser.document
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import react.*
import react.dom.aria.ariaDescribedBy
import react.dom.aria.ariaDisabled
import react.dom.aria.ariaPressed
import react.dom.aria.ariaRoleDescription
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.div
import react.dom.svg.ReactSVG
import tools.aqua.bgw.DraggableOptions
import tools.aqua.bgw.DraggableResultTransform
import tools.aqua.bgw.builder.NodeBuilder
import tools.aqua.bgw.builder.ReactConverters.toDragEventData
import tools.aqua.bgw.builder.ReactConverters.toKeyEventData
import tools.aqua.bgw.builder.ReactConverters.toMouseEventData
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.bgwVisuals
import tools.aqua.bgw.elements.cssBuilder
import tools.aqua.bgw.event.JCEFEventDispatcher
import tools.aqua.bgw.handlers
import tools.aqua.bgw.useDraggable
import web.dom.Element
import web.timers.Timeout
import web.timers.clearTimeout
import web.timers.setTimeout
import kotlin.math.sqrt
import kotlin.random.Random

external interface HexagonViewProps : Props {
    var data : HexagonViewData
}

fun PropertiesBuilder.cssBuilderIntern(componentViewData: HexagonViewData) {
    cssBuilder(componentViewData)
    justifyContent = JustifyContent.center
    alignItems = AlignItems.center
}

val HexagonView = FC<HexagonViewProps> { props ->
    val draggable = useDraggable(object : DraggableOptions {
        override var id: String = props.data.id
    })

    val (lastTransform, setLastTransform) = useState<DraggableResultTransform>(object : DraggableResultTransform {
        override var x: Double = 0.0
        override var y: Double = 0.0
        override var scaleX: Double = 1.0
        override var scaleY: Double = 1.0
    })

    useEffect(listOf(draggable.transform)) {
        var resetTimeout: Timeout? = null

        if (draggable.transform != null) {
            resetTimeout?.let { clearTimeout(it) }
            draggable.transform?.let { setLastTransform(it) }
        } else {
            resetTimeout = setTimeout({
                setLastTransform(object : DraggableResultTransform {
                    override var x: Double = 0.0
                    override var y: Double = 0.0
                    override var scaleX: Double = 1.0
                    override var scaleY: Double = 1.0
                })
            }, 100)
        }
    }

    val elementRef = useRef<Element>(null)

    bgwHexagonView {
        tabIndex = 0
        id = props.data.id
        className = ClassName("hexagonView")

        ref = elementRef
        useEffect {
            elementRef.current?.let { draggable.setNodeRef(it) }
        }

        css {
            cssBuilderIntern(props.data)
            width = (sqrt(3.0) * props.data.size).em
            height = 2 * props.data.size.em
            transform = translate(lastTransform.x.px, lastTransform.y.px)
            cursor = if(props.data.isDraggable) Cursor.pointer else Cursor.default
        }

        bgwVisuals {
            className = ClassName("visuals")
            +VisualBuilder.build(props.data.visual)
        }

        if(props.data.isDraggable) {
            onPointerDown = {
                draggable.listeners.onPointerDown.invoke(it, props.data.id)
            }
        }

        onContextMenu = {
            it.preventDefault()
            JCEFEventDispatcher.dispatchEvent(it.toMouseEventData(id))
        }
        onContextMenu = {
            it.preventDefault()
            JCEFEventDispatcher.dispatchEvent(it.toMouseEventData(id))
        }
        onClick = { JCEFEventDispatcher.dispatchEvent(it.toMouseEventData(id)) }
        onKeyDown = { JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(id, KeyEventAction.PRESS)) }
        onKeyUp = { JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(id, KeyEventAction.RELEASE)) }

        ariaDescribedBy = draggable.attributes.ariaDescribedBy
        ariaDisabled = draggable.attributes.ariaDisabled
        ariaPressed = draggable.attributes.ariaPressed
        ariaRoleDescription = draggable.attributes.ariaRoleDescription
    }
}

inline val bgwHexagonView: IntrinsicType<HTMLAttributes<Element>>
    get() = "bgw_hexagon_view".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()