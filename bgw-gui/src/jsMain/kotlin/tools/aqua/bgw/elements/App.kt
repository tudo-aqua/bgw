package tools.aqua.bgw.elements

import csstype.*
import emotion.react.Global
import emotion.react.styles
import react.FC
import react.Props
import react.ReactElement
import react.create
import tools.aqua.bgw.toFC

external interface AppProps : Props {
    var components: List<ReactElement<*>>
}

val App = FC<AppProps> { props ->
    Global {
        styles {
            "html" {
                fontSize = (100 / 720.0).vh
                width = 100.vw
                height = 100.vh
                margin = 0.px
                overflow = Overflow.hidden
            }
            "body" {
                backgroundColor = rgb(255, 255, 255)
                color = rgb(0, 0, 0)
                margin = 0.px
            }

            ".visuals" {
                width = 100.pct
                height = 100.pct
                position = Position.absolute
                left = 0.px
                top = 0.px
            }

            "div[data-type='visual']" {
                width = 100.pct
                height = 100.pct
                position = Position.absolute
                left = 0.px
                top = 0.px
            }
        }
    }
    props.components.toFC().create()
}