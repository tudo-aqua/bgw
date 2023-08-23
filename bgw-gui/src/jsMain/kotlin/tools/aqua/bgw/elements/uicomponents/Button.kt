package tools.aqua.bgw.elements.uicomponents

import ButtonData
import csstype.*
import emotion.react.css
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
    }
}

inline val bgwButton: IntrinsicType<HTMLAttributes<HTMLDivElement>>
    get() = "bgw_button".unsafeCast<IntrinsicType<HTMLAttributes<HTMLDivElement>>>()