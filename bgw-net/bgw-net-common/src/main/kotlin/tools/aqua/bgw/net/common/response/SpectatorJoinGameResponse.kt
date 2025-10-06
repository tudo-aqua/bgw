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

package tools.aqua.bgw.net.common.response

/**
 * Response upon [tools.aqua.bgw.net.common.request.SpectatorJoinGameMessage].
 *
 * @property status Status code.
 * @property sessionID Session ID for this game. ``null`` if joining was not successful.
 * @property players [List] of all non-spectator players currently in this game. Sorted ascending by
 *   connection time i.e. index 0 is the game's host.
 * @property message The Welcome message from the host.
 */
class SpectatorJoinGameResponse(
    val status: JoinGameResponseStatus,
    val sessionID: String?,
    val players: List<String>,
    val message: String
) : Response()
