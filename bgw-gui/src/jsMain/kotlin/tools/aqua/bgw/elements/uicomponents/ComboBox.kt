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
import tools.aqua.bgw.builder.ReactConverters.toKeyEventData
import tools.aqua.bgw.builder.ReactConverters.toMouseEventData
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.*
import tools.aqua.bgw.event.JCEFEventDispatcher
import tools.aqua.bgw.handlers
import web.dom.Element

external interface ComboBoxProps : Props {
    var data: ComboBoxData
}

fun PropertiesBuilder.cssBuilderIntern(componentViewData: ComboBoxData) {
    cssBuilder(componentViewData)
}

val ComboBox = FC<ComboBoxProps> { props ->
    bgwComboBox {
        id = props.data.id
        className = ClassName("comboBox")
        css {
            cssBuilderIntern(props.data)
        }

        bgwVisuals {
            className = ClassName("visuals")
            +VisualBuilder.build(props.data.visual)
        }

        select {
            placeholder = props.data.prompt
            css {
                fontBuilder(props.data)
                comboBoxBuilder(props.data)
                placeholder {
                    fontBuilder(props.data)
                    opacity = number(0.5)
                }
                position = Position.absolute
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
                println("Selection changed $value")
                JCEFEventDispatcher.dispatchEvent(SelectionChangedEventData(value).apply { id = props.data.id })
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

inline val bgwComboBox: IntrinsicType<HTMLAttributes<Element>>
    get() = "bgw_combo_box".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()