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

import LinearLayoutData
import csstype.PropertiesBuilder
import emotion.react.css
import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import org.w3c.dom.get
import react.*
import react.dom.aria.*
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML.div
import tools.aqua.bgw.DraggableOptions
import tools.aqua.bgw.DroppableOptions
import tools.aqua.bgw.builder.NodeBuilder
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.bgwContents
import tools.aqua.bgw.elements.bgwVisuals
import tools.aqua.bgw.elements.cssBuilder
import tools.aqua.bgw.event.applyCommonEventHandlers
import tools.aqua.bgw.useDraggable
import tools.aqua.bgw.useDroppable
import web.cssom.*
import web.dom.Element

internal external interface LinearLayoutProps : Props {
  var data: LinearLayoutData
}

internal fun PropertiesBuilder.cssBuilderIntern(componentViewData: LinearLayoutData) {
  cssBuilder(componentViewData)
}

internal val LinearLayout =
    FC<LinearLayoutProps> { props ->
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

      bgwLinearLayout {
        id = props.data.id
        className = ClassName("linearLayout")

        css(style)

        ariaDetails = props.data.orientation

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
          id = props.data.id + "--components"
          css {
            width = 100.pct
            height = 100.pct
            display = Display.flex
            flexDirection =
                if (props.data.orientation == "horizontal") FlexDirection.row
                else FlexDirection.column
            if (props.data.orientation == "horizontal") {
              justifyContent =
                  when (props.data.alignment.first) {
                    "left" -> JustifyContent.flexStart
                    "center" -> JustifyContent.center
                    "right" -> JustifyContent.flexEnd
                    else -> JustifyContent.center
                  }
              alignItems =
                  when (props.data.alignment.second) {
                    "top" -> AlignItems.flexStart
                    "center" -> AlignItems.center
                    "bottom" -> AlignItems.flexEnd
                    else -> AlignItems.center
                  }
            } else {
              alignItems =
                  when (props.data.alignment.first) {
                    "left" -> AlignItems.flexStart
                    "center" -> AlignItems.center
                    "right" -> AlignItems.flexEnd
                    else -> AlignItems.center
                  }
              justifyContent =
                  when (props.data.alignment.second) {
                    "top" -> JustifyContent.flexStart
                    "center" -> JustifyContent.center
                    "bottom" -> JustifyContent.flexEnd
                    else -> JustifyContent.center
                  }
            }
            // gap = props.data.spacing.em
          }

          props.data.components.forEach {
            +NodeBuilder.build(it)
            /* div {
                css {
                    position = Position.relative
                    flex = Flex(number(1.0), number(1.0), Auto.auto)
                    maxWidth = it.width.em
                    width = it.width.em
                    // margin = (props.data.spacing / 2).em
                    marginTop = it.posY.em
                    display = Display.flex
                    alignItems = AlignItems.center
                    justifyContent = JustifyContent.center
                }

                +NodeBuilder.build(it)
            } */
          }

          useLayoutEffect(listOf(props.data)) {
            document.getElementById(props.data.id + "--components")?.let {
              for (i in 0 until it.childElementCount) {
                val child = it.children[i] as HTMLElement
                if (props.data.orientation == "vertical") {
                  child.style.marginBottom = "${props.data.spacing}em"
                  child.style.marginLeft = "0"
                } else {
                  child.style.marginRight = "${props.data.spacing}em"
                  child.style.marginTop = "0"
                }
              }
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

internal inline val bgwLinearLayout: IntrinsicType<HTMLAttributes<Element>>
  get() = "bgw_linear_layout".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()
