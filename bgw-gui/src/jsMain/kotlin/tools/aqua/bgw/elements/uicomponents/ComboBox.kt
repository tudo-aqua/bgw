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

import ComboBoxData
import csstype.PropertiesBuilder
import data.event.SelectionChangedEventData
import emotion.react.css
import react.*
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML.option
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.select
import tools.aqua.bgw.DroppableOptions
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.*
import tools.aqua.bgw.event.JCEFEventDispatcher
import tools.aqua.bgw.event.applyCommonEventHandlers
import tools.aqua.bgw.useDroppable
import web.cssom.*
import web.cssom.NamedColor.Companion.transparent
import web.dom.Element

internal external interface ComboBoxProps : Props {
  var data: ComboBoxData
}

internal fun PropertiesBuilder.cssBuilderIntern(componentViewData: ComboBoxData) {
  cssBuilder(componentViewData)
}

internal val ComboBox =
    FC<ComboBoxProps> { props ->
      val droppable =
          useDroppable(
              object : DroppableOptions {
                override var id: String = props.data.id
                override var disabled = !props.data.isDroppable
              })

      val elementRef = useRef<Element>(null)

      val selectedIndex = props.data.selectedItem?.first ?: -1

      bgwComboBox {
        id = props.data.id
        className = ClassName("comboBox")
        css { cssBuilderIntern(props.data) }

        ref = elementRef
        useEffect { elementRef.current?.let { droppable.setNodeRef(it) } }

        bgwVisuals {
          className = ClassName("visuals")
          +VisualBuilder.build(
              if (selectedIndex == -1) props.data.visual else props.data.itemVisuals[selectedIndex])
        }

        select {
          css {
            comboBoxBuilder(props.data)
            fontBuilder(props.data)
            outline = None.none
            border = None.none
            position = Position.absolute
            boxSizing = BoxSizing.borderBox
            paddingLeft = 1.em
            display = Display.flex
            alignItems = AlignItems.center
          }
          if (!props.data.disallowUnselect) {
            option {
              value = (-1).toString()
              selected = props.data.selectedItem == null

              css {
                border = None.none
                outline = None.none
                boxSizing = BoxSizing.borderBox
                paddingLeft = 1.em
                position = Position.relative
                background = transparent
              }

              +VisualBuilder.build(props.data.visual)

              p {
                +props.data.prompt

                css {
                  position = Position.relative
                  zIndex = zIndex(1)
                  margin = 0.px
                  cursor = Cursor.pointer
                  simpleFontBuilder(props.data)
                }
              }
            }
          }
          props.data.items.forEachIndexed { index, it ->
            option {
              value = it.first.toString()
              selected = props.data.selectedItem?.first == it.first

              css {
                border = None.none
                outline = None.none
                boxSizing = BoxSizing.borderBox
                paddingLeft = 1.em
                position = Position.relative
                background = transparent
              }

              +VisualBuilder.build(props.data.itemVisuals[index])

              p {
                +it.second

                css {
                  position = Position.relative
                  zIndex = zIndex(1)
                  margin = 0.px
                  cursor = Cursor.pointer
                  simpleFontBuilder(props.data)
                }
              }
            }
          }
          onChange = {
            val value = it.target.value.toInt()
            JCEFEventDispatcher.dispatchEvent(
                SelectionChangedEventData(value).apply { id = props.data.id })
          }
        }

        applyCommonEventHandlers(props.data)
      }
    }

internal inline val bgwComboBox: IntrinsicType<HTMLAttributes<Element>>
  get() = "bgw_combo_box".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()
