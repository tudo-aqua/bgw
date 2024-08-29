package tools.aqua.bgw.elements.uicomponents

import CheckBoxData
import RadioButtonData
import TextFieldData
import ToggleButtonData
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
import react.dom.html.ReactHTML.span
import tools.aqua.bgw.builder.ReactConverters.toKeyEventData
import tools.aqua.bgw.builder.ReactConverters.toMouseEventData
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.elements.*
import tools.aqua.bgw.event.JCEFEventDispatcher
import tools.aqua.bgw.handlers
import web.dom.Element
import web.html.InputType

external interface ToggleButtonProps : Props {
    var data: ToggleButtonData
}

fun PropertiesBuilder.cssBuilderIntern(componentViewData: ToggleButtonData) {
    cssBuilder(componentViewData)
    display = Display.flex
    alignItems = AlignItems.center
    justifyItems = JustifyItems.flexStart
    gap = 10.em
}

val ToggleButton = FC<ToggleButtonProps> { props ->
    bgwToggleButton {
        id = props.data.id
        className = ClassName("textField")
        css {
            cssBuilderIntern(props.data)
        }

        bgwVisuals {
            className = ClassName("visuals")
            +VisualBuilder.build(props.data.visual)
        }

        input {
            type = InputType.checkbox
            id = props.data.id + "--toggle"
            checked = props.data.isSelected
            name = props.data.group

            css {
                width = 20.em
                height = 20.em
                maxWidth = 20.em
                zIndex = integer(1)
            }
            onChange = {
                JCEFEventDispatcher.dispatchEvent(RadioChangedEventData(!props.data.isSelected).apply { id = props.data.id })
            }
        }

        span {
            className = ClassName("toggle")
        }

        label {
            className = ClassName("text")
            htmlFor = props.data.id + "--toggle"
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

        onContextMenu = {
            it.preventDefault()
            JCEFEventDispatcher.dispatchEvent(it.toMouseEventData(id))
        }
        onClick = { JCEFEventDispatcher.dispatchEvent(it.toMouseEventData(id)) }
        onKeyDown = { JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(id, KeyEventAction.PRESS)) }
        onKeyUp = { JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(id, KeyEventAction.RELEASE)) }
    }
}

inline val bgwToggleButton: IntrinsicType<HTMLAttributes<Element>>
    get() = "bgw_togglebutton".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()