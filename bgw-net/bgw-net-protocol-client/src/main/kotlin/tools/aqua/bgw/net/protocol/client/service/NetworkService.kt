/*
 * Copyright 2022-2024 The BoardGameWork Authors
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

package tools.aqua.bgw.net.protocol.client.service

import tools.aqua.bgw.core.Color
import java.awt.Font
import java.awt.font.FontRenderContext
import java.awt.geom.AffineTransform
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import tools.aqua.bgw.net.common.GameAction
import tools.aqua.bgw.net.protocol.client.view.ProtocolClientApplication
import tools.aqua.bgw.net.protocol.client.view.messageviews.GameMessageView

/** Network service. */
class NetworkService(private val view: ProtocolClientApplication) {

  private var client: ProtocolBoardGameClient? = null
  private val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss.SS")
  private val playerColors: MutableList<Color> =
      mutableListOf(
          Color(100, 0, 0),
          Color(0, 0, 100),
          Color(0, 100, 0),
          Color(130, 130, 0),
      )
  private val colorMap: MutableMap<String, Color> = mutableMapOf()

  // region Connection
  /**
   * Connects to server and joins a game session.
   *
   * @param address Server address and port.
   * @param secret Server secret.
   * @param sessionID Session ID to join to.
   */
  fun joinGame(address: String, secret: String, sessionID: String) {
    if (!connect(address, secret)) return

    client?.spectateGame(sessionID, "")
  }

  /**
   * Connects to server.
   *
   * @param address Server address and port in format "127.0.0.1:8080"
   * @param secret Network secret.
   */
  private fun connect(address: String, secret: String): Boolean {
    if (address.isEmpty() || secret.isEmpty()) return false

    val newClient =
        ProtocolBoardGameClient(host = address, secret = secret, view = view, service = this)

    return if (newClient.connect()) {
      this.client = newClient
      true
    } else {
      false
    }
  }

  /** Disconnects from the server and closes web socket client. */
  fun close() {
    if (client?.isOpen == true) client?.disconnect()
  }

  /** Formats current timestamp using [timeFormatter]. */
  fun getTimestamp(): String = LocalTime.now().format(timeFormatter)

  /** Returns color for [player]. */
  fun getPlayerColor(player: String): Color =
      colorMap.getOrPut(player) { playerColors.removeFirst().also { playerColors.add(it) } }

  /** Returns simple name of [GameAction] instance. */
  fun parseMessageType(message: GameAction): String = message.javaClass.simpleName

  /** Returns string representation of [message]. */
  fun parseMessage(message: GameAction): List<String> {
    val lines = message.toString().split("\n")
    val result = mutableListOf<String>()

    val font =
        Font(GameMessageView.textFont.family, Font.PLAIN, GameMessageView.textFont.size.toInt())
    val frc = FontRenderContext(AffineTransform(), true, true)

    for (line in lines) {
      var tmpLine = ""
      for (c in line.toCharArray()) {
        if (font.getStringBounds(tmpLine + c, frc).width <= 400) {
          tmpLine += c
        } else {
          result.add(tmpLine)
          tmpLine = "     $c"
        }
      }
      result.add(tmpLine)
    }

    return result
  }
  // endregion
}
