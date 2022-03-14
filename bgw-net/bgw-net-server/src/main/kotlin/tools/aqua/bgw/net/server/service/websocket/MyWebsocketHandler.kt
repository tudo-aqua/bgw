package tools.aqua.bgw.net.server.service.websocket

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import tools.aqua.bgw.net.common.notification.UserDisconnectedNotification
import tools.aqua.bgw.net.server.player
import tools.aqua.bgw.net.server.service.GameService
import tools.aqua.bgw.net.server.service.MessageService
import tools.aqua.bgw.net.server.service.PlayerService

@Component
class MyWebsocketHandler(
	private val playerService: PlayerService,
	private val messageService: MessageService,
	private val gameService: GameService,
) : TextWebSocketHandler() {

  private val logger = LoggerFactory.getLogger(javaClass)

  /** Logs the transport error. */
  override fun handleTransportError(session: WebSocketSession, throwable: Throwable) {
    logger.info(
        "A transport error occurred for user with session id ${session.id}. Error Message: ${throwable.localizedMessage}")
    throw throwable
  }

  /** Creates a new player and associates it with the [WebSocketSession]. */
  override fun afterConnectionEstablished(session: WebSocketSession) {
    playerService.createPlayer(session)
    logger.info("User with session id ${session.id} connected")
    logger.info("Connected players:" + playerService.getAll())
  }

  /**
   * First removes the player associated with the [WebSocketSession] from its game. Then deletes the
   * player.
   */
  override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
    val player = session.player
    val game = player.game
    gameService.leaveGame(player)

    if (game != null) {
      messageService.broadcastNotification(
          game, UserDisconnectedNotification("disconnected", player.name)
      )
    }

    playerService.deletePlayer(session)
    logger.info("User with session id ${session.id} disconnected")
    logger.info("Connected players:" + playerService.getAll())
  }

  /** Delegates the handling of the message payload to [messageService]. */
  override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
    logger.info("received message ${message.payload}")
    messageService.handleMessage(session, message.payload)
  }
}