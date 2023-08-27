package tools.aqua.bgw.elements.uicomponents

import ButtonData
import csstype.*
import emotion.react.css
import org.w3c.dom.HTMLDivElement
import react.FC
import react.IntrinsicType
import react.Props
import react.dom.html.HTMLAttributes
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.bgwText
import tools.aqua.bgw.elements.bgwVisuals
import tools.aqua.bgw.elements.cssBuilder
import tools.aqua.bgw.event.JCEFEventDispatcher
import tools.aqua.bgw.builder.ReactConverters.toMouseEventData

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
        onClick = {
            JCEFEventDispatcher.dispatchEvent(it.toMouseEventData(id))
        }
    }
}

inline val bgwButton: IntrinsicType<HTMLAttributes<HTMLDivElement>>
    get() = "bgw_button".unsafeCast<IntrinsicType<HTMLAttributes<HTMLDivElement>>>()