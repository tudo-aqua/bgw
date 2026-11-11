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

package data.event

import FiniteDouble
import kotlinx.serialization.Serializable

/**
 * A pan or zoom command for a camera pane.
 *
 * @property sequence Distinguishes consecutive identical commands (e.g. scrolling twice by the same
 *   offset), which would otherwise result in an unchanged state that is not sent again.
 * @property panByTotalX Sum of the x offsets of all relative pans issued so far. The browser
 *   applies the difference to the sum it applied last, so relative pans are neither lost when
 *   several are sent as one state nor applied twice when a state is rendered again.
 * @property panByTotalY Like [panByTotalX] for the y offsets.
 */
@Serializable
internal data class InternalCameraPanData(
    val panTo: Pair<Double, Double>? = null,
    val panSmooth: Boolean = true,
    val panBy: Boolean = false,
    val zoom: Double? = null,
    val zoomOnly: Boolean = false,
    val sequence: Long = 0,
    val panByTotalX: FiniteDouble = 0.0,
    val panByTotalY: FiniteDouble = 0.0
) : EventData()
