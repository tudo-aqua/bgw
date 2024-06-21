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

package tools.aqua.bgw.net.server.entity

/**
 * Class representing a game session holding IDs and currently connected players.
 *
 * @property gameID The game ID this instance is associated to.
 * @property sessionID Unique ID identifying this session.
 * @property greetingMessage Greeting message to be broadcast to all players joining this session.
 * @param initializer Instance of the host player.
 */
class GameInstance(
    val gameID: String,
    val sessionID: String,
    val greetingMessage: String,
    initializer: Player
) {

  /** All players currently in this game instance. */
  private val mutablePlayers: MutableList<Player> = mutableListOf(initializer)

  /** public accessor for connected players as immutable player list. */
  val players: List<Player>
    get() = mutablePlayers.toList()

  /**
   * This is set to [System.currentTimeMillis] whenever the last [Player] leaves this [GameInstance]
   * . It is set to 'null' whenever a [Player] joins this [GameInstance].
   */
  var orphanCandidateSince: Long? = null
    private set

  /**
   * Removes given player from this game and updates orphaned status.
   *
   * @param player Player to be removed from this game
   *
   * @return 'true' iff the [player] was in the players list and was successfully removed.
   */
  fun remove(player: Player): Boolean =
      mutablePlayers.remove(player).also { updateOrphanedStatus() }

  /**
   * Adds given player to this game and updates orphaned status.
   *
   * @param player Player to be added to this game
   *
   * @return 'true' iff the [player] was successfully added.
   */
  fun add(player: Player): Boolean = mutablePlayers.add(player).also { updateOrphanedStatus() }

  /**
   * Sets [orphanCandidateSince] to [System.currentTimeMillis] if [orphanCandidateSince] is
   * currently 'null' and the [mutablePlayers] list is empty.
   */
  private fun updateOrphanedStatus() {
    orphanCandidateSince =
        if (orphanCandidateSince == null && mutablePlayers.isEmpty()) System.currentTimeMillis()
        else null
  }

  /** Checks for equal sessionID as it must be unique. */
  override fun equals(other: Any?): Boolean =
      if (other is GameInstance) sessionID == other.sessionID else false

  /** Hashes sessionID as it must be unique. */
  override fun hashCode(): Int = sessionID.hashCode()
}
