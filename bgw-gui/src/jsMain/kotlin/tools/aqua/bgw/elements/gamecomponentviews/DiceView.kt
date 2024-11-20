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
import tools.aqua.bgw.DraggableOptions
import tools.aqua.bgw.DraggableResultTransform
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
import tools.aqua.bgw.handlers
import tools.aqua.bgw.useDraggable
import web.dom.Element
import web.timers.Timeout
import web.timers.clearTimeout
import web.timers.setTimeout

external interface DiceViewProps : Props {
    var data: DiceViewData
}

fun PropertiesBuilder.cssBuilderIntern(componentViewData: DiceViewData) {
    cssBuilder(componentViewData)
}

val DiceView = FC<DiceViewProps> { props ->
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

    bgwDiceView {
        id = props.data.id
        className = ClassName("diceView")

        ref = elementRef
        useEffect {
            elementRef.current?.let { draggable.setNodeRef(it) }
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

inline val bgwDiceView: IntrinsicType<HTMLAttributes<Element>>
    get() = "bgw_dice_view".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()