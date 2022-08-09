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

import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import tools.aqua.bgw.net.common.SERVER_ENDPOINT
import tools.aqua.bgw.net.server.entity.tables.KeyValueRepository
import tools.aqua.bgw.net.server.entity.tables.KeyValueStoreEntry

/**
 * [WebSocketConfigurer] for BWG-Net applications.
 *
 * @property wsHandler Auto-Wired [BGWWebsocketHandler].
 * @property keyValueRepository Auto-Wired [KeyValueRepository].
 */
@Configuration
@EnableWebSocket
class BGWWebSocketConfigurer(
    private val wsHandler: BGWWebsocketHandler,
    private val keyValueRepository: KeyValueRepository
) : WebSocketConfigurer {
  override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
    registry
        .addHandler(wsHandler, "/$SERVER_ENDPOINT")
        .addInterceptors(BGWHandshakeInterceptor(keyValueRepository))
  }

  /** Generates and initializes random network secret. */
  @EventListener(ApplicationReadyEvent::class)
  fun initializeNetworkSecret() {
    if (!keyValueRepository.existsById("Network secret")) {
      val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
      val length = 10
      val randomString =
          (1..length)
              .map { kotlin.random.Random.nextInt(0, charPool.size) }
              .map(charPool::get)
              .joinToString("")
      keyValueRepository.save(KeyValueStoreEntry("Network secret", randomString))
    }
  }
}
