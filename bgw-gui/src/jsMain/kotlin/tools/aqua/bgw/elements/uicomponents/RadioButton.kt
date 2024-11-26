package tools.aqua.bgw.elements.uicomponents

import CheckBoxData
import RadioButtonData
import TextFieldData
import csstype.PropertiesBuilder
import web.cssom.*
import data.event.KeyEventAction
import data.event.internal.CheckBoxChangedEventData
import data.event.internal.RadioChangedEventData
import data.event.internal.SelectionChangedEventData
import data.event.internal.TextInputChangedEventData
import emotion.react.css
import kotlinx.browser.document
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLSelectElement
import react.*
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.label
import tools.aqua.bgw.DroppableOptions
import tools.aqua.bgw.DroppableResult
import tools.aqua.bgw.builder.ReactConverters.toKeyEventData
import tools.aqua.bgw.builder.ReactConverters.toMouseEnteredData
import tools.aqua.bgw.builder.ReactConverters.toMouseEventData
import tools.aqua.bgw.builder.ReactConverters.toMouseExitedData
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.elements.*
import tools.aqua.bgw.event.JCEFEventDispatcher
import tools.aqua.bgw.event.applyCommonEventHandlers
import tools.aqua.bgw.handlers
import tools.aqua.bgw.useDroppable
import web.dom.Element
import web.html.InputType

external interface RadioButtonProps : Props {
    var data: RadioButtonData
}

fun PropertiesBuilder.cssBuilderIntern(componentViewData: RadioButtonData) {
    cssBuilder(componentViewData)
    display = Display.flex
    alignItems = AlignItems.center
    justifyItems = JustifyItems.flexStart
    gap = 10.em
}

val RadioButton = FC<RadioButtonProps> { props ->
    val droppable = useDroppable(object : DroppableOptions {
        override var id: String = props.data.id
        override var disabled = !props.data.isDroppable
    })

    val elementRef = useRef<Element>(null)

    bgwRadioButton {
        id = props.data.id
        className = ClassName("textField")
        css {
            cssBuilderIntern(props.data)
        }

        ref = elementRef
        useEffect {
            elementRef.current?.let { droppable.setNodeRef(it) }
        }

        bgwVisuals {
            className = ClassName("visuals")
            +VisualBuilder.build(props.data.visual)
        }

        input {
            type = InputType.radio
            id = props.data.id + "--radio"
            checked = props.data.isSelected
            name = props.data.group

            css {
                width = 20.em
                height = 20.em
                maxWidth = 20.em
                zIndex = integer(1)
                outline = None.none
                border = None.none
            }
            onChange = {
                JCEFEventDispatcher.dispatchEvent(RadioChangedEventData(!props.data.isSelected).apply { id = props.data.id })
            }
        }

        label {
            className = ClassName("text")
            htmlFor = props.data.id + "--radio"
            +props.data.text

            css {
                fontBuilder(props.data)
                alignmentBuilder(props.data)
                display = Display.flex
                width = 100.pct
                height = 100.pct
                position = Position.relative
            }
        }

        applyCommonEventHandlers(props.data)
    }
}

inline val bgwRadioButton: IntrinsicType<HTMLAttributes<Element>>
    get() = "bgw_radiobutton".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()