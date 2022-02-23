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

package tools.aqua.bgw.net.server.entity

class Game(val gameID: String, val sessionID: String, initializer: Player) {
  private val mutablePlayers: MutableList<Player> = mutableListOf(initializer)

  /**
   * This is set to [System.currentTimeMillis] whenever the last [Player] leaves this [Game]. It is
   * set to null whenever a [Player] joins this [Game].
   */
  var orphanCandidateSince: Long? = null
    private set

  val players: List<Player>
    get() = mutablePlayers.toList()

  fun remove(player: Player): Boolean {
    val result = mutablePlayers.remove(player)
    if (mutablePlayers.isEmpty()) {
      orphanCandidateSince = System.currentTimeMillis()
    }
    return result
  }

  fun add(player: Player): Boolean {
    val result = mutablePlayers.add(player)
    if (mutablePlayers.isNotEmpty()) {
      orphanCandidateSince = null
    }
    return result
  }

  override fun equals(other: Any?): Boolean =
      if (other is Game) sessionID == other.sessionID else false

  override fun hashCode(): Int {
    return sessionID.hashCode()
  }
}
