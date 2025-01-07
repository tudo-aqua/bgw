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

import ToggleButtonData
import csstype.PropertiesBuilder
import data.event.RadioChangedEventData
import emotion.react.css
import react.*
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.label
import react.dom.html.ReactHTML.span
import tools.aqua.bgw.DroppableOptions
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.*
import tools.aqua.bgw.event.JCEFEventDispatcher
import tools.aqua.bgw.event.applyCommonEventHandlers
import tools.aqua.bgw.useDroppable
import web.cssom.*
import web.dom.Element
import web.html.InputType

internal external interface ToggleButtonProps : Props {
  var data: ToggleButtonData
}

internal fun PropertiesBuilder.cssBuilderIntern(componentViewData: ToggleButtonData) {
  cssBuilder(componentViewData)
  display = Display.flex
  alignItems = AlignItems.center
  justifyItems = JustifyItems.flexStart
  gap = 10.em
}

internal val ToggleButton =
    FC<ToggleButtonProps> { props ->
      val droppable =
          useDroppable(
              object : DroppableOptions {
                override var id: String = props.data.id
                override var disabled = !props.data.isDroppable
              })

      val elementRef = useRef<Element>(null)

      bgwToggleButton {
        id = props.data.id
        className = ClassName("textField")
        css { cssBuilderIntern(props.data) }

        ref = elementRef
        useEffect { elementRef.current?.let { droppable.setNodeRef(it) } }

        bgwVisuals {
          className = ClassName("visuals")
          +VisualBuilder.build(props.data.visual)
        }

        input {
          type = InputType.checkbox
          id = props.data.id + "--toggle"
          checked = props.data.isSelected
          name = props.data.group

          css {
            width = 20.em
            height = 20.em
            maxWidth = 20.em
            zIndex = integer(1)
          }
          onChange = {
            JCEFEventDispatcher.dispatchEvent(
                RadioChangedEventData(!props.data.isSelected).apply { id = props.data.id })
          }
        }

        span { className = ClassName("toggle") }

        label {
          className = ClassName("text")
          htmlFor = props.data.id + "--toggle"
          +props.data.text

          css {
            fontBuilder(props.data)
            alignmentBuilder(props.data)
            display = Display.flex
            width = 100.pct
            height = 100.pct
            position = Position.relative
          }
        }

        applyCommonEventHandlers(props.data)
      }
    }

internal inline val bgwToggleButton: IntrinsicType<HTMLAttributes<Element>>
  get() = "bgw_togglebutton".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()
