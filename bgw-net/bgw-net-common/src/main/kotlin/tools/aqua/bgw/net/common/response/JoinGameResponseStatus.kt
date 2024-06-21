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

package tools.aqua.bgw.net.common.response

/** Status codes for join game responses. */
enum class JoinGameResponseStatus {
  /** Joined the game successfully. */
  SUCCESS,

  /**
   * This connection is already associated with a game on the server and can not join another game
   * at this time. Disconnect from the current game to connect to another game.
   */
  ALREADY_ASSOCIATED_WITH_GAME,

  /** No game with the specified id was found on the server. */
  INVALID_SESSION_ID,

  /** A player with the same player name is already part of the game. */
  PLAYER_NAME_ALREADY_TAKEN,

  /** Something on the server went wrong. */
  SERVER_ERROR
}
