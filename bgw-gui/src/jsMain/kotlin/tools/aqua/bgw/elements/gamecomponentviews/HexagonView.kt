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

package tools.aqua.bgw.elements.gamecomponentviews

import HexagonViewData
import csstype.PropertiesBuilder
import emotion.react.css
import kotlin.math.sqrt
import react.*
import react.dom.aria.*
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML.div
import tools.aqua.bgw.*
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.bgw
import tools.aqua.bgw.elements.bgwVisuals
import tools.aqua.bgw.elements.cssBuilder
import tools.aqua.bgw.event.applyCommonEventHandlers
import web.cssom.*
import web.dom.Element

internal external interface HexagonViewProps : Props {
  var data: HexagonViewData
}

internal fun PropertiesBuilder.cssBuilderIntern(componentViewData: HexagonViewData) {
  cssBuilder(componentViewData)
  justifyContent = JustifyContent.center
  alignItems = AlignItems.center
}

internal val HexagonView =
    FC<HexagonViewProps> { props ->
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

      val elementRef = useRef<Element>(null)

      bgwHexagonView {
        tabIndex = 0
        id = props.data.id
        className = ClassName("hexagonView")

        ariaDetails = "hex-${props.data.orientation}"

        ref = elementRef
        useEffect {
          elementRef.current?.let { draggable.setNodeRef(it) }
          elementRef.current?.let { droppable.setNodeRef(it) }
        }

        css {
          cssBuilderIntern(props.data)
          if (props.data.orientation == "pointy_top") {
            width = (sqrt(3.0) * props.data.size).bgw
            height = 2 * props.data.size.bgw
          } else {
            width = 2 * props.data.size.bgw
            height = (sqrt(3.0) * props.data.size).bgw
          }
          translate =
              "${draggable.transform?.x?.px ?: 0.px} ${draggable.transform?.y?.px ?: 0.px}".unsafeCast<
                  Translate>()
          cursor = if (props.data.isDraggable) Cursor.pointer else Cursor.default
        }

        bgwVisuals {
          className = ClassName("visuals")
          +VisualBuilder.build(props.data.visual)
        }

        if (props.data.isDraggable) {
          onPointerDown = { draggable.listeners.onPointerDown.invoke(it, props.data.id) }
        }

        applyCommonEventHandlers(props.data)

        ariaDescribedBy = draggable.attributes.ariaDescribedBy
        ariaDisabled = draggable.attributes.ariaDisabled
        ariaPressed = draggable.attributes.ariaPressed
        ariaRoleDescription = draggable.attributes.ariaRoleDescription
      }
    }

internal inline val bgwHexagonView: IntrinsicType<HTMLAttributes<Element>>
  get() = "bgw_hexagon_view".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()
