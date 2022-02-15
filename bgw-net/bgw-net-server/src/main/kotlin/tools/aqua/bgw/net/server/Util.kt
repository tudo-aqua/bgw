package tools.aqua.bgw.net.server

import org.springframework.web.socket.WebSocketSession
import tools.aqua.bgw.net.server.entity.Player

val WebSocketSession.player: Player
	get() {
		val player = attributes["player"] ?: error("missing attribute") //TODO
		if (player is Player) return player else error("wrong type") //TODO
	}