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

package tools.aqua.bgw.net.protocol.client.view

import kotlin.math.max
import kotlin.math.min
import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.net.protocol.client.view.messageviews.MessageView
import tools.aqua.bgw.net.protocol.client.view.messageviews.SpacerMessageView
import tools.aqua.bgw.visual.ColorVisual

/** The protocol screen. */
class ProtocolScene : BoardGameScene(height = 800, width = 500, background = ColorVisual.WHITE) {

  private var currentZMin = 0.0
  private val messagePane =
      GridPane<MessageView>(
              posX = 25, posY = 0, columns = 1, rows = 1, layoutFromCenter = false, spacing = 10)
          .apply { onMouseWheel = { posY = min(max(posY + it.direction * 50, currentZMin), -40.0) } }

  init {
    addComponents(messagePane)
    messagePane[0, 0] = SpacerMessageView()
  }

  /** Adds a [MessageView]. */
  fun addMessage(msg: MessageView) {
    messagePane.grow(bottom = 1)
    messagePane[0, messagePane.rows - 1] = msg
    messagePane.posY -= msg.height + 10
    currentZMin -= msg.height + 10
  }
}
