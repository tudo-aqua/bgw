/*
 * Copyright 2021-2024 The BoardGameWork Authors
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

@file:Suppress("unused")

package tools.aqua.bgw.event

/**
 * Event that gets raised for mouse wheel inputs.
 *
 * @constructor Creates a [ScrollEvent].
 *
 * @property direction The scroll direction.
 * @property isControlDown Whether control key was pressed.
 * @property isShiftDown Whether shift key was pressed.
 * @property isAltDown Whether alt key was pressed.
 */
class ScrollEvent(
    val direction: ScrollDirection,
    val isControlDown: Boolean,
    val isShiftDown: Boolean,
    val isAltDown: Boolean
) : InputEvent()
