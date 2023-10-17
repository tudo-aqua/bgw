package tools.aqua.bgw.elements.uicomponents

import CheckBoxData
import TextFieldData
import csstype.*
import data.event.KeyEventAction
import data.event.internal.CheckBoxChangedEventData
import data.event.internal.SelectionChangedEventData
import data.event.internal.TextInputChangedEventData
import emotion.react.css
import kotlinx.browser.document
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLSelectElement
import react.*
import react.dom.html.HTMLAttributes
import react.dom.html.InputType
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.label
import tools.aqua.bgw.builder.ReactConverters.toKeyEventData
import tools.aqua.bgw.builder.ReactConverters.toMouseEventData
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.elements.*
import tools.aqua.bgw.event.JCEFEventDispatcher
import tools.aqua.bgw.handlers

external interface CheckBoxProps : Props {
    var data: CheckBoxData
}

fun PropertiesBuilder.cssBuilderIntern(componentViewData: CheckBoxData) {
    cssBuilder(componentViewData)
    display = Display.flex
    alignItems = AlignItems.center
    justifyItems = JustifyItems.flexStart
    gap = 10.rem
}

val CheckBox = FC<CheckBoxProps> { props ->
    bgwCheckBox {
        id = props.data.id
        className = ClassName("textField")
        css {
            cssBuilderIntern(props.data)
        }

        +VisualBuilder.build(props.data.visual)

        input {
            type = InputType.checkbox
            id = props.data.id + "--checkbox"
            checked = props.data.isChecked

            css {
                width = 20.rem
                height = 20.rem
                maxWidth = 20.rem
                zIndex = integer(1)
            }
            onChange = {
                JCEFEventDispatcher.dispatchEvent(CheckBoxChangedEventData(!props.data.isChecked).apply { id = props.data.id })
            }
        }

        label {
            className = ClassName("text")
            htmlFor = props.data.id + "--checkbox"
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
        onKeyPress = { JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(id, KeyEventAction.TYPE)) }
    }
}

inline val bgwCheckBox: IntrinsicType<HTMLAttributes<HTMLInputElement>>
    get() = "bgw_checkbox".unsafeCast<IntrinsicType<HTMLAttributes<HTMLInputElement>>>()