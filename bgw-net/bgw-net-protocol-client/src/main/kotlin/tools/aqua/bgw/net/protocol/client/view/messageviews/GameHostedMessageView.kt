/*
 * Copyright 2022-2024 The BoardGameWork Authors
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

import java.awt.Color
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.util.Font

/** [MessageView] displaying "Successfully hosted game with sessionID XXX". */
class GameHostedMessageView(sessionID: String) : MessageView() {

  private val messageHeight: Double = 50.0
  private val colorStyle = "-fx-background-color: #00eaff;"

  init {
    height = messageHeight
    addAll(
        Label(
                posX = 0,
                posY = height - messageHeight,
                width = width,
                height = messageHeight,
                text = "- Successfully hosted game with sessionID \"$sessionID\" -",
                font = Font(size = 12, color = Color.BLACK, fontWeight = Font.FontWeight.SEMI_BOLD))
            .apply { backgroundStyle = "$colorStyle$cornerStyle" })
  }
}
