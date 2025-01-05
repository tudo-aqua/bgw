package tools.aqua.bgw.elements.uicomponents

import TextFieldData
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

internal external interface TextFieldProps : Props {
    var data: TextFieldData
}

internal fun PropertiesBuilder.cssBuilderIntern(componentViewData: TextFieldData) {
    cssBuilder(componentViewData)
}

internal val TextField = FC<TextFieldProps> { props ->
    val droppable = useDroppable(object : DroppableOptions {
        override var id: String = props.data.id
        override var disabled = !props.data.isDroppable
    })

    val elementRef = useRef<Element>(null)

    bgwTextField {
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
            placeholder = props.data.prompt
            defaultValue = props.data.text
            css {
                inputBuilder(props.data)
                fontBuilder(props.data)
                outline = None.none
                border = None.none
                textIndent = 1.em

                placeholder {
                    placeholderFontBuilder(props.data)
                }
            }
            onChange = {
                val value = it.target.value
                JCEFEventDispatcher.dispatchEvent(TextInputChangedEventData(value).apply { id = props.data.id })
            }
        }

        applyCommonEventHandlers(props.data)
    }
}

internal inline val bgwTextField: IntrinsicType<HTMLAttributes<Element>>
    get() = "bgw_text_field".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()