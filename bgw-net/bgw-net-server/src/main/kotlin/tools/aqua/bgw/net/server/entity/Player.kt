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

import org.springframework.web.socket.WebSocketSession

/**
 * Class representing a connected player.
 *
 * @property name The player's name.
 * @property game Associated [GameInstance].
 * @property session Associated [WebSocketSession].
 * @property isSpectator Whether this player is spectator only. Default: `false`.
 */
class Player(
    val name: String,
    var game: GameInstance?,
    val session: WebSocketSession,
    var isSpectator: Boolean = false
) {

  /** Compares session as it must be unique. */
  override fun equals(other: Any?): Boolean =
      if (other is Player) session == other.session else false

  /** Hashes session as it must be unique. */
  override fun hashCode(): Int = session.hashCode()
}
