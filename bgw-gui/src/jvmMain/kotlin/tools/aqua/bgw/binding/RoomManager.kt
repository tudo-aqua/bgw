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

import io.ktor.websocket.*
import java.util.concurrent.ConcurrentHashMap
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import tools.aqua.bgw.core.BoardGameApplication

/**
 * Manages multiple rooms for lobby-based web games. Each room has its own BoardGameApplication
 * instance and isolated state.
 */
internal object RoomManager {
  private val rooms = ConcurrentHashMap<String, Room>()
  private val sessionToRoom = ConcurrentHashMap<WebSocketSession, String>()
  private val roomMutex = Mutex()

  /** Creates a new room with the given application. */
  suspend fun createRoom(roomId: String, application: BoardGameApplication): Room {
    roomMutex.withLock {
      if (rooms.containsKey(roomId)) {
        throw IllegalArgumentException("Room $roomId already exists")
      }

      val room = Room(roomId, application)
      rooms[roomId] = room
      return room
    }
  }

  /** Joins a session to a specific room. */
  suspend fun joinRoom(roomId: String, session: WebSocketSession): Room? {
    roomMutex.withLock {
      val room = rooms[roomId] ?: return null
      room.addSession(session)
      sessionToRoom[session] = roomId
      return room
    }
  }

  /** Removes a session from its current room. */
  suspend fun leaveRoom(session: WebSocketSession) {
    roomMutex.withLock {
      val roomId = sessionToRoom.remove(session) ?: return
      val room = rooms[roomId] ?: return
      room.removeSession(session)

      // Remove room if it becomes empty
      if (room.isEmpty()) {
        rooms.remove(roomId)
      }
    }
  }

  /** Gets the room for a specific session. */
  fun getRoomForSession(session: WebSocketSession): Room? {
    val roomId = sessionToRoom[session] ?: return null
    return rooms[roomId]
  }

  /** Gets a room by ID. */
  fun getRoom(roomId: String): Room? {
    return rooms[roomId]
  }

  /** Gets all active rooms. */
  fun getAllRooms(): Map<String, Room> {
    return rooms.toMap()
  }

  /** Removes a room completely. */
  suspend fun removeRoom(roomId: String) {
    roomMutex.withLock {
      val room = rooms.remove(roomId) ?: return
      // Remove all sessions from this room
      sessionToRoom.entries.removeAll { it.value == roomId }
      room.close()
    }
  }
}

/** Represents a game room with its own BoardGameApplication and WebSocket sessions. */
internal class Room(val id: String, val application: BoardGameApplication) {
  private val sessions = ConcurrentHashMap<WebSocketSession, Boolean>()
  private val sessionMutex = Mutex()

  suspend fun addSession(session: WebSocketSession) {
    sessionMutex.withLock { sessions[session] = true }
  }

  suspend fun removeSession(session: WebSocketSession) {
    sessionMutex.withLock { sessions.remove(session) }
  }

  fun isEmpty(): Boolean = sessions.isEmpty()

  fun getSessionCount(): Int = sessions.size

  /** Sends a message to all sessions in this room. */
  suspend fun broadcast(message: String) {
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

  /** Sends a message to a specific session in this room. */
  suspend fun sendToSession(session: WebSocketSession, message: String) {
    if (sessions.containsKey(session)) {
      try {
        session.send(message)
      } catch (e: Exception) {
        // Session might be closed, remove it
        sessions.remove(session)
      }
    }
  }

  /** Closes the room and all its sessions. */
  suspend fun close() {
    val activeSessions = sessions.keys.toList()
    activeSessions.forEach { session ->
      try {
        session.close()
      } catch (e: Exception) {
        // Ignore errors when closing
      }
    }
    sessions.clear()
  }
}
