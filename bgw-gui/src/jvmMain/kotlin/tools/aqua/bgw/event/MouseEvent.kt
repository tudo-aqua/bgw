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
 * Event that gets raised for mouse inputs.
 *
 * @constructor Creates a [MouseEvent] with given [button].
 *
 * @property button Corresponding mouse button enum value.
 * @param button Corresponding mouse button enum value.
 * @param posX The X-coordinate of the mouse event position.
 * @param posY The Y-coordinate of the mouse event position.
 *
 * @since 0.1
 */
class MouseEvent(
    val button: MouseButtonType,

    /**
     * The X-coordinate of the mouse event position.
     *
     * @since 0.8
     */
    val posX: Number,

    /**
     * The Y-coordinate of the mouse event position.
     *
     * @since 0.8
     */
    val posY: Number
) : InputEvent()
