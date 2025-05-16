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

package tools.aqua.bgw.elements.visual

import ImageVisualData
import emotion.react.css
import kotlinx.browser.document
import org.w3c.dom.*
import react.FC
import react.IntrinsicType
import react.Props
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML.canvas
import react.dom.html.ReactHTML.img
import react.useEffect
import tools.aqua.bgw.elements.filterBuilder
import tools.aqua.bgw.elements.flipBuilder
import tools.aqua.bgw.elements.styleBuilder
import web.cssom.*
import web.cssom.atrule.width
import web.dom.Element

internal external interface ImageVisualProps : Props {
  var data: ImageVisualData
  var parentWidth: Double
  var parentHeight: Double
}

internal val ImageVisual =
    FC<ImageVisualProps> { props ->
      if (props.data.width != -1 && props.data.height != -1) {
        bgwImageVisual {
          css {
            styleBuilder(props.data.style)
            flipBuilder(props.data.flipped)
            filterBuilder(props.data.filters)
            transform = rotatez(props.data.rotation.deg)

            width = 100.pct
            height = 100.pct
            overflow = Overflow.hidden
          }

          canvas {
            id = props.data.id
            width = props.data.width.toDouble()
            height = props.data.height.toDouble()

            css {
              position = Position.absolute
              width = 100.pct
              height = 100.pct

              opacity = number(props.data.transparency)
              // TODO...
            }

            useEffect {
              val img = document.createElement("img") as HTMLImageElement
              img.src = props.data.path

              img.addEventListener(
                  "load",
                  {
                    val canvas = document.getElementById(props.data.id) as HTMLCanvasElement
                    val ctx = canvas.getContext("2d") as CanvasRenderingContext2D

                    ctx.clearRect(
                        0.0, 0.0, props.data.width.toDouble(), props.data.height.toDouble())
                    ctx.drawImage(
                        img,
                        props.data.offsetX.toDouble(),
                        props.data.offsetY.toDouble(),
                        props.data.width.toDouble(),
                        props.data.height.toDouble(),
                        0.0,
                        0.0,
                        props.data.width.toDouble(),
                        props.data.height.toDouble())
                  })
            }
          }
        }
      } else {
        bgwImageVisual {
          id = props.data.id

          css {
            styleBuilder(props.data.style)
            flipBuilder(props.data.flipped)
            filterBuilder(props.data.filters)
            transform = rotatez(props.data.rotation.deg)

            backgroundImage = url(props.data.path)

            backgroundSize = BackgroundSize.cover
            backgroundRepeat = BackgroundRepeat.noRepeat
            backgroundPosition = Background.center as BackgroundPosition

            left = props.data.offsetX.em
            top = props.data.offsetY.em

            opacity = number(props.data.transparency)
            // TODO...
          }
        }
      }
    }

internal inline val bgwImageVisual: IntrinsicType<HTMLAttributes<Element>>
  get() = "bgw_image_visual".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()

internal inline val bgwImageVisualOffset: IntrinsicType<HTMLAttributes<Element>>
  get() = "bgw_image_visual_offset".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()
