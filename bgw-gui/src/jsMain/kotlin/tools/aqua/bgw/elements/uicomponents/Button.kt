package tools.aqua.bgw.elements.uicomponents

import ButtonData
import EventsData
import KeyEventData
import MouseEventData
import csstype.*
import emotion.react.css
import kotlinx.serialization.encodeToString
import mapper
import org.w3c.dom.HTMLDivElement
import react.FC
import react.IntrinsicType
import react.Props
import react.dom.html.ButtonType
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.bgwText
import tools.aqua.bgw.elements.bgwVisuals
import tools.aqua.bgw.elements.cssBuilder
import tools.aqua.bgw.event.KeyCode
import tools.aqua.bgw.event.MouseButtonType
import tools.aqua.bgw.webSocket

external interface ButtonProps : Props {
    var data : ButtonData
}

fun PropertiesBuilder.cssBuilderIntern(componentViewData: ButtonData) {
    cssBuilder(componentViewData)
    justifyContent = JustifyContent.center
    alignItems = AlignItems.center
    fontSize = 15.rem
    cursor = Cursor.pointer

    hover {
        "> .visuals" {
            filter = brightness(1.1)
        }
    }
}

val Button = FC<ButtonProps> { props ->
    bgwButton {
        id = props.data.id
        className = ClassName("button")
        css {
            cssBuilderIntern(props.data)
        }

        bgwVisuals {
            className = ClassName("visuals")
            VisualBuilder.build(props.data.visual).forEach {
                +it
            }
        }

        bgwText {
            className = ClassName("text")
            +props.data.text
        }
    }
}

inline val bgwButton: IntrinsicType<HTMLAttributes<HTMLDivElement>>
    get() = "bgw_button".unsafeCast<IntrinsicType<HTMLAttributes<HTMLDivElement>>>()