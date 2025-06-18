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

import TextFieldData
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
import web.timers.Timeout
import web.timers.clearTimeout
import web.timers.setTimeout

internal external interface TextFieldProps : Props {
  var data: TextFieldData
}

internal fun PropertiesBuilder.cssBuilderIntern(componentViewData: TextFieldData) {
  cssBuilder(componentViewData)
}

internal val TextField =
    FC<TextFieldProps> { props ->
      val droppable =
          useDroppable(
              object : DroppableOptions {
                override var id: String = props.data.id
                override var disabled = !props.data.isDroppable
              })

      val elementRef = useRef<Element>(null)

      val (inputValue, setInputValue) = useState(props.data.text)
      val isTyping = useRef(false) // Track if user is actively typing
      val typingTimeout = useRef<Timeout>(null)
      val lastHandledText = useRef(props.data.text) // Track last handled text from JVM
      val propChangeCount = useRef(0) // Count prop changes to detect repeats
      val lastUserInput = useRef(props.data.text) // Track last user input

      useEffect(listOf(props.data.text)) {
        // Increment prop change counter to track distinct prop updates
        propChangeCount.current = propChangeCount.current?.plus(1)

        // Check if text from JVM is different from last handled text
        val isNewText = props.data.text != lastHandledText.current

        if (isTyping.current === true) {
          // When user is typing, don't override their input
          // Store the JVM update to potentially apply later
          lastHandledText.current = props.data.text
        } else if (isNewText || props.data.text != inputValue) {
          // Apply the update if:
          // 1. It's a new text value from the JVM that we haven't handled before OR
          // 2. The current input value doesn't match the JVM value
          lastHandledText.current = props.data.text
          setInputValue(props.data.text)
        }
      }

      // Explicitly handle focus/blur events to improve typing detection
      val handleFocus = {
        // Clear any pending typing timeout when focusing
        if (typingTimeout.current != null) {
          clearTimeout(typingTimeout.current)
          typingTimeout.current = null
        }
        isTyping.current = true
      }

      val handleBlur = {
        // Immediately capture the current input value as the last user input
        lastUserInput.current = inputValue

        // Immediately dispatch the final value to ensure it's not lost
        JCEFEventDispatcher.dispatchEvent(
            TextInputChangedEventData(inputValue).apply { id = props.data.id })

        // Reset typing state immediately to prevent conflicts with other fields
        isTyping.current = false

        // Clear any pending timeout
        if (typingTimeout.current != null) {
          clearTimeout(typingTimeout.current)
          typingTimeout.current = null
        }
      }

      bgwTextField {
        id = props.data.id
        key = props.data.id
        className = ClassName("textField")
        css { cssBuilderIntern(props.data) }

        ref = elementRef
        useEffect { elementRef.current?.let { droppable.setNodeRef(it) } }

        bgwVisuals {
          className = ClassName("visuals")
          +VisualBuilder.build(props.data.visual)
        }

        input {
          placeholder = props.data.prompt
          defaultValue = props.data.text
          value = inputValue
          key = "textInput-${props.data.id}"
          spellCheck = false
          css {
            inputBuilder(props.data)
            fontBuilder(props.data)
            outline = None.none
            border = None.none
            boxSizing = BoxSizing.borderBox
            paddingLeft = 10.bgw

            placeholder { placeholderFontBuilder(props.data) }
          }
          readOnly = props.data.isReadonly
          onChange = {
            isTyping.current = true

            val value = it.target.value
            setInputValue(value)
            lastUserInput.current = value

            JCEFEventDispatcher.dispatchEvent(
                TextInputChangedEventData(value).apply { id = props.data.id })

            // Reset typing state after a shorter delay
            if (typingTimeout.current != null) {
              clearTimeout(typingTimeout.current)
            }
            typingTimeout.current =
                setTimeout(
                    {
                      isTyping.current = false
                      typingTimeout.current = null
                    },
                    200) // Reduced from 400ms to make it more responsive
          }

          onFocus = { handleFocus() }
          onBlur = { handleBlur() }
        }

        applyCommonEventHandlers(props.data)
      }
    }

internal inline val bgwTextField: IntrinsicType<HTMLAttributes<Element>>
  get() = "bgw_text_field".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()
