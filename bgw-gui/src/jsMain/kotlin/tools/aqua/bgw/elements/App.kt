package tools.aqua.bgw.elements

import SceneData
import csstype.*
import emotion.react.Global
import emotion.react.css
import emotion.react.styles
import kotlinx.browser.document
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.events.Event
import react.*
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.section
import tools.aqua.bgw.builder.SceneBuilder
import tools.aqua.bgw.toFC
import tools.aqua.bgw.webSocket
import webViewType

external interface AppProps : Props {
    var data: SceneData
}

val App = FC<AppProps> { props ->
    useEffect {
        webSocket?.send("Hello from Client!")
    }
    Global {
        styles {
            props.data.fonts.forEach { font ->
                fontFace {
                    fontFamily = font.second
                    fontStyle = FontStyle.normal
                    fontWeight = integer(font.third)
                    src = "url(static/${font.first})"
                }
            }

            "html" {
                fontSize = (100 / props.data.height).vh
                width = 100.vw
                height = 100.vh
                margin = 0.px
                overflow = Overflow.hidden
                userSelect = None.none
            }
            "body" {
                backgroundColor = rgb(0, 0, 0)
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

            "bgw_color_visual, bgw_image_visual, bgw_text_visual" {
                width = 100.pct
                height = 100.pct
                position = Position.absolute
                left = 0.px
                top = 0.px
                display = Display.flex
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

            "bgw_grid_element > *" {
                position = important(Position.relative)
                left = important(0.px)
                top = important(0.px)
            }

            ".text, .components" {
                position = Position.absolute
            }

            "bgw_linear_layout > bgw_contents > *" {
                position = important(Position.relative)
                left = important(Globals.unset)
                top = important(Globals.unset)
                flexGrow = number(0.0)
                flexShrink = number(0.0)
            }

            "bgw_hexagon_grid > bgw_contents > bgw_hexagon_content > *" {
                position = important(Position.relative)
                left = important(Globals.unset)
                top = important(Globals.unset)
                flexGrow = number(0.0)
                flexShrink = number(0.0)
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
                overflow = Overflow.hidden
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

inline fun minContent(): GridTemplateTracks =
    "min-content".unsafeCast<GridTemplateTracks>()