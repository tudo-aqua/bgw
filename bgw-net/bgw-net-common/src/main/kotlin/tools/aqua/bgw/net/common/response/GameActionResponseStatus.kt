/*
 * Copyright 2022-2024 The BoardGameWork Authors
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

/** Status codes for game messages. */
enum class GameActionResponseStatus {
  /** The message was valid and broadcast to all the other connected players. */
  SUCCESS,

  /** This connection was not associated with a game. */
  NO_ASSOCIATED_GAME,

  /** A message was sent but connection was established in spectator only mode. */
  SPECTATOR_ONLY,

  /** The payload did not match the specified schema. Message was rejected. */
  INVALID_JSON,

  /** Something went wrong on the server. */
  SERVER_ERROR
}
