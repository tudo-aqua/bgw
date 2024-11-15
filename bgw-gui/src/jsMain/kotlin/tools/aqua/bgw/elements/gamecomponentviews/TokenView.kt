package tools.aqua.bgw.elements.gamecomponentviews

import TokenViewData
import csstype.PropertiesBuilder
import data.event.DragEventAction
import data.event.KeyEventAction
import emotion.react.css
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.DragEvent
import org.w3c.dom.HTMLElement
import react.*
import react.dom.aria.ariaDescribedBy
import react.dom.aria.ariaDisabled
import react.dom.aria.ariaPressed
import react.dom.aria.ariaRoleDescription
import react.dom.createPortal
import react.dom.events.SyntheticEvent
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.Unselectable.Companion.on
import tools.aqua.bgw.*
import tools.aqua.bgw.builder.ReactConverters.toDragEventData
import tools.aqua.bgw.builder.ReactConverters.toKeyEventData
import tools.aqua.bgw.builder.ReactConverters.toMouseEventData
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.bgwVisuals
import tools.aqua.bgw.elements.cssBuilder
import tools.aqua.bgw.event.JCEFEventDispatcher
import web.cssom.*
import web.dom.Element
import web.events.Event
import web.html.HTMLButtonElement
import web.timers.Timeout
import web.timers.clearTimeout
import web.timers.setTimeout

external interface TokenViewProps : Props {
    var data: TokenViewData
}

fun PropertiesBuilder.cssBuilderIntern(componentViewData: TokenViewData) {
    cssBuilder(componentViewData)
}

val TokenView = FC<TokenViewProps> { props ->
    val draggable = useDraggable(object : DraggableOptions {
        override var id: String = props.data.id
    })

    val (lastTransform, setLastTransform) = useState<DraggableResultTransform>(object : DraggableResultTransform {
        override var x: Double = 0.0
        override var y: Double = 0.0
        override var scaleX: Double = 1.0
        override var scaleY: Double = 1.0
    })

    val style: PropertiesBuilder.() -> Unit = {
        cssBuilderIntern(props.data)
        transform = translate(draggable.transform?.x?.px ?: 0.px, draggable.transform?.y?.px ?: 0.px)
        cursor = if(props.data.isDraggable) Cursor.pointer else Cursor.default
    }

    /* useEffect(listOf(draggable.transform)) {
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
            }, 200)
        }
    } */

    val elementRef = useRef<Element>(null)

    bgwTokenView {
        id = props.data.id
        className = ClassName("tokenView")

        ref = elementRef
        useEffect {
            elementRef.current?.let { draggable.setNodeRef(it) }
        }
        css(style)

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

inline val bgwTokenView: IntrinsicType<HTMLAttributes<Element>>
    get() = "bgw_token_view".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()