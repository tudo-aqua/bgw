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

import kotlinx.serialization.Serializable

/**
 * Enum for all available mouse button clicks.
 *
 * @since 0.1
 */
@Serializable
enum class MouseButtonType {
    /** LEFT / primary mouse button click. */
    LEFT_BUTTON,

    /** RIGHT / secondary mouse button click. */
    RIGHT_BUTTON,

    /** MOUSE WHEEL button click. */
    MOUSE_WHEEL,

    /** All other mouse button clicks. */
    OTHER,

    /** Unspecified. */
    @Deprecated("MouseButtonType.UNSPECIFIED should not be used.")
    UNSPECIFIED
}
