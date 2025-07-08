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

import PropData
import SceneMapper
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import jsonMapper
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.serialization.encodeToString
import tools.aqua.bgw.application.Constants
import tools.aqua.bgw.core.BoardGameApplication

/** Room-aware WebSocket channel that handles multiple game rooms. */
internal class RoomWebSocketChannel(private val path: String) {

  fun install(application: Application) {
    application.routing {
      webSocket(path) {
        var currentRoom: Room? = null

        try {
          // Wait for room join message
          for (frame in incoming) {
            if (frame is Frame.Text) {
              val message = frame.readText()

              // Handle room join message
              if (message.startsWith("JOIN_ROOM:")) {
                val roomId = message.substringAfter("JOIN_ROOM:")
                currentRoom = RoomManager.getRoom(roomId)

                if (currentRoom != null) {
                  RoomManager.joinRoom(roomId, this@webSocket)

                  // Send initial room state
                  val appData =
                      SceneMapper.map(
                              menuScene = currentRoom.application.menuScene,
                              gameScene = currentRoom.application.boardGameScene)
                          .apply {
                            fonts =
                                currentRoom.application.loadedFonts.map { (path, fontName, weight)
                                  ->
                                  Triple(path, fontName, weight.toInt())
                                }
                          }
                  val json = jsonMapper.encodeToString(PropData(appData))
                  currentRoom.sendToSession(this@webSocket, json)
                  break
                } else {
                  send("ERROR: Room $roomId not found")
                  close(CloseReason(CloseReason.Codes.CANNOT_ACCEPT, "Room not found"))
                  return@webSocket
                }
              }
            }
          }

          // Handle regular messages once joined to a room
          for (frame in incoming) {
            if (frame is Frame.Text) {
              val message = frame.readText()
              currentRoom?.let { room -> handleRoomMessage(room, this@webSocket, message) }
            }
          }
        } catch (e: ClosedReceiveChannelException) {
          // Connection closed normally
        } catch (e: Exception) {
          e.printStackTrace()
        } finally {
          // Clean up when connection closes
          currentRoom?.let { RoomManager.leaveRoom(this@webSocket) }
        }
      }
    }
  }

  private suspend fun handleRoomMessage(room: Room, session: WebSocketSession, message: String) {
    val type = message.substringBefore('|')
    val content = message.substringAfter('|')

    when (type) {
      "bgwQuery" -> {
        try {
          handleRoomEvent(room, content)
        } catch (e: Exception) {
          e.printStackTrace()
        }
      }
      "bgwAnimationQuery" -> {
        try {
          handleRoomAnimation(room, content)
        } catch (e: Exception) {
          e.printStackTrace()
        }
      }
      "bgwGlobalQuery" -> {
        try {
          handleRoomGlobalEvent(room, content)
        } catch (e: Exception) {
          e.printStackTrace()
        }
      }
    }
  }

  private suspend fun handleRoomEvent(room: Room, content: String) {
    // Use the room's application context for event handling
    val originalApplication = Constants.FRONTEND.application
    val originalBoardGameScene = Constants.FRONTEND.boardGameScene
    val originalMenuScene = Constants.FRONTEND.menuScene

    try {
      // Temporarily set the room's application context
      Constants.FRONTEND.application = room.application
      Constants.FRONTEND.boardGameScene = room.application.boardGameScene
      Constants.FRONTEND.menuScene = room.application.menuScene

      // Handle the event with the room's context
      eventListener(content)
    } finally {
      // Restore the original context
      Constants.FRONTEND.application = originalApplication
      Constants.FRONTEND.boardGameScene = originalBoardGameScene
      Constants.FRONTEND.menuScene = originalMenuScene
    }
  }

  private suspend fun handleRoomAnimation(room: Room, content: String) {
    val originalApplication = Constants.FRONTEND.application
    val originalBoardGameScene = Constants.FRONTEND.boardGameScene
    val originalMenuScene = Constants.FRONTEND.menuScene

    try {
      Constants.FRONTEND.application = room.application
      Constants.FRONTEND.boardGameScene = room.application.boardGameScene
      Constants.FRONTEND.menuScene = room.application.menuScene

      animationListener(content)
    } finally {
      Constants.FRONTEND.application = originalApplication
      Constants.FRONTEND.boardGameScene = originalBoardGameScene
      Constants.FRONTEND.menuScene = originalMenuScene
    }
  }

  private suspend fun handleRoomGlobalEvent(room: Room, content: String) {
    val originalApplication = Constants.FRONTEND.application
    val originalBoardGameScene = Constants.FRONTEND.boardGameScene
    val originalMenuScene = Constants.FRONTEND.menuScene

    try {
      Constants.FRONTEND.application = room.application
      Constants.FRONTEND.boardGameScene = room.application.boardGameScene
      Constants.FRONTEND.menuScene = room.application.menuScene

      globalListener(content)
    } finally {
      Constants.FRONTEND.application = originalApplication
      Constants.FRONTEND.boardGameScene = originalBoardGameScene
      Constants.FRONTEND.menuScene = originalMenuScene
    }
  }
}

// Add extension properties to BoardGameApplication for room management
internal var BoardGameApplication.boardGameScene: tools.aqua.bgw.core.BoardGameScene?
  get() = Constants.FRONTEND.boardGameScene
  set(value) {
    Constants.FRONTEND.boardGameScene = value
  }

internal var BoardGameApplication.menuScene: tools.aqua.bgw.core.MenuScene?
  get() = Constants.FRONTEND.menuScene
  set(value) {
    Constants.FRONTEND.menuScene = value
  }

internal val BoardGameApplication.loadedFonts:
    List<Triple<String, String, tools.aqua.bgw.util.Font.FontWeight>>
  get() = Constants.FRONTEND.loadedFonts
