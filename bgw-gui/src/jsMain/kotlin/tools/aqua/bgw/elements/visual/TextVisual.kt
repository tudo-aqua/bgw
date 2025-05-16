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

import TextVisualData
import emotion.react.css
import react.FC
import react.IntrinsicType
import react.Props
import react.dom.html.HTMLAttributes
import tools.aqua.bgw.elements.filterBuilder
import tools.aqua.bgw.elements.flipBuilder
import tools.aqua.bgw.elements.styleBuilder
import web.cssom.*
import web.dom.Element

internal external interface TextVisualProps : Props {
  var data: TextVisualData
}

internal val TextVisual =
    FC<TextVisualProps> { props ->
      bgwTextVisual {
        id = props.data.id
        css {
          styleBuilder(props.data.style)
          flipBuilder(props.data.flipped)
          filterBuilder(props.data.filters)
          transform = rotatez(props.data.rotation.deg)
          fontFamily = (props.data.font?.family ?: "Arial") as FontFamily?
          fontWeight = (props.data.font?.fontWeight ?: "normal") as FontWeight?
          fontStyle = (props.data.font?.fontStyle ?: "normal") as FontStyle?
          fontSize = props.data.font?.size?.em
          color = Color(props.data.font?.color ?: "black")
          justifyContent =
              when (props.data.alignment.first) {
                "left" -> JustifyContent.flexStart
                "center" -> JustifyContent.center
                "right" -> JustifyContent.flexEnd
                else -> JustifyContent.center
              }
          textAlign =
              when (props.data.alignment.first) {
                "left" -> TextAlign.left
                "center" -> TextAlign.center
                "right" -> TextAlign.right
                else -> TextAlign.center
              }
          alignItems =
              when (props.data.alignment.second) {
                "top" -> AlignItems.flexStart
                "center" -> AlignItems.center
                "bottom" -> AlignItems.flexEnd
                else -> AlignItems.center
              }

          left = props.data.offsetX.em
          top = props.data.offsetY.em
          opacity = number(props.data.transparency)
        }
        +props.data.text
      }
    }

internal inline val bgwTextVisual: IntrinsicType<HTMLAttributes<Element>>
  get() = "bgw_text_visual".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()
