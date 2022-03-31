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

package tools.aqua.bgw.net.server.service

import org.springframework.stereotype.Service
import org.springframework.web.socket.WebSocketSession
import tools.aqua.bgw.net.server.entity.Player
import tools.aqua.bgw.net.server.entity.repositories.PlayerRepository

/**
 * This service handles creation and deletion of players from [PlayerRepository].
 *
 * @property playerRepository Auto-Wired repository for players.
 */
@Service
class PlayerService(val playerRepository: PlayerRepository) {
  /**
   * Creates a new [Player] with given [session].
   *
   * @param session The new player's [WebSocketSession].
   */
  fun createPlayer(session: WebSocketSession) {
    val playerName =
        session.attributes["playerName"] ?: error("playerName attribute missing") // TODO
    with(Player(playerName as String, null, session)) {
      playerRepository.add(this)
      session.attributes["player"] = this
    }
  }

  /**
   * Deletes a [Player] with given [session].
   *
   * @param session The player's [WebSocketSession].
   */
  fun deletePlayer(session: WebSocketSession) {
    val player = session.attributes["player"] ?: error("player attribute missing") // TODO
    playerRepository.remove(player as Player)
  }

  /**
   * Returns all connected players.
   *
   * @return [List] of all connected players.
   */
  fun getAll(): List<Player> = playerRepository.getAll()
}
