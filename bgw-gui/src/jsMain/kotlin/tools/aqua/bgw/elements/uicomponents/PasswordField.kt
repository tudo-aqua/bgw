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

import PasswordFieldData
import csstype.PropertiesBuilder
import data.event.TextInputChangedEventData
import emotion.react.css
import react.*
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML.input
import tools.aqua.bgw.DroppableOptions
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.*
import tools.aqua.bgw.event.JCEFEventDispatcher
import tools.aqua.bgw.event.applyCommonEventHandlers
import tools.aqua.bgw.useDroppable
import web.cssom.*
import web.dom.Element
import web.html.InputType

internal external interface PasswordFieldProps : Props {
  var data: PasswordFieldData
}

internal fun PropertiesBuilder.cssBuilderIntern(componentViewData: PasswordFieldData) {
  cssBuilder(componentViewData)
}

internal val PasswordField =
    FC<PasswordFieldProps> { props ->
      val droppable =
          useDroppable(
              object : DroppableOptions {
                override var id: String = props.data.id
                override var disabled = !props.data.isDroppable
              })

      val elementRef = useRef<Element>(null)

      val (inputValue, setInputValue) = useState(props.data.text)

      useEffect(listOf(props.data.text)) { setInputValue(props.data.text) }

      bgwPasswordField {
        id = props.data.id
        key = props.data.id
        className = ClassName("passwordField")
        css { cssBuilderIntern(props.data) }

        ref = elementRef

        useEffect { elementRef.current?.let { droppable.setNodeRef(it) } }

        bgwVisuals {
          className = ClassName("visuals")
          +VisualBuilder.build(props.data.visual)
        }

        input {
          placeholder = props.data.prompt
          type = InputType.password
          defaultValue = props.data.text
          value = inputValue
          spellCheck = false
          css {
            fontBuilder(props.data)
            inputBuilder(props.data)
            outline = None.none
            border = None.none
            textIndent = 1.em

            placeholder { placeholderFontBuilder(props.data) }
          }
          onChange = {
            val value = it.target.value
            setInputValue(value)
            JCEFEventDispatcher.dispatchEvent(
                TextInputChangedEventData(value).apply { id = props.data.id })
          }
        }

        applyCommonEventHandlers(props.data)
      }
    }

internal inline val bgwPasswordField: IntrinsicType<HTMLAttributes<Element>>
  get() = "bgw_password_field".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()
