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

package tools.aqua.bgw.elements.layoutviews

import GridElementData
import GridPaneData
import csstype.PropertiesBuilder
import emotion.react.css
import react.*
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML.div
import tools.aqua.bgw.DroppableOptions
import tools.aqua.bgw.builder.NodeBuilder.build
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.*
import tools.aqua.bgw.event.applyCommonEventHandlers
import tools.aqua.bgw.useDroppable
import web.cssom.*
import web.dom.Element
import web.dom.document

internal external interface GridPaneProps : Props {
  var data: GridPaneData
}

internal fun PropertiesBuilder.cssBuilderIntern(componentViewData: GridPaneData) {
  cssBuilder(componentViewData)
}

internal external interface GridPaneElementProps : Props {
  var data: GridElementData
}

internal inline val bgwGridElement: IntrinsicType<HTMLAttributes<Element>>
  get() = "bgw_grid_element".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()

internal val ReactGridPane =
    FC<GridPaneProps> { props ->
      val droppable =
          useDroppable(
              object : DroppableOptions {
                override var id: String = props.data.id
                override var disabled = !props.data.isDroppable
              })

      val elementRef = useRef<Element>(null)

      bgwGridPane {
        tabIndex = 0
        id = props.data.id
        className = ClassName("gridPane")

        ref = elementRef
        useEffect { elementRef.current?.let { droppable.setNodeRef(it) } }

        css {
          cssBuilderIntern(props.data)
          width = fit()
          height = fit()

          if (props.data.layoutFromCenter) {
            useLayoutEffect(listOf(props.data)) {
              document.getElementById(props.data.id)?.let {
                val element = it
                val width = convertToPx(element.offsetWidth.toDouble())
                val height = convertToPx(element.offsetHeight.toDouble())
                val x = (props.data.posX - width / 2)
                val y = (props.data.posY - height / 2)
                element.style.left = x.bgw.toString()
                element.style.top = y.bgw.toString()
              }
            }
          } else {
            useLayoutEffect(listOf(props.data)) {
              document.getElementById(props.data.id)?.let {
                it.style.left = props.data.posX.bgw.toString()
                it.style.top = props.data.posY.bgw.toString()
              }
            }
          }
        }

        bgwVisuals {
          className = ClassName("visuals")
          +VisualBuilder.build(props.data.visual)
        }

        bgwContents {
          className = ClassName("components")
          css {
            gridTemplateColumns = repeat(props.data.columns, minContent())
            gridTemplateRows = repeat(props.data.rows, minContent())
            display = Display.grid
            width = fit()
            height = fit()
            gap = props.data.spacing.bgw
          }

          props.data.grid.forEach { gridElementData ->
            bgwGridElement {
              css {
                gridColumn = integer(gridElementData.column + 1)
                gridRow = integer(gridElementData.row + 1)
                justifySelf =
                    when (gridElementData.alignment.first) {
                      "left" -> JustifySelf.flexStart
                      "center" -> JustifySelf.center
                      "right" -> JustifySelf.flexEnd
                      else -> JustifySelf.center
                    }
                alignSelf =
                    when (gridElementData.alignment.second) {
                      "top" -> AlignSelf.flexStart
                      "center" -> AlignSelf.center
                      "bottom" -> AlignSelf.flexEnd
                      else -> AlignSelf.center
                    }
                position = Position.relative
                left = gridElementData.component?.posX?.bgw
                top = gridElementData.component?.posY?.bgw
              }

              gridElementData.component?.let { +build(it) }
            }
          }
        }

        applyCommonEventHandlers(props.data)
      }
    }
internal inline val bgwGridPane: IntrinsicType<HTMLAttributes<Element>>
  get() = "bgw_grid_pane".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()
