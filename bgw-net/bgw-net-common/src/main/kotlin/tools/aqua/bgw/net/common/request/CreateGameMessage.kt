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

package tools.aqua.bgw.net.common.request

/**
 * Message indicating creation of a new game session with given [gameID] and [sessionID].
 *
 * @property gameID ID of the current game to be used.
 * @property sessionID Unique id for the new session to be created on the server.
 * @property greetingMessage Greeting message to be broadcast to all players joining this session.
 */
data class CreateGameMessage(val gameID: String, val sessionID: String, val greetingMessage: String) : Request()
