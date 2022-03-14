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

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean
import tools.aqua.bgw.net.server.entity.tables.KeyValueRepository

@Configuration
@EnableWebSocket
class WebSocketServerConfiguration(
  private val wsHandler: MyWebsocketHandler,
  private val keyValueRepository: KeyValueRepository
) : WebSocketConfigurer {

  /** Not really needed anymore. An idle timeout could be configured here. */
  @Bean
  fun createWebSocketContainer(): ServletServerContainerFactoryBean =
      ServletServerContainerFactoryBean().apply {
        // setMaxSessionIdleTimeout(IDLE_TIMEOUT)
      }

  override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
    registry
        .addHandler(wsHandler, "/chat")
        .addInterceptors(MyHandshakeInterceptor(keyValueRepository))
  }
}

