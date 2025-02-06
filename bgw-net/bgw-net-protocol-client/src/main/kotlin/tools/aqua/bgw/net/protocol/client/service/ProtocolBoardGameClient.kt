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

package tools.aqua.bgw.net.protocol.client.service

import tools.aqua.bgw.net.client.BoardGameClient
import tools.aqua.bgw.net.client.NetworkLogging
import tools.aqua.bgw.net.common.GameAction
import tools.aqua.bgw.net.common.notification.PlayerJoinedNotification
import tools.aqua.bgw.net.common.notification.PlayerLeftNotification
import tools.aqua.bgw.net.common.notification.SpectatorJoinedNotification
import tools.aqua.bgw.net.common.response.*
import tools.aqua.bgw.net.protocol.client.view.ProtocolClientApplication

/**
 * [BoardGameClient] implementation for network communication.
 *
 * @param host Host address.
 * @param secret Network secret.
 * @property view The [ProtocolClientApplication].
 * @property service The [NetworkService].
 */
class ProtocolBoardGameClient(
    host: String,
    secret: String,
    val view: ProtocolClientApplication,
    val service: NetworkService
) :
    BoardGameClient(
        playerName = "Spectator${System.currentTimeMillis()}",
        host = host,
        secret = secret,
        networkLoggingBehavior = NetworkLogging.VERBOSE) {

  override fun onOpen() {
    view.onConnectionEstablished()
  }

  override fun onCreateGameResponse(response: CreateGameResponse) {
    if (response.status == CreateGameResponseStatus.SUCCESS)
        view.onGameCreated(requireNotNull(response.sessionID))
  }

  override fun onSpectatorJoinGameResponse(response: SpectatorJoinGameResponse) {
    if (response.status == JoinGameResponseStatus.SUCCESS)
        view.onGameJoined(requireNotNull(response.sessionID))
  }

  override fun onPlayerJoined(notification: PlayerJoinedNotification) {
    view.onPlayerJoined(notification.sender, false)
  }

  override fun onSpectatorJoined(notification: SpectatorJoinedNotification) {
    view.onPlayerJoined(notification.sender, true)
  }

  override fun onPlayerLeft(notification: PlayerLeftNotification) {
    view.onPlayerLeft(notification.sender)
  }

  override fun onGameActionReceived(message: GameAction, sender: String) {
    view.onGameActionReceived(
        timestamp = service.getTimestamp(),
        player = sender,
        playerNameColor = service.getPlayerColor(player = sender),
        messageType = service.parseMessageType(message = message),
        message = service.parseMessage(message = message))
  }
}
