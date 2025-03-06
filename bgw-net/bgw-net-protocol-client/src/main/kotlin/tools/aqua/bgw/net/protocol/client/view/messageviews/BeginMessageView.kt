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

import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual

/** [MessageView] displaying "Beginning of conversation". */
class BeginMessageView : MessageView(ColorVisual(Color(0xc27e00))) {

  private val messageHeight: Double = 50.0
  private val colorStyle = "-fx-background-color: #c27e00;"

  init {
    height = messageHeight
    addAll(
        Label(
            posX = 0,
            posY = height - messageHeight,
            width = width,
            height = messageHeight,
            text = "- Beginning of conversation -",
            font = Font(size = 12, color = Color.BLACK, fontWeight = Font.FontWeight.NORMAL)))
  }
}
