/*
 * Copyright 2022 The BoardGameWork Authors
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

import kotlin.random.Random
import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.net.protocol.client.view.messageviews.ConnectedMessageView
import tools.aqua.bgw.net.protocol.client.view.messageviews.GameMessageView
import tools.aqua.bgw.net.protocol.client.view.messageviews.MessageView
import tools.aqua.bgw.visual.ColorVisual
import java.awt.Color
import java.time.LocalDateTime

class ProtocolScene : BoardGameScene(height = 800, width = 500, background = ColorVisual.WHITE) {

  private val texts = listOf(
    "uiawevbwuovuwevrvuwe",
    "iwBEVUIWbuevbwouevb|zowuvzee|wuzfgveugvu|kGVEuozgvOUFEG|VBRILBVEUIOR|BVUOWEBUVBRBZV",
    "iwBEVUIWbuevbwouevb|zowuvzee|wuzfgveugvu|kGVEuozgvOUFEG|VBRILBVEUIOR|BVUOWEBUVBRBZV|iwveeuzwvezvu|iwebvuwervb")

  private val messagePane =
      GridPane<MessageView>(
              posX = 25, posY = 0, columns = 1, rows = 1, layoutFromCenter = false, spacing = 10)
          .apply { onScroll = { posY += it.direction * 50 } }

  private val btn =
      Button(200, 200, 100, 50, "Add").apply {
        onMouseClicked = { addMessage(texts[Random.nextInt(3)]) }
      }

  init {
    addComponents(messagePane, btn)
    messagePane[0, 0] = ConnectedMessageView()
  }

  fun addMessage(msg: MessageView) {
    messagePane.grow(bottom = 1)
    messagePane[0, messagePane.rows - 1] = msg
    messagePane.posY -= msg.height + 10
  }

  private fun addMessage(msg: String) {
    val msgView = GameMessageView(
      timestamp = LocalDateTime.now().toString(),
      player = "Player 1",
      playerNameColor = Color.RED,
      messageType = "Test Type",
      text = msg.split("|"))
    messagePane.grow(bottom = 1)
    messagePane[0, messagePane.rows - 1] = msgView
    messagePane.posY -= msgView.height + 10
  }
}
