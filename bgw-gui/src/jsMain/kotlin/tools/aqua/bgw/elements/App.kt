/*
 * Copyright 2025-2026 The BoardGameWork Authors
 * SPDX-License-Identifier: Apache-2.0
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tools.aqua.bgw.elements

import AppData
import CameraPaneData
import ComponentViewData
import GameComponentContainerData
import GridPaneData
import PaneData
import data.event.DragGestureExitedEventData
import data.event.KeyEventAction
import emotion.react.Global
import emotion.react.css
import emotion.react.styles
import kotlinx.browser.document
import kotlinx.browser.window
import react.*
import react.dom.events.KeyboardEvent
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.img
import tools.aqua.bgw.*
import tools.aqua.bgw.builder.ReactConverters.toDragEndedEventData
import tools.aqua.bgw.builder.ReactConverters.toDragEnteredEventData
import tools.aqua.bgw.builder.ReactConverters.toDragEventData
import tools.aqua.bgw.builder.ReactConverters.toDragMoveEventData
import tools.aqua.bgw.builder.ReactConverters.toDragStartedEventData
import tools.aqua.bgw.builder.ReactConverters.toKeyEventData
import tools.aqua.bgw.builder.SceneBuilder
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.core.DEFAULT_MENU_SCENE_OPACITY
import tools.aqua.bgw.event.JCEFEventDispatcher
import tools.aqua.bgw.hooks.useAnimatedLogo
import tools.aqua.bgw.hooks.useImagePreloading
import web.cssom.*
import web.dom.Element

internal external interface AppProps : Props {
  var data: AppData
}

internal val App =
    FC<AppProps> { props ->
      useEffect { webSocket?.send("Hello from Client!") }

      props.data.fonts.forEach { font ->
        Global {
          styles {
            fontFace {
              fontFamily = font.second
              fontStyle = FontStyle.normal
              fontWeight = integer(font.third)
              src = "url(static/${font.first})"
            }
          }
        }
      }

      Global {
        styles {
          ".bgw-root-container" {
            width = 100.pct
            height = 100.pct
            position = Position.absolute
            left = 0.px
            top = 0.px
            display = Display.flex
            justifyContent = JustifyContent.center
            alignItems = AlignItems.center
            backgroundColor = rgb(0, 0, 0, 0.0)
            overflow = Overflow.hidden
            containerName = bgwContainer()
            containerType = ContainerType.size
          }

          ".bgw-root *" {
            // transformOrigin = TransformOrigin(GeometryPosition.center, GeometryPosition.center)
            border = None.none
            outline = None.none
          }

          // BLUE
          "@container bgwContainer (min-aspect-ratio: ${props.data.width}/${props.data.height}) and (orientation: landscape)" {
            ".bgw-root, .bgw-dialogs" {
              set(CustomPropertyName("--bgwUnit"), (100.0 / props.data.height).cqh)
              width = 100.cqw
              height = 100.cqh
              margin = 0.px
              overflow = Overflow.hidden
              userSelect = None.none
            }

            "bgw_scenes" {
              height = 100.cqh
              width = (100.0 / props.data.height * props.data.width).cqh
              position = Position.relative
              backgroundColor = rgb(0, 0, 0, 0.0)
              overflow = Overflow.hidden
              display = Display.block
            }
          }

          // RED
          "@container bgwContainer (max-aspect-ratio: ${props.data.width}/${props.data.height}) and (orientation: landscape)" {
            ".bgw-root, .bgw-dialogs" {
              set(CustomPropertyName("--bgwUnit"), (100.0 / props.data.width).cqw)
              width = 100.cqw
              height = 100.cqh
              margin = 0.px
              overflow = Overflow.hidden
              userSelect = None.none
            }

            "bgw_scenes" {
              width = 100.cqw
              height = (100.0 / props.data.width * props.data.height).cqw
              position = Position.relative
              backgroundColor = rgb(0, 0, 0, 0.0)
              overflow = Overflow.hidden
              display = Display.block
            }
          }

          // GREEN
          "@container bgwContainer (min-aspect-ratio: ${props.data.width}/${props.data.height}) and (orientation: portrait)" {
            ".bgw-root, .bgw-dialogs" {
              set(CustomPropertyName("--bgwUnit"), (100.0 / props.data.height).cqh)
              width = 100.cqw
              height = 100.cqh
              margin = 0.px
              overflow = Overflow.hidden
              userSelect = None.none
            }

            "bgw_scenes" {
              height = 100.cqh
              width = (100.0 / props.data.height * props.data.width).cqh
              position = Position.relative
              backgroundColor = rgb(0, 0, 0, 0.0)
              overflow = Overflow.hidden
              display = Display.block
            }
          }

          // YELLOW
          "@container bgwContainer (max-aspect-ratio: ${props.data.width}/${props.data.height}) and (orientation: portrait)" {
            ".bgw-root, .bgw-dialogs" {
              set(CustomPropertyName("--bgwUnit"), (100.0 / props.data.width).cqw)
              width = 100.cqw
              height = 100.cqh
              margin = 0.px
              overflow = Overflow.hidden
              userSelect = None.none
            }

            "bgw_scenes" {
              width = 100.cqw
              height = (100.0 / props.data.width * props.data.height).cqw
              position = Position.relative
              backgroundColor = rgb(0, 0, 0, 0.0)
              overflow = Overflow.hidden
              display = Display.block
            }
          }

          ".bgw-root" {
            backgroundColor = rgb(0, 0, 0, 1.0)
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
            zIndex = integer(1000)
          }

          /* "bgw_menu_scene[aria-expanded='true']:after" {
            content = Content("")
            position = Position.absolute
            width = 100.pct
            height = 100.pct
            inset = 0.px
            backgroundColor = rgb(0, 0, 0, 0.0)
            zIndex = integer(999)
            backdropFilter = blur(bgwUnit(DEFAULT_BLUR_RADIUS))
          } */

          "bgw_hexagon_view[aria-details='hex-pointy_top']" {
            clipPath = polygonPath("0% 25%, 0% 75%, 50% 100%, 100% 75%, 100% 25%, 50% 0%")
          }

          "bgw_hexagon_view[aria-details='hex-flat_top']" {
            clipPath = polygonPath("25% 0%, 75% 0%, 100% 50%, 75% 100%, 25% 100%, 0% 50%")
          }

          "bgw_menu_scene[aria-expanded='true'] > bgw_scene" { opacity = number(1.0) }

          "bgw_camera_pane" { overflow = Overflow.hidden }

          "bgw_camera_target" {
            width = fit()
            height = fit()
          }

          "bgw_camera_target > *" { position = important(Position.relative) }

          "bgw_grid_element > *" {
            position = important(Position.relative)
            left = important(0.px)
            top = important(0.px)
          }

          ".text, .components" { position = Position.absolute }

          "bgw_linear_layout > bgw_contents > div > *, bgw_linear_layout > bgw_contents > *" {
            position = important(Position.relative)
            // left = important(Globals.unset)
            // top = important(Globals.unset)
            flexGrow = number(0.0)
            flexShrink = number(0.0)
          }

          "bgw_card_stack > bgw_contents > *" {
            position = important(Position.absolute)
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

          "textarea::-webkit-scrollbar" { display = None.none }

          "input[type='color']::-webkit-color-swatch" {
            borderRadius = bgwUnit(3)
            border = None.none
          }

          "bgw_togglebutton" {
            position = Position.relative
            display = Display.inlineFlex
            width = bgwUnit(30)
            height = bgwUnit(18)
          }

          "bgw_togglebutton > input[type='checkbox']" {
            opacity = number(0.0)
            position = Position.relative
            width = bgwUnit(36)
            minWidth = bgwUnit(36)
            height = 100.pct
          }

          ".toggle" {
            position = Position.absolute
            left = 4.bgw
            width = 30.bgw
            height = 18.bgw
            backgroundColor = rgb(145, 145, 145)
            transition = transition(300, "background-color")
            borderRadius = 3.bgw
          }

          ".toggle::before" {
            content = Content("")
            position = Position.absolute
            width = 12.bgw
            height = 12.bgw
            left = 3.bgw
            top = 3.bgw
            backgroundColor = rgb(255, 255, 255)
            transition = transition(300, "transform")
            borderRadius = 3.bgw
          }

          "bgw_togglebutton > input[type='checkbox']:checked + .toggle" {
            backgroundColor = rgb(0, 117, 255)
          }

          "bgw_togglebutton > input[type='checkbox']:checked + .toggle::before" {
            transform = translatex(12.bgw)
          }

          ".bgw-root" {
            width = 100.pct
            height = 100.pct
            position = Position.absolute
            left = 0.px
            top = 0.px
            display = Display.flex
            justifyContent = JustifyContent.center
            alignItems = AlignItems.center
            backgroundColor = rgb(0, 0, 0, 1.0)
            overflow = Overflow.hidden
          }

          ".bgw-root *[aria-roledescription='draggable'][aria-pressed='true']" {
            position = important(Position.fixed)
            opacity = important(number(1.0))
            zIndex = important(integer(1000000))
          }

          ".bgw-root *:has(*[aria-roledescription='draggable'][aria-pressed='true'])" {
            zIndex = important(integer(1000000))
          }

          "bgw_satchel bgw_contents *[aria-roledescription='draggable']:not([aria-pressed='true'])" {
            width = important(100.pct)
            height = important(100.pct)
          }

          "bgw_scroll::-webkit-scrollbar" { display = None.none }

          // DragOverlay specific styles to ensure visibility
          ".bgw-drag-overlay" {
            zIndex = important(integer(99999998))
            pointerEvents = important(None.none)
          }

          ".bgw-drag-overlay > *" {
            zIndex = important(integer(99999998))
            pointerEvents = important(None.none)
            transform = important(None.none)
          }

          "select, ::picker(select)" {
            appearance = important(baseSelect())
            border = None.none
            backgroundColor = rgb(0, 0, 0, 0.0)
          }

          "option::checkmark, ::picker-icon" { display = None.none }

          "option" {
            minInlineSize = fit()
            minBlockSize = fit()
            paddingBlock = 0.5.bgw
            paddingInline = 0.bgw
          }

          "select" {
            border = None.none
            paddingBlock = 0.bgw
            paddingInline = 0.bgw
            minInlineSize = fit()
            minBlockSize = fit()
          }
        }
      }

      /* div {
          id = "bgw_loading"
          css {
              position = Position.fixed
              width = 100.pct
              height = 100.pct
              backgroundColor = rgb(20,20,20, 1.0)
              overflow = Overflow.hidden
              display = Display.flex
              justifyContent = JustifyContent.center
              alignItems = AlignItems.center
              zIndex = integer(5000000)
          }

          img {
              src = "logo_animated.svg"
          }

          /* useEffect {
              val loadingSVG = document.createElement("svg")
              loadingSVG.id = "eRjslcmEZrC1"
              loadingSVG.setAttribute("xmlns", "http://www.w3.org/2000/svg")
              loadingSVG.setAttribute("xmlns:xlink", "http://www.w3.org/1999/xlink")
              loadingSVG.setAttribute("viewBox", "0 0 262.985 228.649")
              loadingSVG.setAttribute("shape-rendering", "geometricPrecision")
              loadingSVG.setAttribute("text-rendering", "geometricPrecision")
              loadingSVG.setAttribute("width", "2560")
              loadingSVG.setAttribute("height", "1313")

              loadingSVG.innerHTML = """<style>#eRjslcmEZrC2_to {animation: eRjslcmEZrC2_to__to 1500ms linear 1 normal forwards}@keyframes eRjslcmEZrC2_to__to { 0% {transform: translate(-431.597px,-66.928515px);animation-timing-function: cubic-bezier(0.68,-0.005,0.295,1)} 40% {transform: translate(-431.597px,-295.583971px)} 100% {transform: translate(-431.597px,-295.583971px)}} #eRjslcmEZrC2 {animation: eRjslcmEZrC2_c_o 1500ms linear 1 normal forwards}@keyframes eRjslcmEZrC2_c_o { 0% {opacity: 0} 20% {opacity: 0;animation-timing-function: cubic-bezier(0.42,0,0.58,1)} 46.666667% {opacity: 1} 100% {opacity: 1}} #eRjslcmEZrC4_to {animation: eRjslcmEZrC4_to__to 1500ms linear 1 normal forwards}@keyframes eRjslcmEZrC4_to__to { 0% {transform: translate(504.367px,367.785409px)} 33.333333% {transform: translate(504.367px,367.785409px);animation-timing-function: cubic-bezier(0,0,0.09,1.005)} 73.333333% {transform: translate(504.367px,335.551px)} 100% {transform: translate(504.367px,335.551px)}} #eRjslcmEZrC5_to {animation: eRjslcmEZrC5_to__to 1500ms linear 1 normal forwards}@keyframes eRjslcmEZrC5_to__to { 0% {transform: translate(387.431408px,270.893836px)} 26.666667% {transform: translate(387.431408px,270.893836px);animation-timing-function: cubic-bezier(0,0,0.09,1.005)} 86.666667% {transform: translate(387.431404px,200.106453px)} 100% {transform: translate(387.431404px,200.106453px)}} #eRjslcmEZrC8_ts {animation: eRjslcmEZrC8_ts__ts 1500ms linear 1 normal forwards}@keyframes eRjslcmEZrC8_ts__ts { 0% {transform: translate(460.728003px,432.108415px) scale(0,0)} 13.333333% {transform: translate(460.728003px,432.108415px) scale(0,0);animation-timing-function: cubic-bezier(0,0,0.09,1.005)} 73.333333% {transform: translate(460.728003px,432.108415px) scale(1,1)} 100% {transform: translate(460.728003px,432.108415px) scale(1,1)}}</style><defs><linearGradient id="eRjslcmEZrC3-fill" x1="0.53" y1="0.19" x2="0.837" y2="0.938" spreadMethod="pad" gradientUnits="objectBoundingBox" gradientTransform="translate(0 0)"><stop id="eRjslcmEZrC3-fill-0" offset="0%" stop-color="#c9326f"/><stop id="eRjslcmEZrC3-fill-1" offset="100%" stop-color="#ff7e7e"/></linearGradient><linearGradient id="eRjslcmEZrC8-fill" x1="-0.047" y1="0.5" x2="0.919" y2="0.585" spreadMethod="pad" gradientUnits="objectBoundingBox" gradientTransform="translate(0 0)"><stop id="eRjslcmEZrC8-fill-0" offset="0%" stop-color="#c9326f"/><stop id="eRjslcmEZrC8-fill-1" offset="100%" stop-color="#ff7e7e"/></linearGradient></defs><g id="eRjslcmEZrC2_to" transform="translate(-431.597,-66.928515)"><g id="eRjslcmEZrC2" transform="translate(0,0)" opacity="0"><path d="M99.263,169.169c-3.654373-.001758-7.261972-.821822-10.558-2.4l-70.633-33.78v0h-.006l-.015-.006L18,132.96h-.007l-.109-.052c-.523-.259-1.742-.982-1.755-2.1.253-4.828.2-15.658-3.019-33.937C11.458,88.2,7.4,78.851,4.428,72.025L4.419,72v0-.009c-1.183465-2.550343-2.2378-5.158644-3.159-7.815-.711379-2.236509-1.129537-4.555873-1.244-6.9-.206903-4.151434.998609-8.250527,3.42-11.629v0l.013-.018.09-.139.05-.077.084-.128c.964807-1.451293,2.096913-2.78411,3.373-3.971c2.10232-1.952788,4.341342-3.753028,6.7-5.387L93.719,1.153c3.535434-1.537378,7.550566-1.537378,11.086,0l93.676,40.73c5.077167,2.206378,8.361087,7.215142,8.36,12.751v56.98c-.006671,5.345507-3.078139,10.213589-7.9,12.521l-89.113,42.639c-3.296794,1.578019-6.905005,2.398067-10.56,2.4Zm0-155.185c-1.064731-.001065-2.118276.217133-3.095.641l-72.7,31.61c-1.8834.819048-3.101594,2.677215-3.101594,4.731s1.218194,3.911952,3.101594,4.731l72.7,31.612c1.975304.854052,4.215696.854052,6.191,0l72.7-31.612c1.884354-.816857,3.104712-2.673607,3.107099-4.727394s-1.213649-3.91337-3.096099-4.734606l-72.709-31.61c-.97833-.424348-2.033605-.642551-3.1-.641Z" transform="translate(487.741 355.059)" fill="url(#eRjslcmEZrC3-fill)"/><g id="eRjslcmEZrC4_to" transform="translate(504.367,367.785409)"><path d="M82.634,76.066c-1.115882.001218-2.219858-.229317-3.242-.677L3.248,42.28c-1.975079-.855649-3.253381-2.802543-3.253381-4.955s1.278302-4.099351,3.253381-4.955L76.963,0.312l.1.034c.263.086.537.166.815.237s.561.134.845.189.576.1.867.143.585.071.882.1.6.041.891.049c.165.005.334.008.5.008.125,0,.257,0,.391,0c.3-.007.594-.021.887-.043s.585-.051.873-.088.586-.091.858-.141.559-.111.827-.177.535-.139.793-.22.508-.17.753-.263c.116-.045.232-.092.343-.14l74.443,32.365c1.975079.855649,3.253381,2.802543,3.253381,4.955s-1.278302,4.099351-3.253381,4.955L85.877,75.389c-1.022451.447819-2.126781.678356-3.243.677Z" transform="translate(0,0)" fill="#ffb2ba"/></g><g id="eRjslcmEZrC5_to" transform="translate(387.431408,270.893836)"><g transform="translate(-387.431404,-200.106453)"><ellipse rx="50.183115" ry="11.587907" transform="translate(587.0015 334.320751)" fill="#201c30" stroke-width="0"/><path d="M82.634,77.486c-1.115882.001218-2.219858-.229317-3.242-.677L3.248,43.7c-1.975079-.855649-3.253381-2.802543-3.253381-4.955s1.278302-4.099351,3.253381-4.955L79.392,0.676c2.067572-.901998,4.417428-.901998,6.485,0L162.021,33.79c1.975079.855649,3.253381,2.802543,3.253381,4.955s-1.278302,4.099351-3.253381,4.955L85.877,76.81c-1.022533.447448-2.126854.677642-3.243.676ZM48.471,31.8c-.127,0-.255,0-.379,0-.285.006-.574.019-.859.039s-.57.047-.854.082-.568.076-.844.123-.555.1-.827.164-.539.13-.8.205-.52.156-.776.245-.5.184-.741.286-.479.213-.7.326-.444.238-.653.366-.406.262-.586.4-.349.275-.5.416c-.148149.135494-.28838.279399-.42.431-.121531.140438-.234087.288398-.337.443-.095119.144718-.179672.296111-.253.453-.068906.14784-.125757.301005-.17.458-.042178.150836-.070956.305102-.086.461-.014199.153005-.014199.306995,0,.46.013334.154113.040442.306723.081.456.042313.15384.097854.303732.166.448.071861.152136.155797.298271.251.437.101597.148374.213542.289392.335.422.124696.148294.258275.288886.4.421.152125.141718.311684.275241.478.4.17.129.359.257.559.381.183.124.381.243.589.356s.437.224.674.326.483.2.74.286.512.166.783.238.541.135.819.19.563.1.848.143.579.072.871.1.581.04.886.049c.169,0,.336.007.5.007.127,0,.257,0,.4,0c.3-.007.594-.021.887-.043s.585-.051.873-.088.586-.091.858-.141.559-.111.827-.177.535-.139.793-.22.508-.17.753-.263c.116-.045.232-.092.343-.14l74.443,32.365c1.975079.855649,3.253381,2.802543,3.253381,4.955s-1.278302,4.099351-3.253381,4.955L85.877,75.389c-1.022451.447819-2.126781.678356-3.243.677Z" transform="translate(504.367 295.578)" fill="#f7efff"/></g></g><g id="eRjslcmEZrC8_ts" transform="translate(460.728003,432.108415) scale(0,0)"><path d="M18.7,3.172c6.968601-4.348247,15.8286-4.251565,22.700656.247714s10.503904,12.581271,9.305344,20.707286c-.472,3.195-1.86,5.5-3.612,8.4v.005c-.473.784-.973,1.613-1.488,2.514-2.424,4.244-1.591,10.633.062,16.193.494,1.661,1.63,4.351,3.009,7.614v0c2.659,6.293,6.219,14.717,7.8,22c2.39,10.971,2.695,32.274,2.415,34.344c0,.021,0,.039-.006.057s0,.028,0,.043c0,5.813-12.876,10.524-28.757,10.525h-.565c-15.879,0-28.755-4.712-28.755-10.522.000127-.03346-.002213-.066884-.007-.1-.28-2.07.025-23.372,2.415-34.344c1.586-7.28,5.145-15.7,7.8-22v0C12.4,55.59,13.538,52.9,14.032,51.24c1.653-5.56,2.487-11.95.062-16.193-.515-.9-1.015-1.73-1.488-2.514v-.005c-1.751-2.9-3.139-5.2-3.612-8.4C7.766055,15.832894,11.579096,7.600246,18.7,3.172Z" transform="translate(-29.847003,-62.89941)" fill="url(#eRjslcmEZrC8-fill)" fill-rule="evenodd"/></g></g></g>""".trimIndent()

              document.getElementById("bgw_loading")?.appendChild(loadingSVG)
          } */
      } */

      val (lastDraggedOver, setLastDraggedOver) = useState<String?>(null)
      val draggedElementRef = useRef<org.w3c.dom.Element>(null)
      val (draggedComponentData, setDraggedComponentData) = useState<ComponentViewData?>(null)

      val pointerSensor =
          useSensor(
              PointerSensor,
              jsObject<PointerSensorOptions> { activationConstraint = jsObject { distance = 10 } })

      val allSensors = useSensors(pointerSensor)

      val measuringConfig =
          jsObject<MeasuringConfiguration> {
            draggable =
                jsObject<DraggableMeasuring> {
                  measure = { node -> getClientRect(node, jsObject { ignoreTransform = false }) }
                }
            droppable =
                jsObject<DroppableMeasuring> {
                  measure = { node -> getClientRect(node, jsObject { ignoreTransform = true }) }
                  strategy = MeasuringStrategy.BeforeDragging
                  frequency = MeasuringFrequency.Optimized
                }
            dragOverlay =
                jsObject<DragOverlayMeasuring> {
                  measure = { node -> getClientRect(node, jsObject { ignoreTransform = false }) }
                }
          }

      DndContext {
        sensors = allSensors
        measuring = measuringConfig

        onDragStart = { event ->
          val element = event.active?.id?.let { document.getElementById(it) }
          draggedElementRef.current = element

          // Find the component data for the dragged element
          event.active?.id?.let { componentId ->
            val gameScene = props.data.gameScene

            val foundComponent =
                when {
                  gameScene != undefined -> findComponentDataById(componentId, gameScene.components)
                  else -> null
                }

            setDraggedComponentData(foundComponent)
          }

          JCEFEventDispatcher.dispatchEvent(event.toDragStartedEventData())
        }

        onDragEnd = { event ->
          if (event.over != null) {
            JCEFEventDispatcher.dispatchEvent(event.toDragEventData())
          }
          JCEFEventDispatcher.dispatchEvent(event.toDragEndedEventData())
          if (lastDraggedOver != null) {
            JCEFEventDispatcher.dispatchEvent(
                DragGestureExitedEventData(lastDraggedOver).apply { this.id = event.active?.id })
          }
          setLastDraggedOver(null)
          setDraggedComponentData(null)
        }

        onDragMove = { event -> JCEFEventDispatcher.dispatchEvent(event.toDragMoveEventData()) }

        onDragOver = { event ->
          JCEFEventDispatcher.dispatchEvent(event.toDragEnteredEventData())
          if (lastDraggedOver != event.over?.id) {
            if (lastDraggedOver != null) {
              JCEFEventDispatcher.dispatchEvent(
                  DragGestureExitedEventData(lastDraggedOver).apply { this.id = event.active?.id })
            }
            setLastDraggedOver(event.over?.id)
          }
        }

        fun globalKeyDown(e: KeyboardEvent<*>) {
          JCEFEventDispatcher.dispatchGlobalEvent(e.toKeyEventData("global", KeyEventAction.TYPE))
        }

        fun globalKeyUp(e: KeyboardEvent<*>) {
          JCEFEventDispatcher.dispatchGlobalEvent(
              e.toKeyEventData("global", KeyEventAction.RELEASE))
        }

        useEffectWithCleanup {
          document.addEventListener("keydown", { globalKeyDown(it.unsafeCast<KeyboardEvent<*>>()) })

          document.addEventListener("keyup", { globalKeyUp(it.unsafeCast<KeyboardEvent<*>>()) })

          onCleanup {
            document.removeEventListener(
                "keydown", { globalKeyDown(it.unsafeCast<KeyboardEvent<*>>()) })

            document.removeEventListener(
                "keyup", { globalKeyUp(it.unsafeCast<KeyboardEvent<*>>()) })
          }
        }

        val menuScene = props.data.menuScene
        val (isMenuVisible, setMenuVisible) = useState(menuScene != undefined)
        var timeoutId: Int? = null

        // Effect to handle visibility changes when menuScene changes
        useEffectWithCleanup(menuScene) {
          if (menuScene != undefined) {
            setMenuVisible(true)
          } else {
            // Start a timer to remove the component after transition completes
            window.setTimeout({ setMenuVisible(false) }, props.data.fadeTime)
          }

          onCleanup { window.clearTimeout(timeoutId ?: 0) }
        }

        val gameScene = props.data.gameScene

        bgwScenes {
          css {
            width = 100.pct
            height = 100.pct
            display = Display.flex
            alignmentBuilder(props.data)
          }

          bgwVisuals {
            className = ClassName("visuals")
            +VisualBuilder.build(props.data.background)
          }

          bgwMenuScene {
            id = "menuScene"
            css {
              position = Position.absolute
              zIndex = zIndex(1000)
              transition = menuTransition(props.data.fadeTime)
              opacity = if (menuScene != undefined) number(1.0) else number(0.0)
              // Keep the element in the DOM but invisible until transition completes
              visibility = if (isMenuVisible) Visibility.visible else Visibility.hidden
            }

            // Always render component when isMenuVisible, even if menuScene is undefined
            if (isMenuVisible) {
              ariaExpanded = menuScene != undefined
              // Only build the scene content if menuScene is defined
              menuScene?.let { +SceneBuilder.build(it) }
            }
          }

          bgwBlur {
            css {
              position = Position.absolute
              zIndex = zIndex(999)
              backdropFilter =
                  backgroundBlur(
                      if (menuScene != undefined && props.data.blurRadius > 0.0) 1.0 else 0.0,
                      props.data.blurRadius)
              transition = menuTransition(props.data.fadeTime)
              // Same visibility logic as with the menu
              visibility = if (isMenuVisible) Visibility.visible else Visibility.hidden
            }

            if (isMenuVisible) {
              ariaExpanded = menuScene != undefined
              div {
                css {
                  width = props.data.width.bgw
                  height = props.data.height.bgw
                }
              }
            }
          }

          if (gameScene != undefined) {
            bgwLock {
              css {
                position = Position.absolute
                width = props.data.width.bgw
                height = props.data.height.bgw
                backgroundColor = rgb(0, 0, 0, 0.0)
                zIndex = zIndex(998)
                display = if (gameScene.locked) Display.block else None.none
              }
            }
          }

          bgwGameScene {
            css { position = Position.absolute }

            id = "boardGameScene"

            if (gameScene != null) {
              +SceneBuilder.build(gameScene)
            }
          }
        }

        // Loading Screen
        props.data.loadingScreen?.let { loadingScreenData ->
          if (loadingScreenData.visible) {
            div {
              id = "bgw_loading_screen"
              css {
                position = Position.fixed
                width = 100.pct
                height = 100.pct
                backgroundColor = "#201C30".unsafeCast<Color>()
                overflow = Overflow.hidden
                display = Display.flex
                flexDirection = FlexDirection.column
                justifyContent = JustifyContent.center
                alignItems = AlignItems.center
                zIndex = integer(5000000)
                animation = "fadeIn 0.3s ease-in".unsafeCast<Animation>()
              }

              // Logo - Animated SVG or custom image
              div {
                css {
                  display = Display.flex
                  justifyContent = JustifyContent.center
                  alignItems = AlignItems.center
                  width = 15.pct
                }
                id = "bgw_loading_logo"

                // If a custom logo path is provided (not empty), use img tag
                if (loadingScreenData.logoPath != null) {
                  img {
                    src = loadingScreenData.logoPath
                    css {
                      width = 15.pct
                    }
                  }
                }
              }

              // Use custom hook to inject animated SVG logo (when logoPath is empty)
              useAnimatedLogo(loadingScreenData, "bgw_loading_logo")

              // Progress Bar (if enabled)
              if (loadingScreenData.showProgressBar && loadingScreenData.totalImages > 0) {
                div {
                  css {
                    width = 98.vw
                    position = Position.absolute
                    left = 1.vw
                    bottom = 2.vw
                    display = Display.flex
                    justifyContent = JustifyContent.spaceBetween
                    alignItems = AlignItems.center
                    fontFamily = string("monospace")
                  }

                  div {
                    css {
                      width = 10.vw
                      height = 0.5.vh
                      backgroundColor = rgb(60, 60, 60, 1.0)
                      borderRadius = 4.px
                      overflow = Overflow.hidden
                    }

                    div {
                      id = "bgw_loading_progress_bar"
                      css {
                        val progress = if (loadingScreenData.totalImages > 0) {
                          (loadingScreenData.loadedImages.toDouble() / loadingScreenData.totalImages.toDouble()) * 100.0
                        } else 0.0

                        width = progress.pct
                        height = 100.pct
                        backgroundColor = "#9B43EE".unsafeCast<Color>()
                        transition = "width 0.3s ease-out".unsafeCast<Transition>()
                      }
                    }
                  }

                  // Progress Text
                  div {
                    id = "bgw_loading_progress_text"
                    css {
                      color = "#5C546E".unsafeCast<Color>()
                      fontSize = 0.5.vh
                      fontFamily = string("monospace")
                    }
                    +loadingScreenData.bgwVersion
                  }
                }
              }
            }

            useImagePreloading(loadingScreenData)
          }
        }

        //        DragOverlay {
        //          className = ClassName("bgw-drag-overlay")
        //          draggedComponentData?.let {
        //            +NodeBuilder.buildOverlay(draggedComponentData)
        //          }
        //        }
      }
    }

internal inline val bgwScenes: IntrinsicType<HTMLAttributes<Element>>
  get() = "bgw_scenes".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()
internal inline val bgwMenuScene: IntrinsicType<HTMLAttributes<Element>>
  get() = "bgw_menu_scene".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()

internal inline val bgwBlur: IntrinsicType<HTMLAttributes<Element>>
  get() = "bgw_blur".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()

internal inline val bgwLock: IntrinsicType<HTMLAttributes<Element>>
  get() = "bgw_lock".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()

internal inline val bgwGameScene: IntrinsicType<HTMLAttributes<Element>>
  get() = "bgw_game_scene".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()

internal inline val bgwVisuals: IntrinsicType<HTMLAttributes<Element>>
  get() = "bgw_visuals".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()

internal inline val bgwContents: IntrinsicType<HTMLAttributes<Element>>
  get() = "bgw_contents".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()

internal inline val bgwText: IntrinsicType<HTMLAttributes<Element>>
  get() = "bgw_text".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()

internal fun polygonPath(
    value: String,
): ClipPath = "polygon($value)".unsafeCast<ClipPath>()

internal fun zIndex(value: Int): ZIndex = value.unsafeCast<ZIndex>()

internal fun fit(): LengthType.FitContent = "fit-content".unsafeCast<LengthType.FitContent>()

internal fun minContent(): GridTemplateTracks = "min-content".unsafeCast<GridTemplateTracks>()

internal fun bgwContainer(): ContainerName = "bgwContainer".unsafeCast<ContainerName>()

internal fun menuTransition(fadeTime: Int): Transition =
    "${fadeTime}ms opacity, ${fadeTime}ms backdrop-filter".unsafeCast<Transition>()

internal fun backgroundBlur(opacity: Double, blurRadius: Double): BackdropFilter =
    "blur(calc(var(--bgwUnit) * ${blurRadius})) opacity($opacity)".unsafeCast<BackdropFilter>()

internal fun transition(duration: Int, property: String): Transition =
    "${duration}ms $property".unsafeCast<Transition>()

internal fun baseSelect(): Appearance = "base-select".unsafeCast<Appearance>()

internal fun transitionAll(duration: Int): Transition = "${duration}ms".unsafeCast<Transition>()

internal inline fun <T> jsObject(builder: T.() -> Unit): T {
  val obj = js("{}")
  builder(obj.unsafeCast<T>())
  return obj.unsafeCast<T>()
}

internal fun bgwUnit(value: Number): Length {
  return "calc(var(--bgwUnit) * ${value})".unsafeCast<Length>()
}

internal inline val Number.bgw: Length
  get() = ("calc(var(--bgwUnit) * ${this})").unsafeCast<Length>()

internal fun defaultTransform(): Transform {
  return "translate(var(--tx), var(--ty)) rotate(var(--rot))".unsafeCast<Transform>()
}

// Helper function to recursively find component data by ID
internal fun findComponentDataById(
    id: String,
    components: List<ComponentViewData>,
    depth: Int = 0
): ComponentViewData? {
  for (component in components) {
    if (component.id == id) {
      return component
    }

    // Check for PaneData or GameComponentContainerData (Area, CardStack, etc.)
    if (component is PaneData) {
      val found = findComponentDataById(id, component.components, depth + 1)
      if (found != null) return found
    }

    if (component is GameComponentContainerData) {
      val found = findComponentDataById(id, component.components, depth + 1)
      if (found != null) return found
    }

    // Check for GridPaneData
    if (component is GridPaneData) {
      for (gridElement in component.grid) {
        gridElement.component?.let { gridComponent ->
          val found = findComponentDataById(id, listOf(gridComponent), depth + 1)
          if (found != null) return found
        }
      }
    }

    // Check for CameraPaneData
    if (component is CameraPaneData) {
      component.target?.let { target ->
        val found = findComponentDataById(id, listOf(target), depth + 1)
        if (found != null) return found
      }
    }
  }
  return null
}
