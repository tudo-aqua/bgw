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

import DialogData
import emotion.react.css
import react.*
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.p
import web.cssom.*

internal external interface DialogProps : Props {
  var data: List<DialogData>
}

internal val Dialog =
    FC<DialogProps> { props ->
      val size = props.data.size
      val (num, setNum) = useState(0)
      val (current, setCurrent) = useState(props.data[0])

      div {
        css {
          width = 100.pct
          height = 100.pct
          position = Position.fixed
          top = 0.px
          left = 0.px
          inset = 0.px
          zIndex = zIndex(2147483647)
          display = Display.flex
          justifyContent = JustifyContent.center
          alignItems = AlignItems.center
          backgroundColor = Color("#00000050")
        }

        div {
          css {
            width = 500.em
            height = 500.em
            backgroundColor = Color("#FFFFFF")
            color = Color("#000000")
          }

          h1 {
            css { fontSize = 20.em }
            +current.title
          }

          p {
            css { fontSize = 16.em }
            +current.header
            +current.message
          }

          div {
            css {
              width = 100.pct
              display = Display.flex
              justifyContent = JustifyContent.spaceBetween
              alignItems = AlignItems.center
            }

            button {
              onClick = {
                if (num > 0) {
                  setNum(num - 1)
                  setCurrent(props.data[num - 1])
                }
              }
              +"Previous"
            }

            p {
              css {
                fontSize = 16.em
                textAlign = TextAlign.center
              }
              +"${num + 1} / $size"
            }

            button {
              onClick = {
                if (num < size - 1) {
                  setNum(num + 1)
                  setCurrent(props.data[num + 1])
                }
              }
              +"Next"
            }
          }
        }
      }
    }
