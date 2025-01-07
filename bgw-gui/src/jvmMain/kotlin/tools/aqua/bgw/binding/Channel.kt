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
import java.util.concurrent.CopyOnWriteArrayList
import kotlinx.coroutines.channels.ClosedSendChannelException

internal class Channel(val path: String = "/ws") {
  val activeSessions = CopyOnWriteArrayList<WebSocketSession>()
  var onClientConnected: suspend (WebSocketSession) -> Unit = {}
  var onClientMessage: (session: WebSocketSession, text: String) -> Unit = { _, _ -> }
  val onClientError: (session: WebSocketSession, e: ClosedSendChannelException) -> Unit = { _, _ ->
  }

  suspend fun sendToAllClients(message: String) {
    // println("Sending message to all clients: $message")
    for (session in activeSessions) {
      try {
        session.send(message)
      } catch (e: ClosedSendChannelException) {
        println("Client $session disconnected: ${e.message}")
      }
    }
  }

  fun install(application: Application) {
    with(application) {
      routing {
        webSocket(path) {
          activeSessions.add(this)
          onClientConnected(this)
          try {
            for (frame in incoming) {
              if (frame is Frame.Text) {
                val text = frame.readText()
                onClientMessage(this, text)
              }
            }
          } catch (e: ClosedSendChannelException) {
            onClientError(this, e)
          } finally {
            activeSessions.remove(this)
          }
        }
      }
    }
  }
}
