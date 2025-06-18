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

import CheckBoxData
import csstype.PropertiesBuilder
import data.event.CheckBoxChangedEventData
import emotion.react.css
import kotlinx.browser.document
import org.w3c.dom.HTMLInputElement
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

internal external interface CheckBoxProps : Props {
  var data: CheckBoxData
}

internal fun PropertiesBuilder.cssBuilderIntern(componentViewData: CheckBoxData) {
  cssBuilder(componentViewData)
  display = if (componentViewData.isVisible) Display.flex else None.none
  alignItems = AlignItems.center
  justifyItems = JustifyItems.flexStart
  gap = 10.bgw
}

internal val CheckBox =
    FC<CheckBoxProps> { props ->
      val droppable =
          useDroppable(
              object : DroppableOptions {
                override var id: String = props.data.id
                override var disabled = !props.data.isDroppable
              })

      val elementRef = useRef<Element>(null)

      bgwCheckBox {
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
          id = props.data.id + "--checkbox"
          checked = props.data.isChecked

          useEffect(
              listOf(
                  props.data.isChecked,
                  props.data.isIndeterminate,
                  props.data.allowIndeterminate)) {
            document.getElementById(props.data.id + "--checkbox")?.let {
              (it as HTMLInputElement).indeterminate =
                  if (!props.data.isChecked) props.data.isIndeterminate else false
            }
          }

          css {
            width = 20.bgw
            height = 20.bgw
            maxWidth = 20.bgw
            zIndex = integer(1)
          }
          onChange = {
            JCEFEventDispatcher.dispatchEvent(
                CheckBoxChangedEventData(!props.data.isChecked).apply { id = props.data.id })
          }
        }

        label {
          className = ClassName("text")
          htmlFor = props.data.id + "--checkbox"
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

internal inline val bgwCheckBox: IntrinsicType<HTMLAttributes<Element>>
  get() = "bgw_checkbox".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()
