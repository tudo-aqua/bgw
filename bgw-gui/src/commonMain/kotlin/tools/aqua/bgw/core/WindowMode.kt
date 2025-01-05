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

package tools.aqua.bgw.core

/** Enum for different window modes.
 *
 * @since 0.1
 */
enum class WindowMode {
    /**
     * Starts Application as normal window with given dimensions. Overrides
     * [BoardGameApplication.isMaximized] as 'false'. Overrides [BoardGameApplication.isFullScreen] as
     * 'false'.
     */
    NORMAL,

    /**
     * Starts Application as maximized non-fullscreen window. Overrides
     * [BoardGameApplication.isMaximized] as 'true'. Overrides [BoardGameApplication.isFullScreen] as
     * 'false'.
     */
    @Deprecated("WindowMode.MAXIMIZED is no longer used as of BGW 1.0.")
    MAXIMIZED,

    /**
     * Starts Application as maximized window. Overrides [BoardGameApplication.isFullScreen] as
     * 'true'.
     *
     * Note: This does not override [BoardGameApplication.isMaximized] which might become relevant
     * after leaving fullscreen mode.
     */
    FULLSCREEN
}
