package examples.net

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.ListView
import tools.aqua.bgw.components.uicomponents.TextField
import tools.aqua.bgw.components.uicomponents.UIComponent
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.net.client.BoardGameClient
import tools.aqua.bgw.visual.ColorVisual

fun main() {
	ExampleClient().apply {
		show()
		exit()
	}
}

class ExampleClient : BoardGameApplication() {
	private val menuScene = ClientMenuScene()

	init {
		showMenuScene(menuScene)
	}
}

@Serializable
data class GameAction(val string: String, val int: Int)

@Serializable
data class InvalidGameAction(val invalidString: String, val invalidInt : Int)

class ClientMenuScene() : MenuScene(background = ColorVisual.WHITE) {
	lateinit var client: BoardGameClient

	inner class CreateGamePane() : Pane<UIComponent>(posX = 50, posY = 200, height = 50, width = 400) {
		private val gameIdField = TextField().apply {
			resize(100, 50)
			reposition(0, 0)
			prompt = "GameID"
		}

		private val sessionIDField = TextField().apply {
			resize(100, 50)
			reposition(150, 0)
			prompt = "SessionID"
		}

		private val createGameButton = Button().apply {
			resize(100, 50)
			reposition(300, 0)
			visual = ColorVisual.LIGHT_GRAY
			text = "CREATE"
			onMouseClicked = {
				client.createGame(gameIdField.text, sessionIDField.text)
			}
		}

		init {
			this.addAll(gameIdField, sessionIDField, createGameButton)
		}

	}

	inner class JoinGamePane() : Pane<UIComponent>(posX = 50, posY = 400, height = 50, width = 400) {
		private val greetingField = TextField().apply {
			resize(100, 50)
			reposition(0, 0)
			prompt = "greeting"
		}

		private val sessionIDField = TextField().apply {
			resize(100, 50)
			reposition(150, 0)
			prompt = "SessionID"
		}

		private val joinGameButton = Button().apply {
			resize(100, 50)
			reposition(300, 0)
			visual = ColorVisual.LIGHT_GRAY
			text = "JOIN"
			onMouseClicked = {
				client.joinGame(sessionIDField.text, greetingField.text)
			}
		}

		init {
			this.addAll(greetingField, sessionIDField, joinGameButton)
		}
	}

	inner class SendActionPane() : Pane<UIComponent>(posX = 50, posY = 600, height = 50, width = 400) {
		private val stringField = TextField().apply {
			resize(100, 50)
			reposition(0, 0)
			prompt = "string"
		}

		private val intField = TextField().apply {
			resize(100, 50)
			reposition(150, 0)
			prompt = "int"
		}

		private val sendButton = Button().apply {
			resize(100, 50)
			reposition(300, 0)
			visual = ColorVisual.LIGHT_GRAY
			text = "SEND VALID"
			onMouseClicked = {
				client.sendGameActionMessage(GameAction(stringField.text, intField.text.toInt()))
			}
		}

		init {
			this.addAll(stringField, intField, sendButton)
		}
	}

	inner class SendInvalidActionPane() : Pane<UIComponent>(posX = 50, posY = 800, height = 50, width = 400) {
		private val stringField = TextField().apply {
			resize(100, 50)
			reposition(0, 0)
			prompt = "string"
		}

		private val intField = TextField().apply {
			resize(100, 50)
			reposition(150, 0)
			prompt = "int"
		}

		private val sendButton = Button().apply {
			resize(100, 50)
			reposition(300, 0)
			visual = ColorVisual.LIGHT_GRAY
			text = "SEND INVALID"
			onMouseClicked = {
				client.sendGameActionMessage(InvalidGameAction(stringField.text, intField.text.toInt()))
			}
		}



		init {
			this.addAll(stringField, intField, sendButton)
		}
	}

	private val leaveGameButton = Button().apply {
		resize(100, 50)
		reposition(350, 1000)
		visual = ColorVisual.LIGHT_GRAY
		text = "LEAVE"
		onMouseClicked = {
			client.leaveGame("goodbye my friends")
		}
	}

	private val playerNameField = TextField().apply {
		resize(100, 50)
		reposition(50, 100)
		prompt = "Player Name"
	}

	private val secretField = TextField().apply {
		resize(100, 50)
		reposition(200, 100)
		text = "geheim"
		prompt = "Secret"
	}

	private val connectButton = Button().apply {
		resize(100, 50)
		reposition(350, 100)
		visual = ColorVisual.LIGHT_GRAY
		text = "Connect"
	}

	private val disconnectButton = Button().apply {
		resize(100, 50)
		reposition(200, 100)
		visual = ColorVisual.LIGHT_GRAY
		text = "Disconnect"
		onMouseClicked = {
			client.disconnect()
			log.items.add("Disconnected")
			removeComponents(this)
			addComponents(playerNameField, connectButton)
		}
	}

	private val log = ListView<String>().apply {
		resize(900, 900)
		reposition(650, 100)
	}

	private val createGame = CreateGamePane()

	private val joinGame = JoinGamePane()

	private val sendAction = SendActionPane()

	private val sendInvalidAction = SendInvalidActionPane()

	init {
		disconnectButton.onMouseClicked = {
			client.disconnect()
			log.items.add("Disconnected")
			removeComponents(disconnectButton, createGame, joinGame, sendAction, sendInvalidAction, leaveGameButton)
			addComponents(playerNameField, secretField, connectButton)
		}
		connectButton.onMouseClicked = {
			client = BoardGameClient(playerNameField.text, secretField.text, "127.0.0.1", 8080)
			client.init()
			client.connect()
			removeComponents(playerNameField, secretField, connectButton)
			addComponents(disconnectButton, createGame, joinGame, sendAction, sendInvalidAction, leaveGameButton)
		}
		addComponents(playerNameField, secretField, connectButton, log)
		opacity = 1.0
	}

	fun BoardGameClient.init() {
		onOpen = {
			BoardGameApplication.runOnGUIThread() {
				log.items.add("Connection is now open")
			}
		}
		onClose = { code, reason, _ ->
			BoardGameApplication.runOnGUIThread() {
				log.items.add("Connection closed with code: $code and reason: $reason")
			}
		}
		onCreateGameResponse = {
			BoardGameApplication.runOnGUIThread() {
				log.items.add("$it")
			}
		}
		onJoinGameResponse = {
			BoardGameApplication.runOnGUIThread() {
				log.items.add("$it")
			}
		}
		onLeaveGameResponse = {
			BoardGameApplication.runOnGUIThread() {
				log.items.add("$it")
			}
		}
		onUserJoined = {
			BoardGameApplication.runOnGUIThread() {
				log.items.add("$it")
			}
		}
		onUserLeft = {
			BoardGameApplication.runOnGUIThread() {
				log.items.add("$it")
			}
		}
		onGameActionResponse = {
			BoardGameApplication.runOnGUIThread() {
				log.items.add("$it")
			}
		}
		onGameActionReceived = { payload, sender ->
			BoardGameApplication.runOnGUIThread() {
				log.items.add("$sender sent ${Json.decodeFromString<GameAction>(payload)}")
			}
		}
	}
}




