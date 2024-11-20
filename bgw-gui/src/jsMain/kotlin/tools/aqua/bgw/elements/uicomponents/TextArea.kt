package tools.aqua.bgw.elements.uicomponents

import PasswordFieldData
import TextAreaData
import TextFieldData
import csstype.PropertiesBuilder
import web.cssom.*
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
import react.dom.html.ReactHTML.textarea
import tools.aqua.bgw.builder.ReactConverters.toKeyEventData
import tools.aqua.bgw.builder.ReactConverters.toMouseEventData
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.elements.*
import tools.aqua.bgw.event.JCEFEventDispatcher
import tools.aqua.bgw.handlers
import web.dom.Element
import web.html.InputType

external interface TextAreaProps : Props {
    var data: TextAreaData
}

fun PropertiesBuilder.cssBuilderIntern(componentViewData: TextAreaData) {
    cssBuilder(componentViewData)
}

val TextArea = FC<TextAreaProps> { props ->
    bgwTextArea {
        id = props.data.id
        className = ClassName("textArea")
        css {
            cssBuilderIntern(props.data)
        }

        bgwVisuals {
            className = ClassName("visuals")
            +VisualBuilder.build(props.data.visual)
        }

        textarea {
            placeholder = props.data.prompt
            defaultValue = props.data.text
            css {
                fontBuilder(props.data)
                inputBuilder(props.data)
                resize = None.none
                boxSizing = BoxSizing.borderBox
                outline = None.none
                border = None.none

                placeholder {
                    placeholderFontBuilder(props.data)
                }
            }
            onChange = {
                val value = it.target.value
                //println("Text changed $value")
                JCEFEventDispatcher.dispatchEvent(TextInputChangedEventData(value).apply { id = props.data.id })
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

inline val bgwTextArea: IntrinsicType<HTMLAttributes<Element>>
    get() = "bgw_text_area".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()