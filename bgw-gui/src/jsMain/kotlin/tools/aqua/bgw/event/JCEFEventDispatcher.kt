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

import data.event.AnimationCleanedEventData
import data.event.AnimationFinishedEventData
import data.event.EventData
import data.event.KeyEventData
import jsonMapper
import kotlinx.serialization.encodeToString
import tools.aqua.bgw.webSocket

internal object JCEFEventDispatcher : EventDispatcher {
  fun dispatchGlobalEvent(event: KeyEventData) {
    try {
      val json = jsonMapper.encodeToString(event)
      webSocket?.send("bgwGlobalQuery|$json")
    } catch (e: Throwable) {
      println("Error while dispatching event: $e")
    }
  }

  override fun dispatchEvent(event: AnimationFinishedEventData) {
    try {
      val json = jsonMapper.encodeToString(event)
      webSocket?.send("bgwAnimationFinishQuery|$json")
    } catch (e: Throwable) {
      println("Error while dispatching event: $e")
    }
  }

  override fun dispatchEvent(event: AnimationCleanedEventData) {
    try {
      val json = jsonMapper.encodeToString(event)
      webSocket?.send("bgwAnimationCleanedQuery|$json")
    } catch (e: Throwable) {
      println("Error while dispatching event: $e")
    }
  }

  override fun dispatchEvent(event: EventData) {
    try {
      val json = jsonMapper.encodeToString(event)
      webSocket?.send("bgwQuery|$json")
    } catch (e: Throwable) {
      println("Error while dispatching event: $e")
    }
  }
}
