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
import tools.aqua.bgw.net.server.entity.Game
import tools.aqua.bgw.net.server.entity.Player
import tools.aqua.bgw.net.server.entity.repositories.GameRepository
import tools.aqua.bgw.net.server.entity.repositories.PlayerRepository

/**
 * This service exposes all active games and players to the frontend. //TODO maybe make Games and
 * Players read only for frontend?
 */
@Service
class FrontendService(
    private val playerRepository: PlayerRepository,
    private val gameRepository: GameRepository
) {
  val activePlayers: List<Player>
    get() = playerRepository.getAll()

  val activeGames: List<Game>
    get() = gameRepository.getAll()
}
