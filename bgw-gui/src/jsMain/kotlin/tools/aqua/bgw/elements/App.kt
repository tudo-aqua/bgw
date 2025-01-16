/*
 * Copyright 2025 The BoardGameWork Authors
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
import data.event.DragGestureExitedEventData
import data.event.KeyEventAction
import emotion.react.Global
import emotion.react.css
import emotion.react.styles
import kotlinx.browser.document
import react.*
import react.dom.aria.ariaExpanded
import react.dom.events.KeyboardEvent
import react.dom.html.HTMLAttributes
import tools.aqua.bgw.*
import tools.aqua.bgw.builder.ReactConverters.toDragEndedEventData
import tools.aqua.bgw.builder.ReactConverters.toDragEnteredEventData
import tools.aqua.bgw.builder.ReactConverters.toDragEventData
import tools.aqua.bgw.builder.ReactConverters.toDragMoveEventData
import tools.aqua.bgw.builder.ReactConverters.toDragStartedEventData
import tools.aqua.bgw.builder.ReactConverters.toKeyEventData
import tools.aqua.bgw.builder.SceneBuilder
import tools.aqua.bgw.core.DEFAULT_BLUR_RADIUS
import tools.aqua.bgw.core.DEFAULT_MENU_SCENE_OPACITY
import tools.aqua.bgw.event.JCEFEventDispatcher
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
            ".bgw-root" {
              fontSize = (100.0 / props.data.height).cqh
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

            "input[type='color']" { fontSize = (100.0 / props.data.height).cqh }

            "bgw_togglebutton > input[type='checkbox'], bgw_checkbox > input[type='checkbox'], bgw_radiobutton > input[type='radio']" {
              fontSize = (100.0 / props.data.height).cqh
            }
          }

          // RED
          "@container bgwContainer (max-aspect-ratio: ${props.data.width}/${props.data.height}) and (orientation: landscape)" {
            ".bgw-root" {
              fontSize = (100.0 / props.data.width).cqw
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

            "input[type='color']" { fontSize = (100.0 / props.data.width).cqw }

            "bgw_togglebutton > input[type='checkbox'], bgw_checkbox > input[type='checkbox'], bgw_radiobutton > input[type='radio']" {
              fontSize = (100.0 / props.data.width).cqw
            }
          }

          // GREEN
          "@container bgwContainer (min-aspect-ratio: ${props.data.width}/${props.data.height}) and (orientation: portrait)" {
            ".bgw-root" {
              fontSize = (100.0 / props.data.height).cqh
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

            "input[type='color']" { fontSize = (100.0 / props.data.height).cqh }

            "bgw_togglebutton > input[type='checkbox'], bgw_checkbox > input[type='checkbox'], bgw_radiobutton > input[type='radio']" {
              fontSize = (100.0 / props.data.height).cqh
            }
          }

          // YELLOW
          "@container bgwContainer (max-aspect-ratio: ${props.data.width}/${props.data.height}) and (orientation: portrait)" {
            ".bgw-root" {
              fontSize = (100.0 / props.data.width).cqw
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
              backgroundColor = rgb(0, 0, 0, 1.0)
              overflow = Overflow.hidden
              display = Display.block
            }

            "input[type='color']" { fontSize = (100.0 / props.data.width).cqw }

            "bgw_togglebutton > input[type='checkbox'], bgw_checkbox > input[type='checkbox'], bgw_radiobutton > input[type='radio']" {
              fontSize = (100.0 / props.data.width).cqw
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
            backdropFilter = blur(DEFAULT_BLUR_RADIUS.em)
          } */

          "bgw_hexagon_view[aria-details='hex-pointy_top']" {
            clipPath = polygonPath("0% 25%, 0% 75%, 50% 100%, 100% 75%, 100% 25%, 50% 0%")
          }

          "bgw_hexagon_view[aria-details='hex-flat_top']" {
            clipPath = polygonPath("25% 0%, 75% 0%, 100% 50%, 75% 100%, 25% 100%, 0% 50%")
          }

          "bgw_menu_scene[aria-expanded='true'] > bgw_scene" {
            opacity = number(1.0)
            backdropFilter = blur(DEFAULT_BLUR_RADIUS.em)
          }

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

          "bgw_linear_layout > bgw_contents > div > *" {
            position = important(Position.relative)
            left = important(Globals.unset)
            top = important(Globals.unset)
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
            borderRadius = 3.em
            border = None.none
          }

          "bgw_togglebutton" {
            position = Position.relative
            display = Display.inlineFlex
            width = 30.em
            height = 18.em
          }

          "bgw_togglebutton > input[type='checkbox']" {
            opacity = number(0.0)
            position = Position.relative
            width = 36.em
            minWidth = 36.em
            height = 100.pct
          }

          ".toggle" {
            position = Position.absolute
            left = 4.em
            width = 30.em
            height = 18.em
            backgroundColor = rgb(145, 145, 145)
            transition = transition(300, "background-color")
            borderRadius = 3.em
          }

          ".toggle::before" {
            content = Content("")
            position = Position.absolute
            width = 12.em
            height = 12.em
            left = 3.em
            top = 3.em
            backgroundColor = rgb(255, 255, 255)
            transition = transition(300, "transform")
            borderRadius = 3.em
          }

          "bgw_togglebutton > input[type='checkbox']:checked + .toggle" {
            backgroundColor = rgb(0, 117, 255)
          }

          "bgw_togglebutton > input[type='checkbox']:checked + .toggle::before" {
            transform = translatex(12.em)
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
            // position = important(Position.fixed)
            opacity = important(number(1.0))
            zIndex = important(integer(1000000))
          }

          ".bgw-root *:has(*[aria-roledescription='draggable'][aria-pressed='true'])" {
            zIndex = important(integer(1000000))
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

              loadingSVG.innerHTML = """<style>#eRjslcmEZrC2_to {animation: eRjslcmEZrC2_to__to 1500ms linear 1 normal forwards}@keyframes eRjslcmEZrC2_to__to { 0% {transform: translate(-431.597px,-66.928515px);animation-timing-function: cubic-bezier(0.68,-0.005,0.295,1)} 40% {transform: translate(-431.597px,-295.583971px)} 100% {transform: translate(-431.597px,-295.583971px)}} #eRjslcmEZrC2 {animation: eRjslcmEZrC2_c_o 1500ms linear 1 normal forwards}@keyframes eRjslcmEZrC2_c_o { 0% {opacity: 0} 20% {opacity: 0;animation-timing-function: cubic-bezier(0.42,0,0.58,1)} 46.666667% {opacity: 1} 100% {opacity: 1}} #eRjslcmEZrC4_to {animation: eRjslcmEZrC4_to__to 1500ms linear 1 normal forwards}@keyframes eRjslcmEZrC4_to__to { 0% {transform: translate(504.367px,367.785409px)} 33.333333% {transform: translate(504.367px,367.785409px);animation-timing-function: cubic-bezier(0,0,0.09,1.005)} 73.333333% {transform: translate(504.367px,335.551px)} 100% {transform: translate(504.367px,335.551px)}} #eRjslcmEZrC5_to {animation: eRjslcmEZrC5_to__to 1500ms linear 1 normal forwards}@keyframes eRjslcmEZrC5_to__to { 0% {transform: translate(387.431408px,270.893836px)} 26.666667% {transform: translate(387.431408px,270.893836px);animation-timing-function: cubic-bezier(0,0,0.09,1.005)} 86.666667% {transform: translate(387.431404px,200.106453px)} 100% {transform: translate(387.431404px,200.106453px)}} #eRjslcmEZrC8_ts {animation: eRjslcmEZrC8_ts__ts 1500ms linear 1 normal forwards}@keyframes eRjslcmEZrC8_ts__ts { 0% {transform: translate(460.728003px,432.108415px) scale(0,0)} 13.333333% {transform: translate(460.728003px,432.108415px) scale(0,0);animation-timing-function: cubic-bezier(0,0,0.09,1.005)} 73.333333% {transform: translate(460.728003px,432.108415px) scale(1,1)} 100% {transform: translate(460.728003px,432.108415px) scale(1,1)}}</style><defs><linearGradient id="eRjslcmEZrC3-fill" x1="0.53" y1="0.19" x2="0.837" y2="0.938" spreadMethod="pad" gradientUnits="objectBoundingBox" gradientTransform="translate(0 0)"><stop id="eRjslcmEZrC3-fill-0" offset="0%" stop-color="#c9326f"/><stop id="eRjslcmEZrC3-fill-1" offset="100%" stop-color="#ff7e7e"/></linearGradient><linearGradient id="eRjslcmEZrC8-fill" x1="-0.047" y1="0.5" x2="0.919" y2="0.585" spreadMethod="pad" gradientUnits="objectBoundingBox" gradientTransform="translate(0 0)"><stop id="eRjslcmEZrC8-fill-0" offset="0%" stop-color="#c9326f"/><stop id="eRjslcmEZrC8-fill-1" offset="100%" stop-color="#ff7e7e"/></linearGradient></defs><g id="eRjslcmEZrC2_to" transform="translate(-431.597,-66.928515)"><g id="eRjslcmEZrC2" transform="translate(0,0)" opacity="0"><path d="M99.263,169.169c-3.654373-.001758-7.261972-.821822-10.558-2.4l-70.633-33.78v0h-.006l-.015-.006L18,132.96h-.007l-.109-.052c-.523-.259-1.742-.982-1.755-2.1.253-4.828.2-15.658-3.019-33.937C11.458,88.2,7.4,78.851,4.428,72.025L4.419,72v0-.009c-1.183465-2.550343-2.2378-5.158644-3.159-7.815-.711379-2.236509-1.129537-4.555873-1.244-6.9-.206903-4.151434.998609-8.250527,3.42-11.629v0l.013-.018.09-.139.05-.077.084-.128c.964807-1.451293,2.096913-2.78411,3.373-3.971c2.10232-1.952788,4.341342-3.753028,6.7-5.387L93.719,1.153c3.535434-1.537378,7.550566-1.537378,11.086,0l93.676,40.73c5.077167,2.206378,8.361087,7.215142,8.36,12.751v56.98c-.006671,5.345507-3.078139,10.213589-7.9,12.521l-89.113,42.639c-3.296794,1.578019-6.905005,2.398067-10.56,2.4Zm0-155.185c-1.064731-.001065-2.118276.217133-3.095.641l-72.7,31.61c-1.8834.819048-3.101594,2.677215-3.101594,4.731s1.218194,3.911952,3.101594,4.731l72.7,31.612c1.975304.854052,4.215696.854052,6.191,0l72.7-31.612c1.884354-.816857,3.104712-2.673607,3.107099-4.727394s-1.213649-3.91337-3.096099-4.734606l-72.709-31.61c-.97833-.424348-2.033605-.642551-3.1-.641Z" transform="translate(487.741 355.059)" fill="url(#eRjslcmEZrC3-fill)"/><g id="eRjslcmEZrC4_to" transform="translate(504.367,367.785409)"><path d="M82.634,76.066c-1.115882.001218-2.219858-.229317-3.242-.677L3.248,42.28c-1.975079-.855649-3.253381-2.802543-3.253381-4.955s1.278302-4.099351,3.253381-4.955L76.963,0.312l.1.034c.263.086.537.166.815.237s.561.134.845.189.576.1.867.143.585.071.882.1.6.041.891.049c.165.005.334.008.5.008.125,0,.257,0,.391,0c.3-.007.594-.021.887-.043s.585-.051.873-.088.586-.091.858-.141.559-.111.827-.177.535-.139.793-.22.508-.17.753-.263c.116-.045.232-.092.343-.14l74.443,32.365c1.975079.855649,3.253381,2.802543,3.253381,4.955s-1.278302,4.099351-3.253381,4.955L85.877,75.389c-1.022451.447819-2.126781.678356-3.243.677Z" transform="translate(0,0)" fill="#ffb2ba"/></g><g id="eRjslcmEZrC5_to" transform="translate(387.431408,270.893836)"><g transform="translate(-387.431404,-200.106453)"><ellipse rx="50.183115" ry="11.587907" transform="translate(587.0015 334.320751)" fill="#201c30" stroke-width="0"/><path d="M82.634,77.486c-1.115882.001218-2.219858-.229317-3.242-.677L3.248,43.7c-1.975079-.855649-3.253381-2.802543-3.253381-4.955s1.278302-4.099351,3.253381-4.955L79.392,0.676c2.067572-.901998,4.417428-.901998,6.485,0L162.021,33.79c1.975079.855649,3.253381,2.802543,3.253381,4.955s-1.278302,4.099351-3.253381,4.955L85.877,76.81c-1.022533.447448-2.126854.677642-3.243.676ZM48.471,31.8c-.127,0-.255,0-.379,0-.285.006-.574.019-.859.039s-.57.047-.854.082-.568.076-.844.123-.555.1-.827.164-.539.13-.8.205-.52.156-.776.245-.5.184-.741.286-.479.213-.7.326-.444.238-.653.366-.406.262-.586.4-.349.275-.5.416c-.148149.135494-.28838.279399-.42.431-.121531.140438-.234087.288398-.337.443-.095119.144718-.179672.296111-.253.453-.068906.14784-.125757.301005-.17.458-.042178.150836-.070956.305102-.086.461-.014199.153005-.014199.306995,0,.46.013334.154113.040442.306723.081.456.042313.15384.097854.303732.166.448.071861.152136.155797.298271.251.437.101597.148374.213542.289392.335.422.131595.141889.271865.275478.42.4.159519.136116.32644.263311.5.381.183.124.381.243.589.356s.437.224.674.326.483.2.74.286.512.166.783.238.541.135.819.19.563.1.848.143.579.072.871.1.581.04.886.049c.169,0,.336.007.5.007.127,0,.257,0,.4,0c.3-.007.6-.021.9-.043s.6-.052.889-.088.587-.082.876-.133.578-.112.855-.177.556-.139.827-.22.541-.171.794-.264.513-.2.752-.3.479-.225.7-.346.446-.253.649-.386.394-.271.574-.415c.169254-.135249.331152-.279455.485-.432.142536-.139796.276133-.288423.4-.445.113104-.143983.216021-.295685.308-.454.085144-.1474.158699-.301197.22-.46.057924-.149925.103085-.304475.135-.462.030671-.151823.047408-.306131.05-.461.002847-.153058-.008535-.306049-.034-.457-.025262-.152813-.064082-.303073-.116-.449-.054545-.151236-.121438-.29773-.2-.438-.082545-.147624-.175795-.289002-.279-.423-.110423-.143548-.230359-.279521-.359-.407-.138009-.137084-.283926-.265971-.437-.386-.165303-.129476-.3372-.250304-.515-.362-.183-.116-.383-.229-.592-.336s-.435-.211-.669-.306-.482-.187-.728-.267-.5-.155-.764-.221-.521-.124-.8-.176-.547-.1-.819-.132-.554-.066-.839-.089-.569-.037-.851-.045c-.169-.011-.33-.017-.49-.017Zm33.316-.33c-.124,0-.251,0-.378,0-.286.006-.572.019-.851.039s-.559.047-.84.081-.554.075-.824.123-.536.1-.8.163-.527.131-.773.2-.505.159-.738.245-.477.185-.7.285-.446.212-.651.325-.409.238-.6.365c-.182423.12419-.358323.2577-.527.4-.154955.129273-.302207.26751-.441.414-.128072.134927-.247003.278245-.356.429-.101091.140461-.191671.288196-.271.442-.074483.145003-.137023.295836-.187.451-.046468.14916-.079926.302065-.1.457-.020073.152123-.026099.305772-.018.459.007537.154609.028956.308227.064.459.036915.155429.086412.307597.148.455.066059.154586.14361.304004.232.447.094474.152409.199392.298091.314.436.124696.148294.258275.288886.4.421.152125.141718.311684.275241.478.4.17.129.359.257.559.381s.416.243.64.354.472.224.719.324.515.2.78.285.537.166.815.237.561.135.845.189.569.1.867.143.586.071.882.1.6.041.891.049c.164,0,.333.008.5.008.126,0,.258,0,.391,0c.3-.007.593-.021.887-.043s.585-.051.873-.088.578-.082.854-.133.555-.11.827-.176.537-.14.793-.22.512-.169.752-.262.481-.2.706-.3.447-.225.651-.345.405-.251.591-.385c.178167-.128457.349095-.266668.512-.414.148832-.134787.289105-.278734.42-.431.120219-.139772.230489-.287802.33-.443.091685-.144514.17193-.295975.24-.453.064206-.147933.1154-.301179.153-.458.07305-.30133.089999-.613533.05-.921-.020162-.154264-.05362-.306498-.1-.455-.048403-.153881-.108926-.303683-.181-.448-.076843-.151385-.164399-.297089-.262-.436-.104179-.147803-.217746-.28876-.34-.422-.131232-.142775-.270455-.277992-.417-.405-.157738-.136562-.322281-.26506-.493-.385-.176-.124-.367-.245-.567-.361s-.419-.23-.64-.335-.466-.21-.713-.305-.5-.185-.765-.266-.528-.155-.795-.221-.547-.126-.819-.176-.553-.095-.836-.132-.568-.066-.849-.088-.573-.037-.856-.045c-.156-.008-.317-.011-.475-.011Zm33.512-.206c-.124,0-.249,0-.373,0-.282.006-.566.019-.844.039s-.559.048-.826.081-.545.075-.8.122-.517.1-.775.163-.5.13-.741.2-.476.157-.7.244-.449.185-.654.284-.415.212-.6.325c-.187244.111749-.368494.233249-.543.364-.163355.12194-.319298.253507-.467.394-.135389.129073-.262005.267047-.379.413-.108241.135263-.205865.27869-.292.429-.08056.141001-.149135.28852-.205.441-.053427.146094-.093589.296702-.12.45-.025688.150261-.037405.302578-.035.455.002429.154166.018829.307795.049.459.031613.155751.076099.308609.133.457.060028.156254.131904.307695.215.453.089542.15544.189784.304466.3.446.117065.15192.242948.296836.377.434.145374.147435.298243.287286.458.419.163.135.343.27.536.4s.4.259.614.379.453.243.69.354.5.223.765.324.542.2.82.284.564.166.849.237.576.134.871.189.587.1.886.142.593.071.895.1.6.041.9.049c.16,0,.322.007.5.007.14,0,.269,0,.394,0c.3-.007.591-.022.878-.043s.583-.052.859-.088.56-.081.833-.132.537-.11.8-.176.521-.141.759-.219.482-.167.712-.261.455-.2.659-.3.413-.224.6-.344c.184573-.118131.362508-.246324.533-.384.158628-.127959.308933-.265906.45-.413.128797-.134281.24777-.277651.356-.429.099468-.139983.187423-.287801.263-.442.070986-.14488.128883-.295813.173-.451.084907-.29776.113396-.60877.084-.917-.015059-.155163-.043498-.308734-.085-.459-.043583-.155508-.099425-.307317-.167-.454-.071501-.154413-.154048-.303467-.247-.446-.098951-.152097-.207504-.297726-.325-.436-.12541-.147654-.258951-.288205-.4-.421-.147-.139-.307-.275-.476-.4s-.357-.262-.548-.384-.4-.244-.619-.36-.452-.23-.688-.333-.49-.207-.756-.3-.53-.184-.8-.265-.546-.154-.826-.22-.559-.124-.842-.176-.569-.095-.855-.131-.567-.065-.861-.088-.58-.037-.86-.045c-.18-.014-.342-.017-.501-.017Z" transform="translate(504.367 295.578)" fill="#f7efff"/></g></g><g id="eRjslcmEZrC8_ts" transform="translate(460.728003,432.108415) scale(0,0)"><path d="M18.7,3.172c6.968601-4.348247,15.8286-4.251565,22.700656.247714s10.503904,12.581271,9.305344,20.707286c-.472,3.195-1.86,5.5-3.612,8.4v.005c-.473.784-.973,1.613-1.488,2.514-2.424,4.244-1.591,10.633.062,16.193.494,1.661,1.63,4.351,3.009,7.614v0c2.659,6.293,6.219,14.717,7.8,22c2.39,10.971,2.695,32.274,2.415,34.344c0,.021,0,.039-.006.057s0,.028,0,.043c0,5.813-12.876,10.524-28.757,10.525h-.565c-15.879,0-28.755-4.712-28.755-10.522.000127-.03346-.002213-.066884-.007-.1-.28-2.07.025-23.372,2.415-34.344c1.586-7.28,5.145-15.7,7.8-22v0C12.4,55.59,13.538,52.9,14.032,51.24c1.653-5.56,2.487-11.95.062-16.193-.515-.9-1.015-1.73-1.488-2.514v-.005c-1.751-2.9-3.139-5.2-3.612-8.4C7.766055,15.832894,11.579096,7.600246,18.7,3.172Z" transform="translate(-29.847003,-62.89941)" fill="url(#eRjslcmEZrC8-fill)" fill-rule="evenodd"/></g></g></g>""".trimIndent()

              document.getElementById("bgw_loading")?.appendChild(loadingSVG)
          } */
      } */

      val (lastDraggedOver, setLastDraggedOver) = useState<String?>(null)
      val draggedElementRef = useRef<org.w3c.dom.Element>(null)

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
        // measuring = measuringConfig

        onDragStart = { event ->
          val element = event.active?.id?.let { document.getElementById(it) }
          draggedElementRef.current = element
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
          JCEFEventDispatcher.dispatchGlobalEvent(e.toKeyEventData("global", KeyEventAction.PRESS))
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

        bgwScenes {
          css {
            width = 100.pct
            height = 100.pct
            display = Display.flex
            justifyContent = JustifyContent.center
            alignItems = AlignItems.center
          }

          val menuScene = props.data.menuScene
          bgwMenuScene {
            id = "menuScene"
            css {
              position = Position.absolute
              zIndex = zIndex(1000)
            }

            if (menuScene != null) {
              ariaExpanded = true

              +SceneBuilder.build(menuScene)
            }
          }
          if(menuScene != null) {
            bgwBlur {
              css {
                position = Position.absolute
                width = props.data.width.em
                height = props.data.height.em
                backgroundColor = rgb(0, 0, 0, 0.0)
                zIndex = zIndex(999)
                backdropFilter = blur(DEFAULT_BLUR_RADIUS.em)
              }
            }
          }
          val gameScene = props.data.gameScene
          bgwGameScene {
            css { position = Position.absolute }

            id = "boardGameScene"

            if (gameScene != null) {
              +SceneBuilder.build(gameScene)
            }
          }
        }

        DragOverlay { className = ClassName("bgw_drag_overlay") }
      }
    }

internal inline val bgwScenes: IntrinsicType<HTMLAttributes<Element>>
  get() = "bgw_scenes".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()
internal inline val bgwMenuScene: IntrinsicType<HTMLAttributes<Element>>
  get() = "bgw_menu_scene".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()

internal inline val bgwBlur : IntrinsicType<HTMLAttributes<Element>>
  get() = "bgw_blur".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()

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

internal fun menuTransition(): Transition =
    ".3s opacity, .3s backdrop-filter".unsafeCast<Transition>()

internal fun transition(duration: Int, property: String): Transition =
    "${duration}ms $property".unsafeCast<Transition>()

internal fun transitionAll(duration: Int): Transition = "${duration}ms".unsafeCast<Transition>()

internal inline fun <T> jsObject(builder: T.() -> Unit): T {
  val obj = js("{}")
  builder(obj.unsafeCast<T>())
  return obj.unsafeCast<T>()
}
