package tools.aqua.bgw.elements.uicomponents

import ButtonData
import ColorVisualData
import csstype.PropertiesBuilder
import web.cssom.*
import data.event.KeyEventAction
import emotion.react.css
import org.w3c.dom.HTMLDivElement
import react.*
import react.dom.html.HTMLAttributes
import tools.aqua.bgw.*
import tools.aqua.bgw.builder.ReactConverters.toKeyEventData
import tools.aqua.bgw.builder.ReactConverters.toMouseEnteredData
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.bgwText
import tools.aqua.bgw.elements.bgwVisuals
import tools.aqua.bgw.elements.cssBuilder
import tools.aqua.bgw.event.JCEFEventDispatcher
import tools.aqua.bgw.builder.ReactConverters.toMouseEventData
import tools.aqua.bgw.builder.ReactConverters.toMouseExitedData
import tools.aqua.bgw.elements.cssTextBuilder
import tools.aqua.bgw.elements.visual.ColorVisual
import tools.aqua.bgw.event.applyCommonEventHandlers
import web.dom.Element

external interface ButtonProps : Props {
    var data : ButtonData
}

fun PropertiesBuilder.cssBuilderIntern(componentViewData: ButtonData) {
    cssBuilder(componentViewData)
    cursor = Cursor.pointer

    hover {
        "> .visuals" {
            filter = brightness(1.1)
        }
    }
}

fun PropertiesBuilder.cssTextBuilderIntern(componentViewData: ButtonData) {
    cssTextBuilder(componentViewData)
}

val Button = FC<ButtonProps> { props ->
    var droppable : DroppableResult? = null

    if(props.data.isDroppable) {
        droppable = useDroppable(object : DroppableOptions {
            override var id: String = props.data.id
        })
    }

    val elementRef = useRef<Element>(null)

    bgwButton {
        tabIndex = 0
        id = props.data.id
        className = ClassName("button")
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

        bgwText {
            className = ClassName("text")
            css {
                cssTextBuilderIntern(props.data)
            }
            +props.data.text
        }

        applyCommonEventHandlers(props.data)
    }
}

inline val bgwButton: IntrinsicType<HTMLAttributes<Element>>
    get() = "bgw_button".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()