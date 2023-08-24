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
import tools.aqua.bgw.builder.VisualBuilder
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
    transition = Transition.easeInOut
    transitionDuration = 0.2.s

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

        ReactHTML.div {
            className = ClassName("visuals")
            +VisualBuilder.build(props.data.visual)
        }

        ReactHTML.h1 {
            className = ClassName("text")
            +props.data.text
        }
        onClick = {
            val mouseEventData = MouseEventData(posX = it.clientX,
                posY = it.clientY,
                button = MouseButtonType.LEFT_BUTTON,
                id = this@bgwButton.id ?: "")

            val keyEventData = KeyEventData(this@bgwButton.id ?: "", KeyCode.K, "", false, false, false)

            val eventsData = EventsData().apply {
                eventData = listOf(mouseEventData, keyEventData)
            }

            val eventJson : String = mapper.encodeToString(eventsData)

            println("Clicked $eventJson")
            webSocket?.send(eventJson)
        }
    }
}

inline val bgwButton: IntrinsicType<HTMLAttributes<HTMLDivElement>>
    get() = "bgw_button".unsafeCast<IntrinsicType<HTMLAttributes<HTMLDivElement>>>()