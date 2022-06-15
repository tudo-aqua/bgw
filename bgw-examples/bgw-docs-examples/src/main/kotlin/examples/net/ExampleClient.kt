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

package examples.net

import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.ListView
import tools.aqua.bgw.components.uicomponents.TextField
import tools.aqua.bgw.components.uicomponents.UIComponent
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.net.client.BoardGameClient
import tools.aqua.bgw.net.common.*
import tools.aqua.bgw.visual.ColorVisual

fun main() {
  ExampleClient().apply {
    show()
    onWindowClosed = { exit() }
  }
}

class ExampleClient : BoardGameApplication() {
  private val menuScene = ClientMenuScene()

  init {
    showMenuScene(menuScene)
  }
}

data class GameAction(val string: String, val int: Int)

data class InitGameAction(val invalidString: String, val invalidInt: Int)

class ClientMenuScene : MenuScene(background = ColorVisual.WHITE) {
  lateinit var client: BoardGameClient<InitGameAction, GameAction, Any>

  inner class CreateGamePane : Pane<UIComponent>(posX = 50, posY = 200, height = 50, width = 400) {
    private val gameIdField =
        TextField().apply {
          resize(100, 50)
          reposition(0, 0)
          prompt = "GameID"
        }

    private val sessionIDField =
        TextField().apply {
          resize(100, 50)
          reposition(150, 0)
          prompt = "SessionID"
        }

    private val createGameButton =
        Button().apply {
          resize(100, 50)
          reposition(300, 0)
          visual = ColorVisual.LIGHT_GRAY
          text = "CREATE"
          onMouseClicked = { client.createGame(gameIdField.text, sessionIDField.text) }
        }

    init {
      this.addAll(gameIdField, sessionIDField, createGameButton)
    }
  }

  inner class JoinGamePane : Pane<UIComponent>(posX = 50, posY = 400, height = 50, width = 400) {
    private val greetingField =
        TextField().apply {
          resize(100, 50)
          reposition(0, 0)
          prompt = "greeting"
        }

    private val sessionIDField =
        TextField().apply {
          resize(100, 50)
          reposition(150, 0)
          prompt = "SessionID"
        }

    private val joinGameButton =
        Button().apply {
          resize(100, 50)
          reposition(300, 0)
          visual = ColorVisual.LIGHT_GRAY
          text = "JOIN"
          onMouseClicked = { client.joinGame(sessionIDField.text, greetingField.text) }
        }

    init {
      this.addAll(greetingField, sessionIDField, joinGameButton)
    }
  }

  inner class SendActionPane : Pane<UIComponent>(posX = 50, posY = 600, height = 50, width = 400) {
    private val stringField =
        TextField().apply {
          resize(100, 50)
          reposition(0, 0)
          prompt = "string"
        }

    private val intField =
        TextField().apply {
          resize(100, 50)
          reposition(150, 0)
          prompt = "int"
        }

    private val sendButton =
        Button().apply {
          resize(100, 50)
          reposition(300, 0)
          visual = ColorVisual.LIGHT_GRAY
          text = "SEND VALID"
          onMouseClicked =
              {
                val action = GameAction(stringField.text, intField.text.toInt())
                client.sendGameActionMessage(action)
              }
        }

    init {
      this.addAll(stringField, intField, sendButton)
    }
  }

  inner class SendInvalidActionPane :
      Pane<UIComponent>(posX = 50, posY = 800, height = 50, width = 400) {
    private val stringField =
        TextField().apply {
          resize(100, 50)
          reposition(0, 0)
          prompt = "string"
        }

    private val intField =
        TextField().apply {
          resize(100, 50)
          reposition(150, 0)
          prompt = "int"
        }

    private val sendButton =
        Button().apply {
          resize(100, 50)
          reposition(300, 0)
          visual = ColorVisual.LIGHT_GRAY
          text = "SEND INVALID"
          onMouseClicked =
              {
                val initGameAction = InitGameAction(stringField.text, intField.text.toInt())
                client.sendInitializeGameMessage(initGameAction)
              }
        }

    init {
      this.addAll(stringField, intField, sendButton)
    }
  }

  private val leaveGameButton =
      Button().apply {
        resize(100, 50)
        reposition(350, 1000)
        visual = ColorVisual.LIGHT_GRAY
        text = "LEAVE"
        onMouseClicked = { client.leaveGame("goodbye my friends") }
      }

  private val playerNameField =
      TextField().apply {
        resize(100, 50)
        reposition(50, 100)
        prompt = "Player Name"
      }

  private val secretField =
      TextField().apply {
        resize(100, 50)
        reposition(200, 100)
        text = "geheim"
        prompt = "Secret"
      }

  private val connectButton =
      Button().apply {
        resize(100, 50)
        reposition(350, 100)
        visual = ColorVisual.LIGHT_GRAY
        text = "Connect"
      }

  private val disconnectButton =
      Button().apply {
        resize(100, 50)
        reposition(200, 100)
        visual = ColorVisual.LIGHT_GRAY
        text = "Disconnect"
        onMouseClicked =
            {
              client.disconnect()
              log.items.add("Disconnected")
              removeComponents(this)
              addComponents(playerNameField, connectButton)
            }
      }

  private val log =
      ListView<String>().apply {
        resize(900, 900)
        reposition(650, 100)
      }

  private val createGame = CreateGamePane()

  private val joinGame = JoinGamePane()

  private val sendAction = SendActionPane()

  private val sendInvalidAction = SendInvalidActionPane()

  init {
    disconnectButton.onMouseClicked =
        {
          client.disconnect()
          log.items.add("Disconnected")
          removeComponents(
              disconnectButton,
              createGame,
              joinGame,
              sendAction,
              sendInvalidAction,
              leaveGameButton)
          addComponents(playerNameField, secretField, connectButton)
        }
    connectButton.onMouseClicked =
        {
          client = ExampleBoardGameClient()
          client.connect()
          removeComponents(playerNameField, secretField, connectButton)
          addComponents(
              disconnectButton,
              createGame,
              joinGame,
              sendAction,
              sendInvalidAction,
              leaveGameButton)
        }
    addComponents(playerNameField, secretField, connectButton, log)
    opacity = 1.0
  }

  inner class ExampleBoardGameClient :
      BoardGameClient<InitGameAction, GameAction, Any>(
          playerName = playerNameField.text,
          secret = secretField.text,
          initGameClass = InitGameAction::class.java,
          gameActionClass = GameAction::class.java,
          endGameClass = Any::class.java,
          host = "127.0.0.1",
          port = 8080) {

    override fun onOpen() {
      BoardGameApplication.runOnGUIThread { log.items.add("Connection is now open") }
    }
    override fun onClose(code: Int, reason: String, remote: Boolean) {
      BoardGameApplication.runOnGUIThread {
        log.items.add("Connection closed with code: $code and reason: $reason")
      }
    }
    override fun onCreateGameResponse(response: CreateGameResponse) {
      BoardGameApplication.runOnGUIThread { log.items.add("$response") }
    }
    override fun onJoinGameResponse(response: JoinGameResponse) {
      BoardGameApplication.runOnGUIThread { log.items.add("$response") }
    }
    override fun onLeaveGameResponse(response: LeaveGameResponse) {
      BoardGameApplication.runOnGUIThread { log.items.add("$response") }
    }
    override fun onUserJoined(notification: UserJoinedNotification) {
      BoardGameApplication.runOnGUIThread { log.items.add("$notification") }
    }
    override fun onUserLeft(notification: UserDisconnectedNotification) {
      BoardGameApplication.runOnGUIThread { log.items.add("$notification") }
    }
    override fun onGameActionResponse(response: GameActionResponse) {
      BoardGameApplication.runOnGUIThread { log.items.add("$response") }
    }
    override fun onGameActionReceived(message: GameAction, sender: String) {
      BoardGameApplication.runOnGUIThread { log.items.add("$sender sent $message") }
    }
  }
}
