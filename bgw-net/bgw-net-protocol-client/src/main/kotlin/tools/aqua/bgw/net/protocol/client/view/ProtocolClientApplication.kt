/*
 * Copyright 2022-2023 The BoardGameWork Authors
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

import java.awt.Color
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.net.protocol.client.service.NetworkService
import tools.aqua.bgw.net.protocol.client.view.messageviews.*
import tools.aqua.bgw.visual.ColorVisual

/** The [BoardGameApplication] class. */
class ProtocolClientApplication :
    BoardGameApplication(width = 500, height = 815, windowTitle = "BGW Protocol Client") {

  private val connectionScene: ConnectionScene = ConnectionScene()
  private val protocolScene: ProtocolScene = ProtocolScene()
  private val networkService: NetworkService = NetworkService(this)

  init {
    background = ColorVisual.WHITE

    connectionScene.buttonJoin.onMouseClicked = {
      networkService.joinGame(
          address = connectionScene.addressText.text,
          secret = connectionScene.secretText.text,
          sessionID = connectionScene.sessionIDText.text,
      )
    }

    onWindowClosed = { networkService.close() }

    showGameScene(connectionScene)
    show()
  }

  /** Adds a [ConnectedMessageView]. */
  fun onConnectionEstablished() {
    runOnGUIThread {
      showGameScene(protocolScene)
      protocolScene.addMessage(ConnectedMessageView())
    }
  }

  /** Adds a [GameHostedMessageView]. */
  fun onGameCreated(sessionID: String) {
    runOnGUIThread {
      protocolScene.addMessage(GameHostedMessageView(sessionID))
      protocolScene.addMessage(BeginMessageView())
    }
  }

  /** Adds a [GameJoinedMessageView]. */
  fun onGameJoined(sessionID: String) {
    runOnGUIThread {
      protocolScene.addMessage(GameJoinedMessageView(sessionID))
      protocolScene.addMessage(BeginMessageView())
    }
  }

  /** Adds a [PlayerJoinedMessageView]. */
  fun onPlayerJoined(player: String, isSpectator: Boolean) {
    runOnGUIThread { protocolScene.addMessage(PlayerJoinedMessageView(player, isSpectator)) }
  }

  /** Adds a [PlayerLeftMessageView]. */
  fun onPlayerLeft(player: String) {
    runOnGUIThread { protocolScene.addMessage(PlayerLeftMessageView(player)) }
  }

  /** Adds a [GameMessageView]. */
  fun onGameActionReceived(
      timestamp: String,
      player: String,
      playerNameColor: Color,
      messageType: String,
      message: List<String>
  ) {
    runOnGUIThread {
      protocolScene.addMessage(
          GameMessageView(
              timestamp = timestamp,
              player = player,
              playerNameColor = playerNameColor,
              messageType = messageType,
              text = message))
    }
  }
}
