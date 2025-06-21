/*
 * Copyright 2021-2025 The BoardGameWork Authors
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
 * @constructor Creates a [WheelEvent].
 *
 * @property direction The scroll direction.
 * @property isControlDown Whether control key was pressed.
 * @property isShiftDown Whether shift key was pressed.
 * @property isAltDown Whether alt key was pressed.
 *
 * @param direction The scroll direction.
 * @param isControlDown Whether control key was pressed.
 * @param isShiftDown Whether shift key was pressed.
 * @param isAltDown Whether alt key was pressed.
 * @param scrollOffset The amount of scroll since last trigger.
 *
 * @see WheelDirection
 *
 * @since 0.7.1
 */
class WheelEvent(
    val direction: WheelDirection,
    val isControlDown: Boolean,
    val isShiftDown: Boolean,
    val isAltDown: Boolean,
    /**
     * The amount of scroll since last [WheelEvent] for the specific component. Value is always
     * positive, usage in combination with [direction] is recommended.
     *
     * @see direction
     *
     * @since 0.10
     */
    val scrollOffset: Double = 0.0
) : InputEvent()
