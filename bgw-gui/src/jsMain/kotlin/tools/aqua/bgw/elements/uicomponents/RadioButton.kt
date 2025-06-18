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

import RadioButtonData
import csstype.PropertiesBuilder
import data.event.RadioChangedEventData
import emotion.react.css
import react.*
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.label
import tools.aqua.bgw.DroppableOptions
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.*
import tools.aqua.bgw.event.JCEFEventDispatcher
import tools.aqua.bgw.event.applyCommonEventHandlers
import tools.aqua.bgw.useDroppable
import web.cssom.*
import web.dom.Element
import web.html.InputType

internal external interface RadioButtonProps : Props {
  var data: RadioButtonData
}

internal fun PropertiesBuilder.cssBuilderIntern(componentViewData: RadioButtonData) {
  display = if (componentViewData.isVisible) Display.flex else None.none
  cssBuilder(componentViewData)
  alignItems = AlignItems.center
  justifyItems = JustifyItems.flexStart
  gap = 10.bgw
  pointerEvents = if (componentViewData.isDisabled) None.none else PointerEvents.all
}

internal val RadioButton =
    FC<RadioButtonProps> { props ->
      val droppable =
          useDroppable(
              object : DroppableOptions {
                override var id: String = props.data.id
                override var disabled = !props.data.isDroppable
              })

      val elementRef = useRef<Element>(null)

      bgwRadioButton {
        id = props.data.id
        className = ClassName("textField")
        css {
          cssBuilderIntern(props.data)
          cursor = Cursor.pointer
        }

        ref = elementRef
        useEffect { elementRef.current?.let { droppable.setNodeRef(it) } }

        bgwVisuals {
          className = ClassName("visuals")
          +VisualBuilder.build(props.data.visual)
        }

        input {
          type = InputType.radio
          id = props.data.id + "--radio"
          checked = props.data.isSelected
          name = props.data.group

          css {
            width = 20.bgw
            height = 20.bgw
            maxWidth = 20.bgw
            zIndex = integer(1)
            outline = None.none
            border = None.none
          }
          onChange = {
            JCEFEventDispatcher.dispatchEvent(
                RadioChangedEventData(!props.data.isSelected).apply { id = props.data.id })
          }
        }

        label {
          className = ClassName("text")
          htmlFor = props.data.id + "--radio"
          +props.data.text

          css {
            fontBuilder(props.data)
            alignmentBuilder(props.data)
            display = Display.flex
            width = 100.pct
            height = 100.pct
            position = Position.relative
            wordBreak = WordBreak.breakAll
          }
        }

        applyCommonEventHandlers(props.data)
      }
    }

internal inline val bgwRadioButton: IntrinsicType<HTMLAttributes<Element>>
  get() = "bgw_radiobutton".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()
