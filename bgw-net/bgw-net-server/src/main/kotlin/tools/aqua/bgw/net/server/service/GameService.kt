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

@file:Suppress("unused")

package tools.aqua.bgw.net.server.service

import kotlin.random.Random
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import tools.aqua.bgw.net.common.response.CreateGameResponseStatus
import tools.aqua.bgw.net.common.response.JoinGameResponseStatus
import tools.aqua.bgw.net.common.response.LeaveGameResponseStatus
import tools.aqua.bgw.net.server.ORPHANED_GAME_CHECK_RATE
import tools.aqua.bgw.net.server.TIME_UNTIL_ORPHANED
import tools.aqua.bgw.net.server.entity.GameInstance
import tools.aqua.bgw.net.server.entity.Player
import tools.aqua.bgw.net.server.entity.repositories.GameRepository

/** This service handles all interactions associated with [GameInstance]. */
@Service
class GameService(private val gameRepository: GameRepository) {

  /** Logger instance. */
  private val logger = LoggerFactory.getLogger(javaClass)

  /**
   * Creates a new [GameInstance].
   *
   * @param gameID ID of the associated [GameInstance].
   * @param sessionID Unique session ID. ``null`` for auto allocation.
   * @param greetingMessage Greeting message to be broadcast to all players joining this session.
   * @param initializer [Player] instance that hosts this session.
   */
  @Synchronized
  fun createGame(
      gameID: String,
      sessionID: String?,
      greetingMessage: String,
      initializer: Player,
  ): Pair<CreateGameResponseStatus, String?> {
    val sid = sessionID ?: nextSessionID()

    val status =
        if (initializer.game == null) {
          val game = GameInstance(gameID, sid, greetingMessage, initializer)
          if (gameRepository.add(game)) {
            initializer.game = game
            CreateGameResponseStatus.SUCCESS
          } else CreateGameResponseStatus.SESSION_WITH_ID_ALREADY_EXISTS
        } else CreateGameResponseStatus.ALREADY_ASSOCIATED_WITH_GAME

    return status to if (status == CreateGameResponseStatus.SUCCESS) sid else null
  }

  /**
   * Removes a [Player] from a [GameInstance].
   *
   * @param player [Player] that leaves.
   */
  @Synchronized
  fun leaveGame(player: Player): LeaveGameResponseStatus {
    val game = player.game

    return if (game == null) {
      LeaveGameResponseStatus.NO_ASSOCIATED_GAME
    } else {
      if (game.remove(player)) {
        player.game = null
        LeaveGameResponseStatus.SUCCESS
      } else LeaveGameResponseStatus.SERVER_ERROR
    }
  }

  /**
   * Adds a [Player] to a [GameInstance].
   *
   * @param player [Player] that joins.
   * @param sessionID Session to join to.
   */
  @Synchronized
  fun joinGame(player: Player, sessionID: String): JoinGameResponseStatus =
      if (player.game != null) {
        JoinGameResponseStatus.ALREADY_ASSOCIATED_WITH_GAME
      } else {
        val game = gameRepository.getBySessionID(sessionID)
        when {
          game == null -> JoinGameResponseStatus.INVALID_SESSION_ID
          game.players.any { it.name == player.name } ->
              JoinGameResponseStatus.PLAYER_NAME_ALREADY_TAKEN
          else -> {
            game.add(player)
            player.game = game
            JoinGameResponseStatus.SUCCESS
          }
        }
      }

  /**
   * Looks for orphaned Games and removes them.
   *
   * A Game is considered orphaned when [GameInstance.orphanCandidateSince]
   * - [System.currentTimeMillis] > [TIME_UNTIL_ORPHANED]
   *
   * This function is scheduled to run on a fixed rate every [ORPHANED_GAME_CHECK_RATE]
   * milliseconds.
   */
  @Synchronized
  @Scheduled(fixedRate = ORPHANED_GAME_CHECK_RATE)
  fun removeOrphanedGames() {
    var numRemoved = 0
    gameRepository.getAll().forEach { game ->
      game.orphanCandidateSince?.let {
        if (it + TIME_UNTIL_ORPHANED < System.currentTimeMillis()) {
          gameRepository.remove(game)
          numRemoved++
          logger.info("Removed game with id ${game.sessionID} because it was orphaned.")
        }
      }
    }
  }

  /**
   * Returns [GameInstance] instance associated with [sessionID].
   *
   * @param sessionID Session ID to search.
   *
   * @return [GameInstance] instance associated with [sessionID], 'null' if no game was found.
   */
  fun getBySessionID(sessionID: String): GameInstance? = gameRepository.getBySessionID(sessionID)

  /**
   * Returns all active [GameInstance] instances.
   *
   * @return All active [GameInstance] instances.
   */
  fun getAll(): List<GameInstance> = gameRepository.getAll()

  /** Generates a new random session ID. */
  private fun nextSessionID(): String {
    val reservedIDs = gameRepository.getAll().map { it.sessionID }
    var offset = Random.nextInt(100, 500)
    var sessionID = "Session$offset"

    while (reservedIDs.contains(sessionID)) {
      sessionID = "Session${offset++}"
    }

    return sessionID
  }
}
