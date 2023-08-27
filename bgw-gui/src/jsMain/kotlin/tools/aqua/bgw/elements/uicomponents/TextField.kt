package tools.aqua.bgw.elements.uicomponents

import TextFieldData
import csstype.*
import data.event.KeyEventAction
import emotion.react.css
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLInputElement
import react.FC
import react.IntrinsicType
import react.Props
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML.input
import tools.aqua.bgw.builder.ReactConverters.toKeyEventData
import tools.aqua.bgw.builder.ReactConverters.toMouseEventData
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.elements.*
import tools.aqua.bgw.event.JCEFEventDispatcher

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

        bgwVisuals {
            className = ClassName("visuals")
            VisualBuilder.build(props.data.visual).forEach {
                +it
            }
        }

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
        }

        onClick = { JCEFEventDispatcher.dispatchEvent(it.toMouseEventData(id)) }
        onKeyDown = { JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(id, KeyEventAction.PRESS)) }
        onKeyUp = { JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(id, KeyEventAction.RELEASE)) }
        onKeyPress = { JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(id, KeyEventAction.TYPE)) }
    }
}

inline val bgwTextField: IntrinsicType<HTMLAttributes<HTMLInputElement>>
    get() = "bgw_text_field".unsafeCast<IntrinsicType<HTMLAttributes<HTMLInputElement>>>()