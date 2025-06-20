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

import TokenViewData
import csstype.PropertiesBuilder
import emotion.react.css
import react.*
import react.dom.aria.ariaDescribedBy
import react.dom.aria.ariaDisabled
import react.dom.aria.ariaPressed
import react.dom.aria.ariaRoleDescription
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML.div
import tools.aqua.bgw.*
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.bgwVisuals
import tools.aqua.bgw.elements.cssBuilder
import tools.aqua.bgw.event.applyCommonEventHandlers
import tools.aqua.bgw.hooks.useComponentSignal
import web.cssom.*
import web.dom.Element

internal external interface TokenViewProps : Props {
  var data: TokenViewData
}

internal fun PropertiesBuilder.cssBuilderIntern(componentViewData: TokenViewData) {
  cssBuilder(componentViewData)
}

internal val TokenView =
    FC<TokenViewProps> { p ->
      val props = useComponentSignal<TokenViewData>(id = p.data.id, initialData = p.data)

      // ----------------------------------------------------------------------------------------------

      val draggable =
          useDraggable(
              object : DraggableOptions {
                override var id: String = props.id
                override var disabled = !props.isDraggable
              })

      val droppable =
          useDroppable(
              object : DroppableOptions {
                override var id: String = props.id
                override var disabled = !props.isDroppable
              })

      val style: PropertiesBuilder.() -> Unit = {
        cssBuilderIntern(props)
        translate =
            "${draggable.transform?.x?.px ?: 0.px} ${draggable.transform?.y?.px ?: 0.px}".unsafeCast<
                Translate>()
        cursor = if (props.isDraggable) Cursor.pointer else Cursor.default
      }

      val elementRef = useRef<Element>(null)

      bgwTokenView {
        id = props.id
        className = ClassName("tokenView")

        ref = elementRef
        useEffect {
          elementRef.current?.let { draggable.setNodeRef(it) }
          elementRef.current?.let { droppable.setNodeRef(it) }
        }

        css(style)

        bgwVisuals {
          className = ClassName("visuals")
          +VisualBuilder.build(props.visual)
        }

        if (props.isDraggable) {
          onPointerDown = { draggable.listeners.onPointerDown.invoke(it, props.id) }
        }

        applyCommonEventHandlers(props)

        ariaDescribedBy = draggable.attributes.ariaDescribedBy
        ariaDisabled = draggable.attributes.ariaDisabled
        ariaPressed = draggable.attributes.ariaPressed
        ariaRoleDescription = draggable.attributes.ariaRoleDescription
      }
    }

internal inline val bgwTokenView: IntrinsicType<HTMLAttributes<Element>>
  get() = "bgw_token_view".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()
