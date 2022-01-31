package tools.aqua.bgw.net.server.service

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import org.springframework.web.socket.handler.TextWebSocketHandler
import org.springframework.web.socket.server.HandshakeInterceptor
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean
import tools.aqua.bgw.net.common.UserDisconnectedNotification
import tools.aqua.bgw.net.server.entity.KeyValueRepository
import tools.aqua.bgw.net.server.player
import java.lang.Exception

@Configuration
@EnableWebSocket
class WebSocketServerConfiguration(
	private val wsHandler: MyWebsocketHandler,
	private val keyValueRepository: KeyValueRepository
) : WebSocketConfigurer {

	/**
	 * Not really needed anymore. An idle timeout could be configured here.
	 */
	@Bean
	fun createWebSocketContainer() = ServletServerContainerFactoryBean().apply {
		//setMaxSessionIdleTimeout(IDLE_TIMEOUT)
	}

	override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
		registry
			.addHandler(wsHandler, "/chat")
			.addInterceptors(MyHandshakeInterceptor(keyValueRepository))
	}
}

/**
 * Checks if the soprasecret HTTP header is matching the current SoPra secret.
 * Checks if the playername HTTP header is set and if its set,
 * assigns it as the playerName attribute to the session.
 *
 * Only allows establishment of web socket session if both checks succeed.
 */
class MyHandshakeInterceptor(private val keyValueRepository: KeyValueRepository) : HandshakeInterceptor {
	val logger = LoggerFactory.getLogger(javaClass)

	override fun beforeHandshake(
		request: ServerHttpRequest,
		response: ServerHttpResponse,
		wsHandler: WebSocketHandler,
		attributes: MutableMap<String, Any>
	): Boolean {
		val secret: String = try {
			keyValueRepository.findById("SoPra Secret").get()
		} catch (e: NoSuchElementException) {
			logger.error("SoPra Secret does not exist in database")
			return false
		}.value
		var isSuccess = (request.headers.getFirst("soprasecret") == secret)
		with(request.headers.getFirst("playername")) {
			if (this == null) {
				isSuccess = false
			} else {
				attributes["playerName"] = this
			}
		}
		return isSuccess
	}

	/**
	 * Does nothing.
	 */
	override fun afterHandshake(
		request: ServerHttpRequest,
		response: ServerHttpResponse,
		wsHandler: WebSocketHandler,
		exception: Exception?
	) {
	}
}

@Component
class MyWebsocketHandler(
	private val playerService: PlayerService,
	private val messageService: MessageService,
	private val gameService: GameService,
) : TextWebSocketHandler() {

	private val logger = LoggerFactory.getLogger(javaClass)

	/**
	 * Logs the transport error.
	 */
	override fun handleTransportError(session: WebSocketSession, throwable: Throwable) {
		logger.info("A transport error occurred for user with session id ${session.id}. Error Message: ${throwable.localizedMessage}")
	}

	/**
	 * Creates a new [Player] and associates it with the [WebSocketSession]
	 */
	override fun afterConnectionEstablished(session: WebSocketSession) {
		playerService.createPlayer(session)
		println(playerService.getAll())
		logger.info("User with session id ${session.id} connected")
	}

	/**
	 * First removes the [Player] associated with the [WebSocketSession] from its [Game].
	 * Then deletes the player.
	 */
	override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
		val player = session.player
		val game = player.game
		gameService.leaveGame(player)
		game?.let {
			messageService.broadcastNotification(
				game,
				UserDisconnectedNotification("disconnected", player.name)
			)
		}
		playerService.deletePlayer(session)
		logger.info("User with session id ${session.id} disconnected")
	}

	/**
	 * Delegates the handling of the message payload to [messageService].
	 */
	override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
		messageService.handleMessage(session, message.payload)
		println(gameService.getAll())
	}
}