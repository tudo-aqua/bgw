package tools.aqua.bgw.elements.gamecomponentviews

import DiceViewData
import TokenViewData
import csstype.PropertiesBuilder
import web.cssom.*
import data.event.KeyEventAction
import emotion.react.css
import org.w3c.dom.HTMLDivElement
import react.*
import react.dom.aria.ariaDescribedBy
import react.dom.aria.ariaDisabled
import react.dom.aria.ariaPressed
import react.dom.aria.ariaRoleDescription
import react.dom.html.HTMLAttributes
import tools.aqua.bgw.*
import tools.aqua.bgw.builder.ReactConverters.toKeyEventData
import tools.aqua.bgw.builder.ReactConverters.toMouseEnteredData
import tools.aqua.bgw.builder.ReactConverters.toMouseEventData
import tools.aqua.bgw.builder.ReactConverters.toMouseExitedData
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.bgwText
import tools.aqua.bgw.elements.bgwVisuals
import tools.aqua.bgw.elements.cssBuilder
import tools.aqua.bgw.event.JCEFEventDispatcher
import tools.aqua.bgw.event.applyCommonEventHandlers
import web.dom.Element
import web.timers.Timeout
import web.timers.clearTimeout
import web.timers.setTimeout

internal external interface DiceViewProps : Props {
    var data: DiceViewData
}

internal fun PropertiesBuilder.cssBuilderIntern(componentViewData: DiceViewData) {
    cssBuilder(componentViewData)
}

internal val DiceView = FC<DiceViewProps> { props ->
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

    bgwDiceView {
        id = props.data.id
        className = ClassName("diceView")

        ref = elementRef
        useEffect {
            elementRef.current?.let { draggable.setNodeRef(it) }
            elementRef.current?.let { droppable.setNodeRef(it) }
        }

        css(style)

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

internal inline val bgwDiceView: IntrinsicType<HTMLAttributes<Element>>
    get() = "bgw_dice_view".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()