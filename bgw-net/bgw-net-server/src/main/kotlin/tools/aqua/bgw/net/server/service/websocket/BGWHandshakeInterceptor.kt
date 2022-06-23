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

package tools.aqua.bgw.net.server.service.websocket

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.HandshakeInterceptor
import tools.aqua.bgw.net.server.entity.tables.KeyValueRepository

/**
 * Checks if the network secret HTTP header is matching the current network secret. Checks if the
 * player name HTTP header is set and assigns it as the playerName attribute to the session.
 *
 * Only allows establishment of web socket session if both checks succeed.
 */
class BGWHandshakeInterceptor(private val keyValueRepository: KeyValueRepository) :
    HandshakeInterceptor {
  private val logger: Logger = LoggerFactory.getLogger(javaClass)

  override fun beforeHandshake(
      request: ServerHttpRequest,
      response: ServerHttpResponse,
      wsHandler: WebSocketHandler,
      attributes: MutableMap<String, Any>
  ): Boolean {
    val secret: String =
        try {
              keyValueRepository.findById("Network secret").get()
            } catch (e: NoSuchElementException) {
              logger.error(
                  "Network secret does not exist in database. Found ${
                keyValueRepository.findAll().joinToString(separator = ", ") { it.key }
              }.")
              response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
              return false
            }
            .value

    val headerSecret = request.headers.getFirst("NetworkSecret")
    val headerName = request.headers.getFirst("PlayerName")

    if (headerSecret == null || headerName == null) {
      response.setStatusCode(HttpStatus.BAD_REQUEST)
      return false
    }

    if (headerSecret != secret) {
      response.setStatusCode(HttpStatus.UNAUTHORIZED)
      return false
    }

    attributes["playerName"] = headerName

    return true
  }

  /** Does nothing. */
  override fun afterHandshake(
      request: ServerHttpRequest,
      response: ServerHttpResponse,
      wsHandler: WebSocketHandler,
      exception: Exception?
  ) {}
}
