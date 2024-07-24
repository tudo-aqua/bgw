package tools.aqua.bgw.elements

import Action
import AppData
import web.cssom.*
import emotion.react.Global
import emotion.react.styles
import org.w3c.dom.HTMLDivElement
import react.FC
import react.IntrinsicType
import react.Props
import react.dom.html.HTMLAttributes
import react.useEffect
import tools.aqua.bgw.builder.SceneBuilder
import tools.aqua.bgw.core.DEFAULT_BLUR_RADIUS
import tools.aqua.bgw.core.DEFAULT_MENU_SCENE_OPACITY
import tools.aqua.bgw.webSocket
import web.dom.Element

external interface AppProps : Props {
    var data: AppData
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

            // BLUE
            "@media (min-aspect-ratio: ${props.data.width}/${props.data.height}) and (orientation: landscape)" {
                "html" {
                    fontSize = (100.0 / props.data.height).vh
                    width = 100.vw
                    height = 100.vh
                    margin = 0.px
                    overflow = Overflow.hidden
                    userSelect = None.none
                }

                "bgw_scenes" {
                    height = 100.vh
                    width = (100.0 / props.data.height * props.data.width).vh
                    position = Position.relative
                    backgroundColor = rgb(0, 0, 0, 0.0)
                    overflow = Overflow.hidden
                }
            }

            // RED
            "@media (max-aspect-ratio: ${props.data.width}/${props.data.height}) and (orientation: landscape)" {
                "html" {
                    fontSize = (100.0 / props.data.width).vw
                    width = 100.vw
                    height = 100.vh
                    margin = 0.px
                    overflow = Overflow.hidden
                    userSelect = None.none
                }

                "bgw_scenes" {
                    width = 100.vw
                    height = (100.0 / props.data.width * props.data.height).vw
                    position = Position.relative
                    backgroundColor = rgb(0, 0, 0, 0.0)
                    overflow = Overflow.hidden
                }
            }

            // GREEN
            "@media (min-aspect-ratio: ${props.data.width}/${props.data.height}) and (orientation: portrait)" {
                "html" {
                    fontSize = (100.0 / props.data.height).vh
                    width = 100.vw
                    height = 100.vh
                    margin = 0.px
                    overflow = Overflow.hidden
                    userSelect = None.none
                }

                "bgw_scenes" {
                    height = 100.vh
                    width = (100.0 / props.data.height * props.data.width).vh
                    position = Position.relative
                    backgroundColor = rgb(0, 0, 0, 0.0)
                    overflow = Overflow.hidden
                }
            }

            // YELLOW
            "@media (max-aspect-ratio: ${props.data.width}/${props.data.height}) and (orientation: portrait)" {
                "html" {
                    fontSize = (100.0 / props.data.width).vw
                    width = 100.vw
                    height = 100.vh
                    margin = 0.px
                    overflow = Overflow.hidden
                    userSelect = None.none
                }

                "bgw_scenes" {
                    width = 100.vw
                    height = (100.0 / props.data.width * props.data.height).vw
                    position = Position.relative
                    backgroundColor = rgb(0, 0, 0, 0.0)
                    overflow = Overflow.hidden
                }
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
                overflow = Overflow.hidden
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
                height = 100.pct
                width = 100.pct
                inset = 0.px
                position = Position.absolute
                display = Display.flex
                backgroundColor = rgb(0, 0, 0, 0.0)
                overflow = Overflow.hidden
            }
            "bgw_menu_scene > bgw_scene > bgw_visuals" {
                opacity = number(DEFAULT_MENU_SCENE_OPACITY)
            }
            "bgw_menu_scene > bgw_scene" {
                opacity = number(0.0)
                backdropFilter = blur(0.px)
            }
            "bgw_hexagon_view" {
                clipPath = polygonPath("0% 25%, 0% 75%, 50% 100%, 100% 75%, 100% 25%, 50% 0%")
            }

            "bgw_menu_scene.scene--visible > bgw_scene" {
                opacity = number(1.0)
                backdropFilter = blur(DEFAULT_BLUR_RADIUS.rem)
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
    bgwScenes {
        val gameScene = props.data.gameScene
        if (gameScene != null) {
            bgwGameScene {
                id = "boardGameScene"
                +SceneBuilder.build(gameScene)
            }
        }
        val menuScene = props.data.menuScene
        if (menuScene != null) {
            bgwMenuScene {
                id = "menuScene"
                if(props.data.action !== Action.HIDE_MENU_SCENE && props.data.action !== Action.SHOW_MENU_SCENE) {
                    className = ClassName("scene--visible")
                }
                +SceneBuilder.build(menuScene)
            }
        }
    }
}
inline val bgwScenes: IntrinsicType<HTMLAttributes<Element>>
    get() = "bgw_scenes".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()
inline val bgwMenuScene: IntrinsicType<HTMLAttributes<Element>>
    get() = "bgw_menu_scene".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()

inline val bgwGameScene: IntrinsicType<HTMLAttributes<Element>>
    get() = "bgw_game_scene".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()

inline val bgwVisuals: IntrinsicType<HTMLAttributes<Element>>
    get() = "bgw_visuals".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()

inline val bgwContents: IntrinsicType<HTMLAttributes<Element>>
    get() = "bgw_contents".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()

inline val bgwText: IntrinsicType<HTMLAttributes<Element>>
    get() = "bgw_text".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()

inline fun polygonPath(
    value: String,
): ClipPath =
    "polygon($value)".unsafeCast<ClipPath>()

inline fun fit(): LengthType.FitContent =
    "fit-content".unsafeCast<LengthType.FitContent>()

inline fun minContent(): GridTemplateTracks =
    "min-content".unsafeCast<GridTemplateTracks>()

inline fun menuTransition(): Transition =
    ".3s opacity, .3s backdrop-filter".unsafeCast<Transition>()

inline fun transition(duration : Int, property : String): Transition =
    "${duration}ms $property".unsafeCast<Transition>()
