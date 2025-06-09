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

import SceneData
import emotion.react.css
import react.FC
import react.IntrinsicType
import react.Props
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML.div
import tools.aqua.bgw.builder.NodeBuilder
import tools.aqua.bgw.builder.VisualBuilder
import web.cssom.*
import web.dom.Element

internal external interface SceneProps : Props {
  var data: SceneData
}

internal val Scene =
    FC<SceneProps> { props ->
      bgwScene {
        css {
          width = props.data.width.em
          height = props.data.height.em
          position = Position.relative
        }

        bgwVisuals {
          className = ClassName("visuals")
          +VisualBuilder.build(props.data.background)
        }

        bgwContents {
          className = ClassName("components")
          css {
            width = 100.pct
            height = 100.pct
            position = Position.absolute
            left = 0.px
            top = 0.px
          }
          props.data.components.forEach { +NodeBuilder.build(it) }
        }
      }
    }

internal inline val bgwScene: IntrinsicType<HTMLAttributes<Element>>
  get() = "bgw_scene".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()
