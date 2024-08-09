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

@file:Suppress("UseDataClass")

package tools.aqua.bgw.net.server.service

import org.springframework.stereotype.Service
import tools.aqua.bgw.net.server.entity.GameInstance
import tools.aqua.bgw.net.server.entity.Player
import tools.aqua.bgw.net.server.entity.repositories.GameRepository
import tools.aqua.bgw.net.server.entity.repositories.PlayerRepository
import tools.aqua.bgw.net.server.entity.tables.SchemasByGame
import tools.aqua.bgw.net.server.entity.tables.SchemasByGameRepository

/** This service exposes all active games and players to the frontend. */
@Service
class FrontendService(
    private val playerRepository: PlayerRepository,
    private val gameRepository: GameRepository,
    private val schemasByGameRepository: SchemasByGameRepository
) {
  /** List of active [Player]s. */
  val activePlayers: List<Player>
    get() = playerRepository.getAll()

  /** List of active [GameInstance]s. */
  val activeGames: List<GameInstance>
    get() = gameRepository.getAll()

  /** List of all schemas grouped by their gameID. */
  val allSchemas: List<SchemasByGame>
    get() = schemasByGameRepository.findAll().toList()

  /** List of all gameIDs of games that have been registered. */
  val allGameIds: List<String>
    get() = allSchemas.map { it.gameID }.distinct()
}
