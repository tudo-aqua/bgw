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

import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.TextField
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.visual.ColorVisual

/**
 * The connection startup screen.
 *
 * @param serverAddress The address of the server to connect to.
 * @param secret The secret to use for the connection.
 * @param gameID The ID of the game to join or host.
 * @param sessionID The session ID to use for the connection.
 */
class ConnectionScene(
    serverAddress: String = "",
    secret: String = "",
    gameID: String = "",
    sessionID: String = ""
) : BoardGameScene(height = 800, width = 500, background = ColorVisual.WHITE) {

  /** [TextField] for the hostname or address and port. */
  val addressText: TextField =
      TextField(
          posX = 50,
          posY = 50,
          height = 40,
          width = width - 100,
          text = serverAddress,
          prompt = "Server address")

  /** [TextField] for the network secret. */
  val secretText: TextField =
      TextField(
          posX = 50, posY = 100, height = 40, width = width - 100, text = secret, prompt = "Secret")

  /** [TextField] for the network secret. */
  val gameIDText: TextField =
      TextField(
          posX = 50,
          posY = 150,
          height = 40,
          width = width - 100,
          text = gameID,
          prompt = "Game ID (hosting only)")

  /** [TextField] for the session id. */
  val sessionIDText: TextField =
      TextField(
          posX = 50,
          posY = 200,
          height = 40,
          width = width - 100,
          prompt = "sessionID",
          text = sessionID)

  /** [Button] for joining a game. */
  val buttonJoin: Button =
      Button(
          posX = 50,
          posY = 250,
          height = 40,
          width = width - 100,
          text = "Join",
          visual = ColorVisual.LIGHT_GRAY)

  init {
    addComponents(addressText, secretText, gameIDText, sessionIDText, buttonJoin)
  }
}
