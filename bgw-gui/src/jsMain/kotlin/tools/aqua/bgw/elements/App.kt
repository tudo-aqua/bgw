package tools.aqua.bgw.elements

import SceneData
import csstype.*
import emotion.react.Global
import emotion.react.css
import emotion.react.styles
import org.w3c.dom.HTMLDivElement
import react.*
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.section
import tools.aqua.bgw.builder.SceneBuilder
import tools.aqua.bgw.toFC
import tools.aqua.bgw.webSocket

external interface AppProps : Props {
    var data: SceneData
}

val App = FC<AppProps> { props ->
    useEffect {
        webSocket?.send("Root component did mount")
    }
    Global {
        styles {
            "html" {
                fontSize = (100 / 720.0).vh
                width = 100.vw
                height = 100.vh
                margin = 0.px
                overflow = Overflow.hidden
                userSelect = None.none
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
                overflow = Overflow.hidden
            }

            "bgw_hexagon_view" {
                clipPath = polygonPath("0% 25%, 0% 75%, 50% 100%, 100% 75%, 100% 25%, 50% 0%")
            }

            "bgw_camera_pane" {
                overflow = Overflow.hidden
            }

            "bgw_camera_target" {
                width = fit()
                height = fit()
            }

            "bgw_camera_target > *" {
                position = important(Position.relative)
            }

            ".text, .components" {
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
    bgwMenuScene {
        id = "menuScene"
    }
    bgwGameScene {
        id = "boardGameScene"

        +SceneBuilder.build(props.data)
    }
}

inline val bgwMenuScene: IntrinsicType<HTMLAttributes<HTMLDivElement>>
    get() = "bgw_menu_scene".unsafeCast<IntrinsicType<HTMLAttributes<HTMLDivElement>>>()

inline val bgwGameScene: IntrinsicType<HTMLAttributes<HTMLDivElement>>
    get() = "bgw_game_scene".unsafeCast<IntrinsicType<HTMLAttributes<HTMLDivElement>>>()

inline val bgwVisuals: IntrinsicType<HTMLAttributes<HTMLDivElement>>
    get() = "bgw_visuals".unsafeCast<IntrinsicType<HTMLAttributes<HTMLDivElement>>>()

inline val bgwContents: IntrinsicType<HTMLAttributes<HTMLDivElement>>
    get() = "bgw_contents".unsafeCast<IntrinsicType<HTMLAttributes<HTMLDivElement>>>()

inline val bgwText: IntrinsicType<HTMLAttributes<HTMLDivElement>>
    get() = "bgw_text".unsafeCast<IntrinsicType<HTMLAttributes<HTMLDivElement>>>()

inline fun polygonPath(
    value: String,
): ClipPath =
    "polygon($value)".unsafeCast<ClipPath>()

inline fun fit(): LengthType.FitContent =
    "fit-content".unsafeCast<LengthType.FitContent>()