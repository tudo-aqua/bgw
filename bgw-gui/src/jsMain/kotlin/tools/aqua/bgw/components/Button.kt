package tools.aqua.bgw.components

import csstype.Color
import csstype.px
import csstype.rgb
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.ButtonType
import react.dom.html.ReactHTML.button

external interface ButtonProps : Props {
    var id: String
    var color: String
}

val ReactButton = FC<ButtonProps> { props ->
    button {
        css {
            padding = 5.px
            backgroundColor = Color(props.color)
            color = rgb(56, 246, 137)
        }
        id = props.id
        type = ButtonType.button
        value = "Click me"
        +"Click me"
    }
}