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

package data.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import tools.aqua.bgw.event.KeyCode

@Serializable
@SerialName("KeyEventData")
internal class KeyEventData(
    var keyCode: KeyCode,
    var character: String,
    var isControlDown: Boolean,
    var isShiftDown: Boolean,
    var isAltDown: Boolean,
    val action: KeyEventAction
) : InputEventData()

@Serializable
internal enum class KeyEventAction {
  PRESS,
  RELEASE,
  TYPE
}
