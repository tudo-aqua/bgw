package tools.aqua.bgw.elements

import SceneData
import csstype.*
import emotion.react.Global
import emotion.react.css
import emotion.react.styles
import react.FC
import react.Props
import react.ReactElement
import react.create
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.section
import tools.aqua.bgw.builder.SceneBuilder
import tools.aqua.bgw.toFC

external interface AppProps : Props {
    var data: SceneData
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

            "bgw_color_visual, bgw_image_visual" {
                width = 100.pct
                height = 100.pct
                position = Position.absolute
                left = 0.px
                top = 0.px
                display = Display.block
            }

            "bgw_scene" {
                height = 100.vh
                width = (100 * 16 / 9).vh
                position = Position.relative
                display = Display.flex
                backgroundColor = rgba(0, 0, 0, 0.0)
            }

            ".text" {
                position = Position.absolute
            }

            "#root" {
                width = 100.pct
                height = 100.pct
                position = Position.absolute
                left = 0.px
                top = 0.px
                display = Display.flex
                justifyContent = JustifyContent.center
                alignItems = AlignItems.center
                backgroundColor = Color("#000000")
            }
        }
    }
    section {
        id = "menuScene"
    }
    section {
        id = "boardGameScene"

        +SceneBuilder.build(props.data)
    }
}