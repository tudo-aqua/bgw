package tools.aqua.bgw.elements.uicomponents

import ComboBoxData
import csstype.PropertiesBuilder
import web.cssom.*
import data.event.KeyEventAction
import data.event.internal.SelectionChangedEventData
import emotion.react.css
import kotlinx.browser.document
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLSelectElement
import react.*
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML.option
import react.dom.html.ReactHTML.select
import tools.aqua.bgw.DroppableOptions
import tools.aqua.bgw.DroppableResult
import tools.aqua.bgw.builder.ReactConverters.toKeyEventData
import tools.aqua.bgw.builder.ReactConverters.toMouseEnteredData
import tools.aqua.bgw.builder.ReactConverters.toMouseEventData
import tools.aqua.bgw.builder.ReactConverters.toMouseExitedData
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.*
import tools.aqua.bgw.event.JCEFEventDispatcher
import tools.aqua.bgw.event.applyCommonEventHandlers
import tools.aqua.bgw.handlers
import tools.aqua.bgw.useDroppable
import web.dom.Element

external interface ComboBoxProps : Props {
    var data: ComboBoxData
}

fun PropertiesBuilder.cssBuilderIntern(componentViewData: ComboBoxData) {
    cssBuilder(componentViewData)
}

val ComboBox = FC<ComboBoxProps> { props ->
    var droppable : DroppableResult? = null

    if(props.data.isDroppable) {
        droppable = useDroppable(object : DroppableOptions {
            override var id: String = props.data.id
        })
    }

    val elementRef = useRef<Element>(null)

    bgwComboBox {
        id = props.data.id
        className = ClassName("comboBox")
        css {
            cssBuilderIntern(props.data)
        }

        ref = elementRef

        if(props.data.isDroppable) {
            useEffect {
                elementRef.current?.let { droppable!!.setNodeRef(it) }
            }
        }

        bgwVisuals {
            className = ClassName("visuals")
            +VisualBuilder.build(props.data.visual)
        }

        select {
            css {
                fontBuilder(props.data)
                comboBoxBuilder(props.data)
                outline = None.none
                border = None.none
                position = Position.absolute
                textIndent = 1.em
            }
            option {
                value = (-1).toString()
                +props.data.prompt
                selected = props.data.selectedItem == null
            }
            props.data.items.forEach {
                option {
                    value = it.first.toString()
                    +it.second
                    selected = props.data.selectedItem?.first == it.first
                }
            }
            onChange = {
                val value = it.target.value.toInt()
                JCEFEventDispatcher.dispatchEvent(SelectionChangedEventData(value).apply { id = props.data.id })
            }
        }

        applyCommonEventHandlers(props.data)
    }
}

inline val bgwComboBox: IntrinsicType<HTMLAttributes<Element>>
    get() = "bgw_combo_box".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()