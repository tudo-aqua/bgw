package tools.aqua.bgw.net.server.service
//
//import kotlinx.serialization.decodeFromString
//import kotlinx.serialization.encodeToString
//import kotlinx.serialization.json.Json
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.stereotype.Service
//import org.springframework.web.socket.TextMessage
//import org.springframework.web.socket.WebSocketMessage
//import org.springframework.web.socket.WebSocketSession
//import tools.aqua.bgw.net.common.*
//import tools.aqua.bgw.net.server.entity.Player
//import java.lang.UnsupportedOperationException
//
//@Service
//class MessageService(@Autowired private val gameService: GameService, @Autowired private val playerService: PlayerService) {
//	fun handleMessage(session: WebSocketSession, messageString: String) {
//		val message : Message = Json.decodeFromString(messageString)
//		when (message) {
//			is Response -> throw UnsupportedOperationException()
//			is Notification -> throw UnsupportedOperationException()
//			is GameActionMessage -> session handle message
//			is CreateGameMessage -> session handle message
//			is JoinGameMessage -> session handle message
//			is LeaveGameMessage -> session handle message
//		}
//	}
//
//	fun sendNotification(session: WebSocketSession, notification: Notification) {
//		session.sendMessage(TextMessage(Json.encodeToString(notification)))
//	}
//
//	private infix fun WebSocketSession.handle(gameActionMessage: GameActionMessage) {
//
//	}
//
//	private infix fun WebSocketSession.handle(createGameMessage: CreateGameMessage) {
//		val (gameID, sessionID) = createGameMessage
//		val response = CreateGameResponse(gameService.createGame(gameID, sessionID, "s", playerService.getPlayerByID(id)))
//		sendMessage(TextMessage(Json.encodeToString(response)))
//	}
//
//	private infix fun WebSocketSession.handle(joinGameMessage: JoinGameMessage) {
//		val (sessionID, password, greeting) = joinGameMessage
//		val response = JoinGameResponse(gameService.joinGame(sessionID, password, greeting, playerService.getPlayerByID(id)))
//		sendMessage(TextMessage(Json.encodeToString(response)))
//	}
//
//	private infix fun WebSocketSession.handle(leaveGameMessage: LeaveGameMessage) {
//
//	}
//}