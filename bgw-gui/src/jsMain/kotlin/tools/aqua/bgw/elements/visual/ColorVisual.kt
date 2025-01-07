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

import ColorVisualData
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

internal external interface ColorVisualProps : Props {
  var data: ColorVisualData
}

internal val ColorVisual =
    FC<ColorVisualProps> { props ->
      bgwColorVisual {
        id = props.data.id
        css {
          styleBuilder(props.data.style)
          flipBuilder(props.data.flipped)
          filterBuilder(props.data.filters)
          backgroundColor = Color(props.data.color)
          opacity = number(props.data.transparency)
        }
      }
    }

internal inline val bgwColorVisual: IntrinsicType<HTMLAttributes<Element>>
  get() = "bgw_color_visual".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()
