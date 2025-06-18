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

import ListViewData
import csstype.PropertiesBuilder
import data.event.StructuredDataSelectEventData
import emotion.react.css
import react.*
import react.dom.html.HTMLAttributes
import tools.aqua.bgw.DroppableOptions
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.*
import tools.aqua.bgw.event.JCEFEventDispatcher
import tools.aqua.bgw.event.applyCommonEventHandlers
import tools.aqua.bgw.useDroppable
import web.cssom.*
import web.dom.Element

internal external interface ListViewProps : Props {
  var data: ListViewData
}

internal fun PropertiesBuilder.cssBuilderIntern(componentViewData: ListViewData) {
  cssBuilder(componentViewData)
}

internal val ListView =
    FC<ListViewProps> { props ->
      val droppable =
          useDroppable(
              object : DroppableOptions {
                override var id: String = props.data.id
                override var disabled = !props.data.isDroppable
              })

      val elementRef = useRef<Element>(null)

      bgwListView {
        id = props.data.id
        className = ClassName("listView")
        css { cssBuilderIntern(props.data) }

        ref = elementRef
        useEffect { elementRef.current?.let { droppable.setNodeRef(it) } }

        bgwVisuals {
          className = ClassName("visuals")
          +VisualBuilder.build(props.data.visual)
        }

        bgwScroll {
          className = ClassName("scroll")
          css {
            width = 100.pct
            height = 100.pct
            overflow = Auto.auto
            position = Position.relative
          }

          bgwContents {
            className = ClassName("components")
            id = props.data.id + "--components"
            css {
              width = 100.pct
              display = Display.flex
              flexDirection = FlexDirection.column
              alignItems = AlignItems.start
              minWidth = 100.pct - 10.bgw

              if (props.data.orientation == "horizontal") {
                flexDirection = FlexDirection.row
                alignItems = AlignItems.center
              }
            }

            props.data.items.forEachIndexed { index, item ->
              bgwText {
                className = ClassName("text")
                css {
                  boxSizing = BoxSizing.borderBox
                  paddingInline = 10.bgw
                  paddingBlock = 5.bgw
                  width = 100.pct
                  fontStyle = props.data.font!!.fontStyle.let { it.unsafeCast<FontStyle>() }
                  fontWeight = integer(props.data.font!!.fontWeight)
                  fontSize = props.data.font!!.size.bgw
                  fontFamily = cssFont(props.data.font!!.family)
                  color = props.data.font!!.color.unsafeCast<Color>()
                  minWidth = fit()

                  if (props.data.selectedItems.contains(index)) {
                    backgroundColor = props.data.selectionBackground.unsafeCast<Color>()
                  }
                }
                +item

                onClick = {
                  JCEFEventDispatcher.dispatchEvent(
                      StructuredDataSelectEventData(index).apply { id = props.data.id })
                }
              }
            }
          }

          applyCommonEventHandlers(props.data)
        }
      }
    }

internal inline val bgwListView: IntrinsicType<HTMLAttributes<Element>>
  get() = "bgw_list_view".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()

internal inline val bgwScroll: IntrinsicType<HTMLAttributes<Element>>
  get() = "bgw_scroll".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()
