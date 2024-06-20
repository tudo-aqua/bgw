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
import tools.aqua.bgw.builder.ReactConverters.toKeyEventData
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.bgwText
import tools.aqua.bgw.elements.bgwVisuals
import tools.aqua.bgw.elements.cssBuilder
import tools.aqua.bgw.event.JCEFEventDispatcher
import tools.aqua.bgw.builder.ReactConverters.toMouseEventData
import tools.aqua.bgw.elements.visual.ColorVisual
import tools.aqua.bgw.handlers
import tools.aqua.bgw.webSocket
import web.dom.Element

external interface ButtonProps : Props {
    var data : ButtonData
}

fun PropertiesBuilder.cssBuilderIntern(componentViewData: ButtonData) {
    cssBuilder(componentViewData)
    justifyContent = JustifyContent.center
    alignItems = AlignItems.center
    cursor = Cursor.pointer

    hover {
        "> .visuals" {
            filter = brightness(1.1)
        }
    }
}

val Button = FC<ButtonProps> { props ->
    bgwButton {
        tabIndex = 0
        id = props.data.id
        className = ClassName("button")
        css {
            cssBuilderIntern(props.data)
        }

        bgwVisuals {
            className = ClassName("visuals")
            +VisualBuilder.build(props.data.visual)
        }

        bgwText {
            className = ClassName("text")
            +props.data.text
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

inline val bgwButton: IntrinsicType<HTMLAttributes<Element>>
    get() = "bgw_button".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()