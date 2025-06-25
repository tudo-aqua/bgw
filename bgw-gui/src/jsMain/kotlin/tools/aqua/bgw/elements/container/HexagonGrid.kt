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

package tools.aqua.bgw.elements.container

import HexagonGridData
import csstype.PropertiesBuilder
import emotion.react.css
import kotlin.math.abs
import kotlin.math.sqrt
import react.*
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML.div
import tools.aqua.bgw.DraggableOptions
import tools.aqua.bgw.DroppableOptions
import tools.aqua.bgw.builder.NodeBuilder
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.bgw
import tools.aqua.bgw.elements.bgwContents
import tools.aqua.bgw.elements.bgwVisuals
import tools.aqua.bgw.elements.cssBuilder
import tools.aqua.bgw.event.applyCommonEventHandlers
import tools.aqua.bgw.useDraggable
import tools.aqua.bgw.useDroppable
import web.cssom.*
import web.dom.Element

internal external interface HexagonGridProps : Props {
  var data: HexagonGridData
}

internal fun PropertiesBuilder.cssBuilderIntern(componentViewData: HexagonGridData) {
  cssBuilder(componentViewData)
  justifyContent = JustifyContent.flexStart
  alignItems = AlignItems.flexStart
}

internal val HexagonGrid =
    FC<HexagonGridProps> { props ->
      val draggable =
          useDraggable(
              object : DraggableOptions {
                override var id: String = props.data.id
                override var disabled = !props.data.isDraggable
              })

      val droppable =
          useDroppable(
              object : DroppableOptions {
                override var id: String = props.data.id
                override var disabled = !props.data.isDroppable
              })

      val style: PropertiesBuilder.() -> Unit = {
        cssBuilderIntern(props.data)
        translate =
            "${draggable.transform?.x?.px ?: 0.px} ${draggable.transform?.y?.px ?: 0.px}".unsafeCast<
                Translate>()
        cursor = if (props.data.isDraggable) Cursor.pointer else Cursor.default
      }

      val elementRef = useRef<Element>(null)

      bgwHexagonGrid {
        tabIndex = 0
        id = props.data.id
        className = ClassName("hexagonGrid")

        css(style)

        ref = elementRef
        useEffect {
          elementRef.current?.let { draggable.setNodeRef(it) }
          elementRef.current?.let { droppable.setNodeRef(it) }
        }

        bgwVisuals {
          className = ClassName("visuals")
          +VisualBuilder.build(props.data.visual)
        }

        if (props.data.isDraggable) {
          onPointerDown = { draggable.listeners.onPointerDown.invoke(it, props.data.id) }
        }

        bgwContents {
          className = ClassName("components")

          var minX = 0.0
          var maxX = 0.0
          var minY = 0.0
          var maxY = 0.0

          props.data.map.forEach {
            if (props.data.orientation == "pointy_top") {
              if (props.data.coordinateSystem == "offset") {
                bgwHexagonContent {
                  val size = it.value.size
                  val w = size * sqrt(3.0)
                  val h = 2 * size
                  val q = it.key.split("/")[0].toInt()
                  val r = it.key.split("/")[1].toInt()

                  val x =
                      if (r % 2 == 0) w * q + props.data.spacing * (q - 1)
                      else w * q + props.data.spacing * (q - 1) + w / 2

                  val y = h * 0.75 * r + props.data.spacing * (r - 1)

                  if (x < minX) minX = x
                  if (x + w > maxX) maxX = x + w
                  if (y < minY) minY = y
                  if (y + h > maxY) maxY = y + h

                  css {
                    position = Position.absolute
                    left = x.bgw + it.value.posX.bgw
                    top = y.bgw + it.value.posY.bgw
                  }
                  +NodeBuilder.build(it.value)
                }
              } else {
                bgwHexagonContent {
                  val size = it.value.size
                  val w = size * sqrt(3.0)
                  val h = 2 * size
                  var q = it.key.split("/")[0].toInt()
                  var r = it.key.split("/")[1].toInt()

                  q = q + (r - (r and 1)) / 2

                  val x =
                      if (r % 2 == 0) w * q + props.data.spacing * (q - 1)
                      else w * q + props.data.spacing * (q - 1) + w / 2

                  val y = h * 0.75 * r + props.data.spacing * (r - 1)

                  if (x < minX) minX = x
                  if (x + w > maxX) maxX = x + w
                  if (y < minY) minY = y
                  if (y + h > maxY) maxY = y + h

                  css {
                    position = Position.absolute
                    left = x.bgw + it.value.posX.bgw
                    top = y.bgw + it.value.posY.bgw
                  }
                  +NodeBuilder.build(it.value)
                }
              }
            } else {
              if (props.data.coordinateSystem == "offset") {
                bgwHexagonContent {
                  val size = it.value.size
                  val w = 2 * size
                  val h = size * sqrt(3.0)
                  val q = it.key.split("/")[0].toInt()
                  val r = it.key.split("/")[1].toInt()

                  val x = w * 0.75 * q + props.data.spacing * (q - 1)

                  val y =
                      if (q % 2 == 0) h * r + props.data.spacing * (r - 1)
                      else h * r + props.data.spacing * (r - 1) + h / 2

                  if (x < minX) minX = x
                  if (x + w > maxX) maxX = x + w
                  if (y < minY) minY = y
                  if (y + h > maxY) maxY = y + h

                  css {
                    position = Position.absolute
                    left = x.bgw + it.value.posX.bgw
                    top = y.bgw + it.value.posY.bgw
                  }
                  +NodeBuilder.build(it.value)
                }
              } else {
                bgwHexagonContent {
                  val size = it.value.size
                  val w = 2 * size
                  val h = size * sqrt(3.0)
                  var q = it.key.split("/")[0].toInt()
                  var r = it.key.split("/")[1].toInt()

                  r = r + (q - (q and 1)) / 2

                  val x = w * 0.75 * q + props.data.spacing * (q - 1)

                  val y =
                      if (q % 2 == 0) h * r + props.data.spacing * (r - 1)
                      else h * r + props.data.spacing * (r - 1) + h / 2

                  if (x < minX) minX = x
                  if (x + w > maxX) maxX = x + w
                  if (y < minY) minY = y
                  if (y + h > maxY) maxY = y + h

                  css {
                    position = Position.absolute
                    left = x.bgw + it.value.posX.bgw
                    top = y.bgw + it.value.posY.bgw
                  }
                  +NodeBuilder.build(it.value)
                }
              }
            }
            css {
              position = Position.absolute
              width = (maxX + abs(minX)).bgw
              height = (maxY + abs(minY)).bgw
              left = (-minX).bgw
              top = (-minY).bgw
            }
          }
        }

        applyCommonEventHandlers(props.data)

        ariaDescribedBy = draggable.attributes.ariaDescribedBy
        ariaDisabled = draggable.attributes.ariaDisabled
        ariaPressed = draggable.attributes.ariaPressed
        ariaRoleDescription = draggable.attributes.ariaRoleDescription
      }
    }

internal inline val bgwHexagonGrid: IntrinsicType<HTMLAttributes<Element>>
  get() = "bgw_hexagon_grid".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()

internal inline val bgwHexagonContent: IntrinsicType<HTMLAttributes<Element>>
  get() = "bgw_hexagon_content".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()
