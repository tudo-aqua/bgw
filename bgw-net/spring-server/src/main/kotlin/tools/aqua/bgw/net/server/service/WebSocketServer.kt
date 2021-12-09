package tools.aqua.bgw.net.server.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
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
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator
import org.springframework.web.socket.handler.TextWebSocketHandler
import org.springframework.web.socket.server.HandshakeInterceptor
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean
import java.lang.Exception

/**
 * Idle timeout constant for WebSocket (in milliseconds).
 */
const val IDLE_TIMEOUT: Long = 600000

@Configuration
@EnableWebSocket
class WebSocketServerConfiguration(@Autowired private val wsHandler: MyWebsocketHandler) : WebSocketConfigurer {

	@Bean
	fun createWebSocketContainer() = ServletServerContainerFactoryBean().apply {
		setMaxSessionIdleTimeout(IDLE_TIMEOUT)
	}


	override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
		registry
			.addHandler(wsHandler, "/chat")
			.addInterceptors(MyHandshakeInterceptor())
	}
}

class MyHandshakeInterceptor() : HandshakeInterceptor {
	override fun beforeHandshake(
		request: ServerHttpRequest,
		response: ServerHttpResponse,
		wsHandler: WebSocketHandler,
		attributes: MutableMap<String, Any>
	): Boolean {
		var isSuccess = request.headers.getFirst("soprasecret") == "geheim"
		with(request.headers.getFirst("playername")) {
			if (this == null) {
				isSuccess = false
			} else {
				attributes["playerName"] = this
			}
		}
		return isSuccess
	}

	override fun afterHandshake(
		request: ServerHttpRequest,
		response: ServerHttpResponse,
		wsHandler: WebSocketHandler,
		exception: Exception?
	) {}
}

@Component
class MyWebsocketHandler(
	@Autowired private val playerService: PlayerService,
	@Autowired private val messageService: MessageService,
	@Autowired private val gameService: GameService,
) : TextWebSocketHandler() {
	override fun handleTransportError(session: WebSocketSession, throwable: Throwable) {
		println("handleTransportError: ${throwable.localizedMessage} $throwable")
		throw throwable
	}

	override fun afterConnectionEstablished(session: WebSocketSession) {
		playerService.createPlayer(session)
		println(playerService.getAll())
	}

	override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
		playerService.deletePlayer(session)
		println(playerService.getAll())
	}

	override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
		messageService.handleMessage(session, message.payload)
		println(gameService.getAll())
	}
}