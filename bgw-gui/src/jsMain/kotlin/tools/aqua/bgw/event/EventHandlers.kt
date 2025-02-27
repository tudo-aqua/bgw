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

package tools.aqua.bgw.event

import ComponentViewData
import data.event.KeyEventAction
import kotlin.math.sign
import react.dom.html.HTMLAttributes
import tools.aqua.bgw.builder.ReactConverters.toKeyEventData
import tools.aqua.bgw.builder.ReactConverters.toMouseEnteredData
import tools.aqua.bgw.builder.ReactConverters.toMouseEventData
import tools.aqua.bgw.builder.ReactConverters.toMouseExitedData
import tools.aqua.bgw.builder.ReactConverters.toMousePressedEventData
import tools.aqua.bgw.builder.ReactConverters.toMouseReleasedEventData
import tools.aqua.bgw.builder.ReactConverters.toScrollEventData
import web.dom.Element
import web.timers.Timeout
import web.timers.clearTimeout
import web.timers.setTimeout

internal fun HTMLAttributes<Element>.applyCommonEventHandlers(props: ComponentViewData) {
  /*onContextMenu = {
    it.preventDefault()
    JCEFEventDispatcher.dispatchEvent(it.toMouseEventData(props.id))
  }*/
  onClick = { JCEFEventDispatcher.dispatchEvent(it.toMouseEventData(props.id)) }
  onMouseDown = { JCEFEventDispatcher.dispatchEvent(it.toMousePressedEventData(props.id)) }
  onMouseUp = { JCEFEventDispatcher.dispatchEvent(it.toMouseReleasedEventData(props.id)) }

  onAuxClick = { JCEFEventDispatcher.dispatchEvent(it.toMouseEventData(props.id)) }
  onKeyDown = {
    JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(props.id, KeyEventAction.PRESS))
    JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(props.id, KeyEventAction.TYPE))
  }
  onKeyUp = {
    JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(props.id, KeyEventAction.RELEASE))
  }
  // onWheel = { JCEFEventDispatcher.dispatchEvent(it.toScrollEventData(props.id)) }

  if (props.hasMouseEnteredEvent) {
    onMouseOver = { JCEFEventDispatcher.dispatchEvent(it.toMouseEnteredData(props.id)) }
  }

  if (props.hasMouseExitedEvent) {
    onMouseOut = { JCEFEventDispatcher.dispatchEvent(it.toMouseExitedData(props.id)) }
  }

  var debounceTimeout: Timeout? = null
  var lastScrollDirection: Double? = null

  onWheel = {
    val currentDirection = it.deltaY.sign
    if (debounceTimeout == null || currentDirection != lastScrollDirection) {
      debounceTimeout?.let { clearTimeout(it) }
      debounceTimeout =
          setTimeout(
              {
                JCEFEventDispatcher.dispatchEvent(it.toScrollEventData(props.id))
                debounceTimeout = null
              },
              200)
      lastScrollDirection = currentDirection
    }
  }
}
