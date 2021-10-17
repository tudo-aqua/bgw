package tools.aqua.bgw.net.client.plugins

import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.response.*

fun Application.configureRouting() {
	// Starting point for a Ktor app:
	routing {
		get("/") {
			call.respondText("Hello World!")
		}
	}

}
