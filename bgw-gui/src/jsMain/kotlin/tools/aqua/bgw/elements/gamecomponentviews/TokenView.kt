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
import tools.aqua.bgw.builder.ReactConverters.toMouseEnteredData
import tools.aqua.bgw.builder.ReactConverters.toMouseEventData
import tools.aqua.bgw.builder.ReactConverters.toMouseExitedData
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.bgwVisuals
import tools.aqua.bgw.elements.cssBuilder
import tools.aqua.bgw.event.JCEFEventDispatcher
import tools.aqua.bgw.event.applyCommonEventHandlers
import web.cssom.*
import web.dom.Element
import web.events.Event
import web.html.HTMLButtonElement
import web.timers.Timeout
import web.timers.clearTimeout
import web.timers.setTimeout

internal external interface TokenViewProps : Props {
    var data: TokenViewData
}

internal fun PropertiesBuilder.cssBuilderIntern(componentViewData: TokenViewData) {
    cssBuilder(componentViewData)
}

internal val TokenView = FC<TokenViewProps> { props ->
    val draggable = useDraggable(object : DraggableOptions {
        override var id: String = props.data.id
        override var disabled = !props.data.isDraggable
    })

    val droppable = useDroppable(object : DroppableOptions {
        override var id: String = props.data.id
        override var disabled = !props.data.isDroppable
    })


    val style: PropertiesBuilder.() -> Unit = {
        cssBuilderIntern(props.data)
        translate = "${draggable.transform?.x?.px ?: 0.px} ${draggable.transform?.y?.px ?: 0.px}".unsafeCast<Translate>()
        cursor = if(props.data.isDraggable) Cursor.pointer else Cursor.default
    }

    val elementRef = useRef<Element>(null)

    bgwTokenView {
        id = props.data.id
        className = ClassName("tokenView")

        ref = elementRef
        useEffect {
            elementRef.current?.let { draggable.setNodeRef(it) }
            elementRef.current?.let { droppable.setNodeRef(it) }
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

        applyCommonEventHandlers(props.data)

        ariaDescribedBy = draggable.attributes.ariaDescribedBy
        ariaDisabled = draggable.attributes.ariaDisabled
        ariaPressed = draggable.attributes.ariaPressed
        ariaRoleDescription = draggable.attributes.ariaRoleDescription
    }
}

internal inline val bgwTokenView: IntrinsicType<HTMLAttributes<Element>>
    get() = "bgw_token_view".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()