package tools.aqua.bgw.net.server.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import org.springframework.web.socket.handler.TextWebSocketHandler
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean

/**
 * Idle timeout constant for WebSocket (in milliseconds).
 */
const val IDLE_TIMEOUT: Long = 30000

@Configuration
@EnableWebSocket
class WebSocketServerConfiguration(@Autowired private val wsHandler: MyWebsocketHandler) : WebSocketConfigurer {

	@Bean
	fun createWebSocketContainer() = ServletServerContainerFactoryBean().apply {
		setMaxSessionIdleTimeout(IDLE_TIMEOUT)
	}


	override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
		registry.addHandler(wsHandler, "/chat")
	}
}

@Component
class MyWebsocketHandler(
	@Autowired private val playerService: PlayerService,
	@Autowired private val messageService: MessageService,
) : TextWebSocketHandler() {
	override fun handleTransportError(session: WebSocketSession, throwable: Throwable) {
		println("handleTransportError: ${throwable.localizedMessage}")
	}

	override fun afterConnectionEstablished(session: WebSocketSession) {
		playerService.createPlayer(session)
	}

	override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
		playerService.deletePlayer(session)
	}

	override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
		messageService.handleMessage(session, message.payload)
	}
}