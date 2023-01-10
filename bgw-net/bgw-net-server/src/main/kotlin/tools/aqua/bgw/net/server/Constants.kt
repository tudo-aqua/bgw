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

package tools.aqua.bgw.net.server

import org.springframework.web.socket.WebSocketSession
import tools.aqua.bgw.net.server.entity.GameInstance
import tools.aqua.bgw.net.server.entity.Player
import tools.aqua.bgw.net.server.service.GameService

/**
 * The interval between consecutive checks for orphaned games in milliseconds.
 * @see [GameService.removeOrphanedGames]
 */
const val ORPHANED_GAME_CHECK_RATE: Long = 20_000L

/**
 * The duration a game has to be without any players until it is considered orphaned.
 * @see [GameService.removeOrphanedGames]
 * @see [GameInstance]
 */
const val TIME_UNTIL_ORPHANED: Long = 60_000L

/** OAuth Logout Success URL. */
internal const val LOGOUT_SUCCESS_URL = "/"

/** The String representation of the MauMau game id. */
const val MAUMAU_GAME_ID: String = "MauMau"

/**
 * The String representation of the URL of the example_schema.json relative to projects resources.
 */
const val EXAMPLE_SCHEMA_JSON_URL_STRING: String = "/example_schema.json"

/** The String representation of the URL of the meta_schema.json relative to projects resources. */
const val META_SCHEMA_JSON_URL_STRING: String = "/meta_schema.json"

/** The String representation of the URL of the meta_schema.json relative to projects resources. */
const val BGW_META_SCHEMA_JSON_URL_STRING: String = "/bgw_meta_schema.json"

/** Player instance. */
val WebSocketSession.player: Player
    get() {
        val player = attributes["player"] ?: error("missing attribute")
        if (player is Player) return player else error("wrong type")
    }

/** Websocket buffer size in bytes **/
const val BUFFER_SIZE = 1024 * 1024 // 1MB
