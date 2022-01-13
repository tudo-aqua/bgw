package tools.aqua.bgw.net.server

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.Input
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.Route
import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import org.springframework.web.socket.handler.TextWebSocketHandler
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean
import tools.aqua.bgw.net.server.entity.JsonSchema
import tools.aqua.bgw.net.server.entity.JsonSchemaRepository
import tools.aqua.bgw.net.server.service.ValidationService

@SpringBootApplication
class Application {
	@Bean
	//TODO remove
	fun commandLineRunner(jsonSchemaRepository: JsonSchemaRepository) : CommandLineRunner {
		return CommandLineRunner {
			jsonSchemaRepository.save(JsonSchema("test", "{}"))
			jsonSchemaRepository.getById("test").also {
				println(it)
			}
		}
	}
}

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}

@Configuration
@EnableScheduling
class AppConfig()

