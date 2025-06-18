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
import preact.signals.core.computed
import preact.signals.core.effect
import preact.signals.react.useComputed
import preact.signals.react.useSignal
import preact.signals.react.useSignalEffect
import react.*
import react.dom.aria.ariaDescribedBy
import react.dom.aria.ariaDisabled
import react.dom.aria.ariaPressed
import react.dom.aria.ariaRoleDescription
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.span
import tools.aqua.bgw.*
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.bgwVisuals
import tools.aqua.bgw.elements.cssBuilder
import tools.aqua.bgw.event.applyCommonEventHandlers
import web.cssom.*
import web.dom.Element
import kotlin.js.Date

internal external interface TokenViewProps : Props {
  var data: TokenViewData
}

internal fun PropertiesBuilder.cssBuilderIntern(componentViewData: TokenViewData) {
  cssBuilder(componentViewData)
}

val colorList = listOf(
    NamedColor.green,
    NamedColor.blue,
    NamedColor.yellow,
    NamedColor.purple,
    NamedColor.orange,
    NamedColor.cyan
)

internal val TokenView =
    FC<TokenViewProps> { props ->
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

      // Get the signal for this component's ID
      val componentSignal = getOrCreateSignal(props.data.id)
      val (count, setCount) = useState(0)

      // Subscribe to signal changes and update React state
      useSignalEffect {
        val signalValue = componentSignal.value
        console.log("TokenView: Signal changed at ${Date.now()} for ${props.data.id} to $signalValue")
        setCount(signalValue)
      }

      useEffect(listOf(count)) {
        // This effect runs whenever the count changes
        console.log("TokenView: Count finally updated to $count for ${props.data.id} at ${Date.now()}")
        console.log("^^^^^^^^")
      }

      val style: PropertiesBuilder.() -> Unit = {
        cssBuilderIntern(props.data)
        translate =
            "${draggable.transform?.x?.px ?: 0.px} ${draggable.transform?.y?.px ?: 0.px}".unsafeCast<
                Translate>()
        cursor = if (props.data.isDraggable) Cursor.pointer else Cursor.default
      }

      val elementRef = useRef<Element>(null)

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
          +VisualBuilder.build(props.data.visual)
        }

        // Display update counter from signal
        span {
          css {
            position = Position.absolute
            top = 0.em
            right = 0.em
            backgroundColor = if(count == 0) NamedColor.red else colorList.random()
            color = NamedColor.white
            display = Display.flex
            alignItems = AlignItems.center
            justifyContent = JustifyContent.center
            width = 100.pct
            height = 100.pct
            fontSize = 12.em
            fontWeight = FontWeight.bold
          }
          // Use the React state value which gets updated by the signal
          +count.toString()
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
