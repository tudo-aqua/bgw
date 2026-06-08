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

package tools.aqua.bgw.elements.visual

import ImageVisualData
import emotion.react.css
import kotlinx.browser.document
import org.w3c.dom.HTMLImageElement
import org.w3c.dom.events.Event
import react.FC
import react.IntrinsicType
import react.Props
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML.canvas
import react.useEffectWithCleanup
import react.useRef
import tools.aqua.bgw.elements.bgw
import tools.aqua.bgw.elements.filterBuilder
import tools.aqua.bgw.elements.flipBuilder
import tools.aqua.bgw.elements.styleBuilder
import web.canvas.CanvasImageSource
import web.canvas.CanvasRenderingContext2D
import web.cssom.*
import web.dom.Element
import web.html.HTMLCanvasElement

internal external interface ImageVisualProps : Props {
  var data: ImageVisualData
  var parentWidth: Double
  var parentHeight: Double
}

internal val ImageVisual =
    FC<ImageVisualProps> { props ->
      val canvasRef = useRef<HTMLCanvasElement>(null)
      val imgRef = useRef<HTMLImageElement>(null)

      useEffectWithCleanup(
          props.data.path,
          props.data.offsetX,
          props.data.offsetY,
          props.data.width,
          props.data.height,
      ) {
        if (props.data.width == -1 || props.data.height == -1) {
          return@useEffectWithCleanup
        }

        val img =
            imgRef.current
                ?: (document.createElement("img") as HTMLImageElement).also { imgRef.current = it }

        fun draw() {
          val canvas = canvasRef.current ?: return
          val ctx =
              canvas.getContext(CanvasRenderingContext2D.ID) as? CanvasRenderingContext2D ?: return

          val width = props.data.width.toDouble()
          val height = props.data.height.toDouble()

          ctx.clearRect(0.0, 0.0, width, height)
          ctx.drawImage(
              img.unsafeCast<CanvasImageSource>(),
              props.data.offsetX.toDouble(),
              props.data.offsetY.toDouble(),
              width,
              height,
              0.0,
              0.0,
              width,
              height)
        }

        val handler: (Event) -> Unit = { draw() }

        img.addEventListener("load", handler)

        if (img.getAttribute("src") != props.data.path) {
          img.setAttribute("src", props.data.path)
        } else if (img.complete) {
          draw()
        }

        onCleanup { img.removeEventListener("load", handler) }
      }

      if (props.data.width != -1 && props.data.height != -1) {
        bgwImageVisual {
          css {
            styleBuilder(props.data.style)
            flipBuilder(props.data.flipped, props.data.rotation)
            filterBuilder(props.data.filters)

            width = 100.pct
            height = 100.pct
            overflow = Overflow.hidden
          }

          canvas {
            ref = canvasRef
            id = props.data.id
            width = props.data.width.toDouble()
            height = props.data.height.toDouble()

            css {
              position = Position.absolute
              width = 100.pct
              height = 100.pct
              opacity = number(props.data.transparency)
            }
          }
        }
      } else {
        bgwImageVisual {
          id = props.data.id

          css {
            styleBuilder(props.data.style)
            flipBuilder(props.data.flipped, props.data.rotation)
            filterBuilder(props.data.filters)

            backgroundImage = url(props.data.path)

            backgroundSize = BackgroundSize.cover
            backgroundRepeat = BackgroundRepeat.noRepeat
            backgroundPosition = Background.center as BackgroundPosition

            left = props.data.offsetX.bgw
            top = props.data.offsetY.bgw

            opacity = number(props.data.transparency)
          }
        }
      }
    }

internal inline val bgwImageVisual: IntrinsicType<HTMLAttributes<Element>>
  get() = "bgw_image_visual".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()

internal inline val bgwImageVisualOffset: IntrinsicType<HTMLAttributes<Element>>
  get() = "bgw_image_visual_offset".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()
