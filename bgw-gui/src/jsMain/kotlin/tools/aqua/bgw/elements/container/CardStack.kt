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

import CardStackData
import csstype.PropertiesBuilder
import emotion.react.css
import react.*
import react.dom.html.HTMLAttributes
import tools.aqua.bgw.DroppableOptions
import tools.aqua.bgw.builder.NodeBuilder
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.alignmentBuilder
import tools.aqua.bgw.elements.bgwContents
import tools.aqua.bgw.elements.bgwVisuals
import tools.aqua.bgw.elements.cssBuilder
import tools.aqua.bgw.event.applyCommonEventHandlers
import tools.aqua.bgw.useDroppable
import web.cssom.*
import web.dom.Element

internal external interface CardStackProps : Props {
  var data: CardStackData
}

internal fun PropertiesBuilder.cssBuilderIntern(componentViewData: CardStackData) {
  cssBuilder(componentViewData)
}

internal val CardStack =
    FC<CardStackProps> { props ->
      val droppable =
          useDroppable(
              object : DroppableOptions {
                override var id: String = props.data.id
                override var disabled = !props.data.isDroppable
              })

      val elementRef = useRef<Element>(null)

      bgwCardStack {
        id = props.data.id
        className = ClassName("cardStack")
        css { cssBuilderIntern(props.data) }

        ref = elementRef
        useEffect { elementRef.current?.let { droppable.setNodeRef(it) } }

        bgwVisuals {
          className = ClassName("visuals")
          +VisualBuilder.build(props.data.visual)
        }

        bgwContents {
          className = ClassName("components")
          css {
            width = 100.pct
            height = 100.pct
            display = Display.flex
            alignmentBuilder(props.data)
          }

          props.data.components.forEach { +NodeBuilder.build(it) }
        }

        applyCommonEventHandlers(props.data)
      }
    }

internal inline val bgwCardStack: IntrinsicType<HTMLAttributes<Element>>
  get() = "bgw_card_stack".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()
