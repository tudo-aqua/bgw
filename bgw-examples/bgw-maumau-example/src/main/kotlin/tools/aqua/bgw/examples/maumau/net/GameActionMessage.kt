package tools.aqua.bgw.examples.maumau.net

import tools.aqua.bgw.examples.maumau.entity.GameAction
import tools.aqua.bgw.examples.maumau.entity.MauMauCard

@Suppress("DataClassPrivateConstructor")
data class GameActionMessage private constructor(val action : String, val card : String) {
	constructor(gameAction : GameAction, card: MauMauCard) : this(gameAction.toString(), card.serialize())
}

