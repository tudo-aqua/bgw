/*
 * Copyright 2025 The BoardGameWork Authors
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

package tools.aqua.bgw.binding

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.util.concurrent.ConcurrentHashMap
import kotlinx.coroutines.channels.ClosedReceiveChannelException

/**
 * Legacy WebSocket channel implementation. This is kept for backward compatibility but should be
 * replaced with room-aware channels.
 */
@Deprecated("Use RoomWebSocketChannel instead")
internal class Channel(private val path: String) {
  private val sessions = ConcurrentHashMap<WebSocketSession, Boolean>()

  var onClientConnected: (suspend (WebSocketSession) -> Unit)? = null
  var onClientMessage: (suspend (WebSocketSession, String) -> Unit)? = null
  var onClientDisconnected: (suspend (WebSocketSession) -> Unit)? = null

  fun install(application: Application) {
    application.routing {
      webSocket(path) {
        sessions[this] = true

        try {
          // Notify client connected
          onClientConnected?.invoke(this)

          // Handle incoming messages
          for (frame in incoming) {
            if (frame is Frame.Text) {
              val message = frame.readText()
              onClientMessage?.invoke(this, message)
            }
          }
        } catch (e: ClosedReceiveChannelException) {
          // Connection closed normally
        } catch (e: Exception) {
          e.printStackTrace()
        } finally {
          // Clean up when connection closes
          sessions.remove(this)
          onClientDisconnected?.invoke(this)
        }
      }
    }
  }

  suspend fun sendToAllClients(message: String) {
    val activeSessions = sessions.keys.toList()
    activeSessions.forEach { session ->
      try {
        session.send(message)
      } catch (e: Exception) {
        // Session might be closed, remove it
        sessions.remove(session)
      }
    }
  }

  suspend fun sendToClient(session: WebSocketSession, message: String) {
    if (sessions.containsKey(session)) {
      try {
        session.send(message)
      } catch (e: Exception) {
        // Session might be closed, remove it
        sessions.remove(session)
      }
    }
  }

  fun getActiveSessionCount(): Int = sessions.size
}
