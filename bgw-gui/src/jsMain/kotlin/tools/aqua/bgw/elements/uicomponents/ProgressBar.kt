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

package tools.aqua.bgw.elements.uicomponents

import ColorVisualData
import CompoundVisualData
import ProgressBarData
import csstype.PropertiesBuilder
import emotion.react.css
import react.*
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML.div
import tools.aqua.bgw.*
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.*
import tools.aqua.bgw.event.applyCommonEventHandlers
import web.cssom.*
import web.dom.Element

internal external interface ProgressBarProps : Props {
  var data: ProgressBarData
}

internal fun PropertiesBuilder.cssBuilderIntern(componentViewData: ProgressBarData) {
  cssBuilder(componentViewData)
}

internal val ProgressBar =
    FC<ProgressBarProps> { props ->
      val droppable =
          useDroppable(
              object : DroppableOptions {
                override var id: String = props.data.id
                override var disabled = !props.data.isDroppable
              })

      val elementRef = useRef<Element>(null)
      val colorVisual =
          (props.data.barVisual as CompoundVisualData).children.first() as ColorVisualData

      bgwProgress {
        tabIndex = 0
        id = props.data.id
        className = ClassName("progress")
        css { cssBuilderIntern(props.data) }

        ref = elementRef
        useEffect { elementRef.current?.let { droppable.setNodeRef(it) } }

        bgwVisuals {
          className = ClassName("visuals")
          +VisualBuilder.build(props.data.visual)
        }

        bgwProgressBar {
          id = "${props.data.id}--bar"
          className = ClassName("progress-bar")
          css {
            width = (props.data.progress * 100).pct
            height = 100.pct
            backgroundColor = Color(colorVisual.color)
            position = Position.absolute
            left = 0.px
            top = 0.px
            transition = transition(500, "width")
            styleBuilder(colorVisual.style)
            filterBuilder(colorVisual.filters)
          }
        }

        applyCommonEventHandlers(props.data)
      }
    }

internal inline val bgwProgress: IntrinsicType<HTMLAttributes<Element>>
  get() = "bgw_progress".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()

internal inline val bgwProgressBar: IntrinsicType<HTMLAttributes<Element>>
  get() = "bgw_progress_bar".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()
