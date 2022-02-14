package tools.aqua.bgw.examples.maumau.service

import tools.aqua.bgw.examples.maumau.entity.CardSuit
import tools.aqua.bgw.examples.maumau.entity.CardValue
import tools.aqua.bgw.examples.maumau.entity.MauMauCard
import tools.aqua.bgw.examples.maumau.entity.MauMauGame
import tools.aqua.bgw.examples.maumau.net.InitGameMessage

class SerializationUtil {
	companion object {
		
		/**
		 * Serializes [MauMauCard] for exchange format
		 */
		fun MauMauCard.serialize() : String = "${cardValue}_${cardSuit}"
		
		/**
		 * Deserializes exchange format to [MauMauCard]
		 */
		fun deserializeMauMauCard(payload: String) : MauMauCard {
			val split = payload.split("_")
			return MauMauCard(cardSuit = CardSuit.valueOf(split[1]), cardValue = CardValue.valueOf(split[0]))
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