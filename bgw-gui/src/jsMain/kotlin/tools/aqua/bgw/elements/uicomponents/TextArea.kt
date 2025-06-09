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

import TextAreaData
import csstype.PropertiesBuilder
import data.event.TextInputChangedEventData
import emotion.react.css
import react.*
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML.textarea
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

internal external interface TextAreaProps : Props {
  var data: TextAreaData
}

internal fun PropertiesBuilder.cssBuilderIntern(componentViewData: TextAreaData) {
  cssBuilder(componentViewData)
}

internal val TextArea =
    FC<TextAreaProps> { props ->
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

      useEffect(listOf(props.data.text)) {
        // Increment prop change counter to track distinct prop updates
        propChangeCount.current = propChangeCount.current?.plus(1)

        // Check if text from JVM is different from last handled text
        val isNewText = props.data.text != lastHandledText.current

        if (isTyping.current === true) {
          // When user is typing, just track that this update happened but don't apply it yet
        } else if (isNewText || props.data.text != inputValue) {
          // Apply the update if:
          // 1. It's a new text value from the JVM that we haven't handled before OR
          // 2. The current input value doesn't match the JVM value
          lastHandledText.current = props.data.text
          setInputValue(props.data.text)
        } else {
          // For handling cases where multiple identical updates arrive while not typing
        }
      }

      // Explicitly handle focus/blur events to improve typing detection
      val handleFocus = { isTyping.current = true }

      val handleBlur = {
        // Small delay before marking as no longer typing (to handle click interactions)
        setTimeout(
            {
              isTyping.current = false

              // When user finishes typing, check if we need to apply any pending JVM updates
              if (props.data.text != inputValue) {
                lastHandledText.current = props.data.text
                setInputValue(props.data.text)
              }
            },
            200)
      }

      bgwTextArea {
        id = props.data.id
        key = props.data.id
        className = ClassName("textArea")
        css { cssBuilderIntern(props.data) }

        ref = elementRef
        useEffect { elementRef.current?.let { droppable.setNodeRef(it) } }

        bgwVisuals {
          className = ClassName("visuals")
          +VisualBuilder.build(props.data.visual)
        }

        textarea {
          placeholder = props.data.prompt
          defaultValue = props.data.text
          value = inputValue
          spellCheck = false
          css {
            fontBuilder(props.data)
            inputBuilder(props.data)
            resize = None.none
            boxSizing = BoxSizing.borderBox
            outline = None.none
            border = None.none

            placeholder { placeholderFontBuilder(props.data) }
          }
          readOnly = props.data.isReadonly
          onChange = {
            isTyping.current = true

            val value = it.target.value
            setInputValue(value)

            JCEFEventDispatcher.dispatchEvent(
                TextInputChangedEventData(value).apply { id = props.data.id })

            // Reset typing state after a short delay
            if (typingTimeout.current != null) {
              clearTimeout(typingTimeout.current)
            }
            typingTimeout.current =
                setTimeout(
                    {
                      isTyping.current = false

                      // Check for pending updates when typing stops
                      if (props.data.text != value && value.isNotBlank()) {
                        lastHandledText.current = props.data.text
                        setInputValue(props.data.text)
                      }
                    },
                    400)
          }

          onBlur = { handleBlur() }
        }

        applyCommonEventHandlers(props.data)
      }
    }

internal inline val bgwTextArea: IntrinsicType<HTMLAttributes<Element>>
  get() = "bgw_text_area".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()
