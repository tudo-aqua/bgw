/*
 * Copyright 2026 The BoardGameWork Authors
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

import SliderData
import csstype.PropertiesBuilder
import data.event.SliderChangedEventData
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

internal external interface SliderProps : Props {
  var data: SliderData
}

internal fun PropertiesBuilder.cssBuilderIntern(componentViewData: SliderData) {
  cssBuilder(componentViewData)
}

internal val Slider =
    FC<SliderProps> { props ->
      useAnimationCleanup(props.data)

      val droppable =
          useDroppable(
              object : DroppableOptions {
                override var id: String = props.data.id
                override var disabled = !props.data.isDroppable || props.data.isDisabled
              })

      val elementRef = useRef<Element>(null)

      bgwSlider {
        id = props.data.id
        className = ClassName("slider")
        css { cssBuilderIntern(props.data) }

        ref = elementRef
        useEffectWithCleanup(props.data.id) { elementRef.current?.let { droppable.setNodeRef(it) } }

        bgwVisuals {
          className = ClassName("visuals")
          +VisualBuilder.build(props.data.visual)
        }

        // The thumb is a square on the track. Its side is the extent across the track.
        val vertical = props.data.orientation == "vertical"
        val thumbSize = if (vertical) props.data.width else props.data.height
        val range = props.data.maxValue - props.data.minValue
        val fraction =
            if (range > 0.0) ((props.data.value - props.data.minValue) / range).coerceIn(0.0, 1.0)
            else 0.0

        bgwSliderThumb {
          className = ClassName("slider-thumb")
          css {
            position = Position.absolute
            width = thumbSize.bgw
            height = thumbSize.bgw
            if (vertical) {
              left = 0.px
              top = ((1.0 - fraction) * (props.data.height - thumbSize)).bgw
            } else {
              top = 0.px
              left = (fraction * (props.data.width - thumbSize)).bgw
            }
            pointerEvents = None.none
          }
          props.data.thumbVisual?.let {
            bgwVisuals {
              className = ClassName("visuals")
              +VisualBuilder.build(it)
            }
          }
        }

        // The native input only handles the interaction. It is invisible, and its thumb has the
        // size of the rendered thumb, so pointer positions map to the same values.
        input {
          type = InputType.range
          value = props.data.value
          min = props.data.minValue
          max = props.data.maxValue
          step = props.data.step
          disabled = props.data.isDisabled
          tabIndex = if (props.data.isFocusable && !props.data.isDisabled) 0 else -1

          css {
            position = Position.absolute
            top = 0.px
            left = 0.px
            width = 100.pct
            height = 100.pct
            margin = 0.px
            padding = 0.px
            opacity = number(0.0)
            appearance = None.none
            cursor = Cursor.pointer

            if (vertical) {
              writingMode = WritingMode.verticalLr
              direction = Direction.rtl
            }

            "&::-webkit-slider-thumb" {
              appearance = None.none
              width = thumbSize.bgw
              height = thumbSize.bgw
            }
          }

          onChange = {
            it.target.value.toDoubleOrNull()?.let { value ->
              JCEFEventDispatcher.dispatchEvent(
                  SliderChangedEventData(value).apply { id = props.data.id })
            }
          }
        }

        applyCommonEventHandlers(props.data)
      }
    }

internal inline val bgwSlider: IntrinsicType<HTMLAttributes<Element>>
  get() = "bgw_slider".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()

internal inline val bgwSliderThumb: IntrinsicType<HTMLAttributes<Element>>
  get() = "bgw_slider_thumb".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()
