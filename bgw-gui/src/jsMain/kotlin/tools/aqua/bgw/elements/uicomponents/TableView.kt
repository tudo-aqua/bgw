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

import TableViewData
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

internal external interface TableViewProps : Props {
  var data: TableViewData
}

internal fun PropertiesBuilder.cssBuilderIntern(componentViewData: TableViewData) {
  cssBuilder(componentViewData)
}

internal val TableView =
    FC<TableViewProps> { props ->
      val droppable =
          useDroppable(
              object : DroppableOptions {
                override var id: String = props.data.id
                override var disabled = !props.data.isDroppable
              })

      val elementRef = useRef<Element>(null)

      bgwTableView {
        id = props.data.id
        className = ClassName("tableView")
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
            display = Display.flex
            alignItems = AlignItems.start
          }

          props.data.columns.forEachIndexed { i, column ->
            bgwContents {
              className = ClassName("column")
              id = props.data.id + "--column-$i"
              css {
                width = column.width.em
                minWidth = column.width.em
                display = Display.flex
                flexDirection = FlexDirection.column
                alignItems = AlignItems.start
              }

              bgwText {
                className = ClassName("text")
                css {
                  padding = 5.em
                  paddingTop = 3.em
                  paddingBottom = 8.em
                  width = 100.pct - 10.em
                  fontStyle = column.font.fontStyle.let { it.unsafeCast<FontStyle>() }
                  fontWeight = integer(column.font.fontWeight)
                  fontSize = column.font.size.em
                  fontFamily = cssFont(column.font.family)
                  color = column.font.color.unsafeCast<Color>()
                  minWidth = fit()
                }
                +column.title
              }

              column.items.forEachIndexed { index, item ->
                bgwText {
                  className = ClassName("text")
                  css {
                    padding = 5.em
                    paddingTop = 3.em
                    paddingBottom = 3.em
                    width = 100.pct - 10.em
                    fontStyle = column.font.fontStyle.let { it.unsafeCast<FontStyle>() }
                    fontWeight = integer(column.font.fontWeight)
                    fontSize = column.font.size.em
                    fontFamily = cssFont(column.font.family)
                    color = column.font.color.unsafeCast<Color>()
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
          }

          applyCommonEventHandlers(props.data)
        }
      }
    }

internal inline val bgwTableView: IntrinsicType<HTMLAttributes<Element>>
  get() = "bgw_list_view".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()

internal inline val bgwTableColumn: IntrinsicType<HTMLAttributes<Element>>
  get() = "bgw_table_column".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()
