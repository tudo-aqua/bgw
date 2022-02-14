package tools.aqua.bgw.examples.maumau.service

import tools.aqua.bgw.examples.maumau.entity.MauMauGame
import tools.aqua.bgw.examples.maumau.net.InitGameMessage

class SerializationUtil {
	companion object {
		fun deserializeInitMessage(payload: String) {
		
		}
		
		fun serializeInitMessage(game: MauMauGame): InitGameMessage = InitGameMessage(
			drawStack = game.drawStack.cards.map { it.serialize() },
			gameStack = game.gameStack.cards.map { it.serialize() },
			hostCards = game.players[0].hand.cards.map { it.serialize() },
			yourCards = game.players[1].hand.cards.map { it.serialize() }
			)
	}
	
	data class GameInitData(
		val drawStack: List<String>,
		val gameStack: List<String>,
		val hostCards: List<String>,
		val yourCards: List<String>
	)
}