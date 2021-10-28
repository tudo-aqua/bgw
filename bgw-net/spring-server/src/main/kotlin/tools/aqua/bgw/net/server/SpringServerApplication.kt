package tools.aqua.bgw.net.server

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.Input
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.Route
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import org.springframework.web.socket.handler.TextWebSocketHandler

@SpringBootApplication
class DemoApplication

fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)
}

@Route
class MainView : VerticalLayout() {
	init {
		add("Hello World")
		add(Button("ich bin der knopf!").apply { addClickListener { println("performing backend code") } })
	}
}

@Configuration
@EnableWebSocket
class WebSocketServerConfiguration : WebSocketConfigurer {
	override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
		registry.addHandler(MyWebsocketHandler(), "/chat")
	}

}

@Component
class MyWebsocketHandler : TextWebSocketHandler() {
	override fun handleTransportError(session: WebSocketSession, throwable: Throwable) {
		println("handleTransportError")
	}

	override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
		println("closed")
	}

	override fun afterConnectionEstablished(session: WebSocketSession) {
		println("established")
	}

	override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
		println("received $message")
	}
}