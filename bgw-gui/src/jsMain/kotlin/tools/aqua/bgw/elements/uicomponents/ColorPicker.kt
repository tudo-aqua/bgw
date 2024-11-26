package tools.aqua.bgw.elements.uicomponents

import ColorPickerData
import TextFieldData
import csstype.PropertiesBuilder
import web.cssom.*
import data.event.KeyEventAction
import data.event.internal.ColorInputChangedEventData
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

external interface ColorPickerProps : Props {
    var data: ColorPickerData
}

fun PropertiesBuilder.cssBuilderIntern(componentViewData: ColorPickerData) {
    cssBuilder(componentViewData)
}

val ColorPicker = FC<ColorPickerProps> { props ->
    val droppable = useDroppable(object : DroppableOptions {
        override var id: String = props.data.id
        override var disabled = !props.data.isDroppable
    })

    val elementRef = useRef<Element>(null)

    bgwColorPicker {
        id = props.data.id
        className = ClassName("colorPicker")
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
            type = InputType.color
            defaultValue = props.data.selectedColor
            value = props.data.selectedColor
            css {
                position = Position.absolute
                top = 0.px
                left = 0.px
                width = 100.pct
                height = 100.pct
                margin = 0.px
                padding = 0.px
                paddingLeft = 5.em
                paddingRight = 5.em
                paddingTop = 3.em
                paddingBottom = 3.em
                appearance = None.none
                border = None.none
                outline = None.none
                backgroundColor = rgb(0, 0, 0, 0.0)
            }

            onChange = {
                val value = it.target.value
                //println("Text changed $value")
                JCEFEventDispatcher.dispatchEvent(ColorInputChangedEventData(value).apply { id = props.data.id })
            }
        }

        applyCommonEventHandlers(props.data)
    }
}

inline val bgwColorPicker: IntrinsicType<HTMLAttributes<Element>>
    get() = "bgw_color_picker".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()