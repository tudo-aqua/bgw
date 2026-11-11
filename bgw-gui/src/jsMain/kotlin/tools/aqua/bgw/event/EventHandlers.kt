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

package tools.aqua.bgw.event

import ComponentViewData
import data.event.KeyEventAction
import kotlin.math.sign
import react.dom.events.WheelEvent
import react.dom.html.HTMLAttributes
import react.useEffectWithCleanup
import react.useRef
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
  if (props.isDisabled) return

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
    onMouseEnter = { JCEFEventDispatcher.dispatchEvent(it.toMouseEnteredData(props.id)) }
  }

  if (props.hasMouseExitedEvent) {
    onMouseLeave = { JCEFEventDispatcher.dispatchEvent(it.toMouseExitedData(props.id)) }
  }

  onWheel = {
    val delta = if (it.deltaY != 0.0) it.deltaY else it.deltaX
    if (delta != 0.0) WheelThrottle.onWheel(props.id, it, delta)
  }
}

/**
 * Limits the scroll events sent to the JVM while the wheel turns quickly.
 *
 * The first wheel input of a component is sent immediately. Further input is summed up and sent
 * every [INTERVAL_MILLIS] while the wheel keeps turning, and immediately when the direction
 * changes. So continuous scrolling makes steady progress, and no scrolled distance is lost.
 */
private object WheelThrottle {
  private const val INTERVAL_MILLIS = 50

  private class State {
    var timeout: Timeout? = null
    var pendingDelta = 0.0
    var lastEvent: WheelEvent<*>? = null
  }

  /** States of the components currently scrolled, by component id. */
  private val states = mutableMapOf<String, State>()

  fun onWheel(id: String, event: WheelEvent<*>, delta: Double) {
    val state = states.getOrPut(id) { State() }
    if (state.timeout == null) {
      send(id, event, delta)
      state.timeout = setTimeout({ flush(id) }, INTERVAL_MILLIS)
      return
    }
    val previous = state.lastEvent
    if (state.pendingDelta != 0.0 && state.pendingDelta.sign != delta.sign && previous != null) {
      send(id, previous, state.pendingDelta)
      state.pendingDelta = 0.0
    }
    state.pendingDelta += delta
    state.lastEvent = event
  }

  private fun flush(id: String) {
    val state = states[id] ?: return
    val event = state.lastEvent
    if (state.pendingDelta == 0.0 || event == null) {
      states.remove(id)
      return
    }
    send(id, event, state.pendingDelta)
    state.pendingDelta = 0.0
    state.lastEvent = null
    state.timeout = setTimeout({ flush(id) }, INTERVAL_MILLIS)
  }

  private fun send(id: String, event: WheelEvent<*>, delta: Double) {
    JCEFEventDispatcher.dispatchEvent(event.toScrollEventData(id, delta))
  }
}

fun <T> useDebouncedCallback(callback: (T) -> Unit, delay: Int): (T) -> Unit {
  val callbackRef = useRef(callback)
  val timeoutRef = useRef<Timeout>(null)

  useEffectWithCleanup(callback) { callbackRef.current = callback }

  return { value: T ->
    timeoutRef.current?.let { clearTimeout(it) }
    timeoutRef.current = setTimeout({ callbackRef.current?.let { it(value) } }, delay)
  }
}
