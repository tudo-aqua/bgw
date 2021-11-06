package tools.aqua.bgw.net.server.model

import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

class Player(val session: WebSocketSession, var game: Game?)