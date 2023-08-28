package tools.aqua.bgw.elements.uicomponents

import TextFieldData
import csstype.*
import data.event.KeyEventAction
import data.event.internal.SelectionChangedEventData
import data.event.internal.TextInputChangedEventData
import emotion.react.css
import kotlinx.browser.document
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLSelectElement
import react.*
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML.input
import tools.aqua.bgw.builder.ReactConverters.toKeyEventData
import tools.aqua.bgw.builder.ReactConverters.toMouseEventData
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.elements.*
import tools.aqua.bgw.event.JCEFEventDispatcher
import tools.aqua.bgw.handlers

external interface TextFieldProps : Props {
    var data: TextFieldData
}

fun PropertiesBuilder.cssBuilderIntern(componentViewData: TextFieldData) {
    cssBuilder(componentViewData)
}

val TextField = FC<TextFieldProps> { props ->
    bgwTextField {
        id = props.data.id
        className = ClassName("textField")
        css {
            cssBuilderIntern(props.data)
        }

        +VisualBuilder.build(props.data.visual)

        input {
            placeholder = props.data.prompt
            defaultValue = props.data.text
            css {
                fontBuilder(props.data)
                inputBuilder(props.data)

                placeholder {
                    fontBuilder(props.data)
                    opacity = number(0.5)
                }
                textIndent = 20.rem
            }
            onChange = {
                val value = it.target.value
                //println("Text changed $value")
                JCEFEventDispatcher.dispatchEvent(TextInputChangedEventData(value).apply { id = props.data.id })
            }
        }

        onClick = { JCEFEventDispatcher.dispatchEvent(it.toMouseEventData(id)) }
        onKeyDown = { JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(id, KeyEventAction.PRESS)) }
        onKeyUp = { JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(id, KeyEventAction.RELEASE)) }
        onKeyPress = { JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(id, KeyEventAction.TYPE)) }
    }
}

inline val bgwTextField: IntrinsicType<HTMLAttributes<HTMLInputElement>>
    get() = "bgw_text_field".unsafeCast<IntrinsicType<HTMLAttributes<HTMLInputElement>>>()