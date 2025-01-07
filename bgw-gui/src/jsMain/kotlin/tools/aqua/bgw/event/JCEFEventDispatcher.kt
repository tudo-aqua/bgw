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

import data.event.AnimationFinishedEventData
import data.event.EventData
import data.event.KeyEventData
import jsonMapper
import kotlinx.browser.window
import kotlinx.serialization.encodeToString

internal object JCEFEventDispatcher : EventDispatcher {
  init {
    initialize()
  }

  fun dispatchGlobalEvent(event: KeyEventData) {
    val json = jsonMapper.encodeToString(event)
    try {
      window.asDynamic().bgwSceneQuery(Base64.encode(json))
    } catch (e: Throwable) {
      println("Error while dispatching event: $e")
    }
  }

  override fun dispatchEvent(event: AnimationFinishedEventData) {
    val json = jsonMapper.encodeToString(event)
    try {
      window.asDynamic().bgwAnimationQuery(Base64.encode(json))
    } catch (e: Throwable) {
      println("Error while dispatching event: $e")
    }
  }

  override fun dispatchEvent(event: EventData) {
    val json = jsonMapper.encodeToString(event)
    try {
      window.asDynamic().bgwQuery(Base64.encode(json))
    } catch (e: Throwable) {
      println("Error while dispatching event: $e")
    }
  }

  private fun initialize() {
    js(
        "window.bgwQuery = function(request) { window.cefQuery({request: request, persistent: false, onSuccess: function (response) {}, onFailure: function (error_code, error_message) {}}) }")
    js(
        "window.bgwAnimationQuery = function(request) { window.cefAnimationQuery({request: request, persistent: false, onSuccess: function (response) {}, onFailure: function (error_code, error_message) {}}) }")
    js(
        "window.bgwSceneQuery = function(request) { window.cefSceneQuery({request: request, persistent: false, onSuccess: function (response) {}, onFailure: function (error_code, error_message) {}}) }")
  }
}
