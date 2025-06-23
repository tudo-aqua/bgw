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

package tools.aqua.bgw.hooks

import AppData
import ComponentViewData
import kotlin.js.Date
import preact.signals.react.useSignalEffect
import react.useState
import tools.aqua.bgw.getOrCreateAppSignal
import tools.aqua.bgw.getOrCreateSignal

/**
 * Custom hook that connects a component to the signals system. It handles getting or creating the
 * signal for a component and updating the props when the signal changes.
 *
 * @param initialData The initial data for the app
 * @return The props object that will automatically update when the signal changes
 */
internal fun useAppSignal(
    initialData: AppData
): AppData {
    // Get or create signal for this component
    val componentSignal = getOrCreateAppSignal(initialData)

    // Set up React state to hold the component props
    val (props, setProps) = useState(initialData)

    // Subscribe to signal changes and update React state
    useSignalEffect {
        val signalValue = componentSignal.value
        console.log("App Signal changed at ${Date.now()}")
        setProps(signalValue)
    }

    return props
}
