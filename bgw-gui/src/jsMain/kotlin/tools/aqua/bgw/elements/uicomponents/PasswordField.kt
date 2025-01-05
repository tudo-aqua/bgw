package tools.aqua.bgw.elements.uicomponents

import PasswordFieldData
import csstype.PropertiesBuilder
import web.cssom.*
import data.event.TextInputChangedEventData
import emotion.react.css
import react.*
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML.input
import tools.aqua.bgw.DroppableOptions
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.*
import tools.aqua.bgw.event.JCEFEventDispatcher
import tools.aqua.bgw.event.applyCommonEventHandlers
import tools.aqua.bgw.useDroppable
import web.dom.Element
import web.html.InputType

internal external interface PasswordFieldProps : Props {
    var data: PasswordFieldData
}

internal fun PropertiesBuilder.cssBuilderIntern(componentViewData: PasswordFieldData) {
    cssBuilder(componentViewData)
}

internal val PasswordField = FC<PasswordFieldProps> { props ->
    val droppable = useDroppable(object : DroppableOptions {
        override var id: String = props.data.id
        override var disabled = !props.data.isDroppable
    })

    val elementRef = useRef<Element>(null)

    bgwPasswordField {
        id = props.data.id
        className = ClassName("passwordField")
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
            placeholder = props.data.prompt
            type = InputType.password
            defaultValue = props.data.text
            css {
                fontBuilder(props.data)
                inputBuilder(props.data)
                outline = None.none
                border = None.none
                textIndent = 1.em

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

        applyCommonEventHandlers(props.data)
    }
}

internal inline val bgwPasswordField: IntrinsicType<HTMLAttributes<Element>>
    get() = "bgw_password_field".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()