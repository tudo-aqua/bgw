/*
 * Copyright 2022-2025 The BoardGameWork Authors
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

package tools.aqua.bgw.net.protocol.client.view.messageviews

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.style.BorderRadius
import tools.aqua.bgw.visual.SingleLayerVisual

/** [MessageView] baseclass. */
sealed class MessageView(visual: SingleLayerVisual) :
    Pane<ComponentView>(
        posX = 0,
        posY = 0,
        height = 0,
        width = 450,
        visual =
            visual.apply {
              transparency = 0.2
              style.borderRadius = BorderRadius(10.0)
            })
