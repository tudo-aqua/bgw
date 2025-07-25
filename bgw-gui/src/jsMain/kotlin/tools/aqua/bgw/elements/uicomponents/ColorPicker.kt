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

import ColorPickerData
import csstype.PropertiesBuilder
import data.event.ColorInputChangedEventData
import emotion.react.css
import react.*
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML.input
import tools.aqua.bgw.DroppableOptions
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.*
import tools.aqua.bgw.event.JCEFEventDispatcher
import tools.aqua.bgw.event.applyCommonEventHandlers
import tools.aqua.bgw.useDroppable
import web.cssom.*
import web.dom.Element
import web.html.InputType
import web.timers.Timeout
import web.timers.clearTimeout
import web.timers.setTimeout

internal external interface ColorPickerProps : Props {
  var data: ColorPickerData
}

internal fun PropertiesBuilder.cssBuilderIntern(componentViewData: ColorPickerData) {
  cssBuilder(componentViewData)
}

internal val ColorPicker =
    FC<ColorPickerProps> { props ->
      val droppable =
          useDroppable(
              object : DroppableOptions {
                override var id: String = props.data.id
                override var disabled = !props.data.isDroppable
              })

      val elementRef = useRef<Element>(null)

      bgwColorPicker {
        id = props.data.id
        className = ClassName("colorPicker")
        css { cssBuilderIntern(props.data) }

        ref = elementRef
        useEffect { elementRef.current?.let { droppable.setNodeRef(it) } }

        bgwVisuals {
          className = ClassName("visuals")
          +VisualBuilder.build(props.data.visual)
        }

        input {
          type = InputType.color
          defaultValue = props.data.selectedColor
          value = props.data.selectedColor
          css {
            position = Position.absolute
            top = 0.px
            left = 0.px
            width = 100.pct
            height = 100.pct
            margin = 0.px
            padding = 0.px
            paddingLeft = 5.bgw
            paddingRight = 5.bgw
            paddingTop = 3.bgw
            paddingBottom = 3.bgw
            appearance = None.none
            border = None.none
            outline = None.none
            backgroundColor = rgb(0, 0, 0, 0.0)
          }

          var debounceTimeout: Timeout? = null

          onChange = {
            val value = it.target.value
            debounceTimeout?.let { clearTimeout(it) }
            debounceTimeout =
                setTimeout(
                    {
                      JCEFEventDispatcher.dispatchEvent(
                          ColorInputChangedEventData(value).apply { id = props.data.id })
                    },
                    200)
          }
        }

        applyCommonEventHandlers(props.data)
      }
    }

internal inline val bgwColorPicker: IntrinsicType<HTMLAttributes<Element>>
  get() = "bgw_color_picker".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()
