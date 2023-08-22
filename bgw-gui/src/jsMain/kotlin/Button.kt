import react.FC
import react.Props
import react.dom.html.ButtonType
import react.dom.html.ReactHTML.button

external interface ButtonProps : Props {
    var id: String
}

val ReactButton = FC<ButtonProps> { props ->
    button {
        id = props.id
        type = ButtonType.button
        value = "Click me"
        +"Click me"
    }
}