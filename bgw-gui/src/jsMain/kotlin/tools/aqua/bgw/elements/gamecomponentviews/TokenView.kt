/*
 * Copyright 2025-2026 The BoardGameWork Authors
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
import react.dom.html.HTMLAttributes
import tools.aqua.bgw.*
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.bgwVisuals
import tools.aqua.bgw.elements.cssBuilder
import tools.aqua.bgw.elements.useAnimationCleanup
import tools.aqua.bgw.event.applyCommonEventHandlers
import web.cssom.*
import web.dom.Element

internal external interface TokenViewProps : Props {
  var data: TokenViewData
}

internal fun PropertiesBuilder.cssBuilderIntern(componentViewData: TokenViewData) {
  cssBuilder(componentViewData)
}

internal val TokenView =
    FC<TokenViewProps> { props ->
      // Clean up animation CSS when animation finishes
      useAnimationCleanup(props.data)

      // Track visual state for flip/stepped animations
      val (currentVisual, setCurrentVisual) = useState(props.data.visual)

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
            "${draggable.transform?.x?.px ?: 0.px} ${draggable.transform?.y?.px ?: 0.px}"
                .unsafeCast<Translate>()
        cursor = if (props.data.isDraggable) Cursor.pointer else Cursor.default
      }

      val elementRef = useRef<Element>(null)

      // Listen for visual updates from Animator
      useEffectWithCleanup(props.data.id) {
        val element = elementRef.current
        if (element != null) {
          val handler: (Any) -> Unit = { _ ->
            val animatorVisual = Animator.getCurrentVisual(props.data.id)
            if (animatorVisual != null) {
              setCurrentVisual(animatorVisual)
            } else {
              setCurrentVisual(props.data.visual)
            }
          }
          element.asDynamic().addEventListener("bgw-visual-update", handler)
          onCleanup { element.asDynamic().removeEventListener("bgw-visual-update", handler) }
        }
      }

      // Update visual when props change (for non-animation updates)
      useEffect(props.data.visual) {
        val animatorVisual = Animator.getCurrentVisual(props.data.id)
        if (animatorVisual == null) {
          setCurrentVisual(props.data.visual)
        }
      }

      bgwTokenView {
        id = props.data.id
        className = ClassName("tokenView")

        ref = elementRef
        useEffect {
          elementRef.current?.let { draggable.setNodeRef(it) }
          elementRef.current?.let { droppable.setNodeRef(it) }
        }

        css(style)

        bgwVisuals {
          className = ClassName("visuals")
          +VisualBuilder.build(currentVisual)
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

internal inline val bgwTokenView: IntrinsicType<HTMLAttributes<Element>>
  get() = "bgw_token_view".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()
