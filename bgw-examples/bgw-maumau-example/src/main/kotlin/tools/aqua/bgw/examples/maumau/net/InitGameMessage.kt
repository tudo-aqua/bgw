package tools.aqua.bgw.examples.maumau.net

import tools.aqua.bgw.examples.maumau.entity.CardSuit
import tools.aqua.bgw.examples.maumau.entity.CardValue

data class InitGameMessage(val stack : List<Pair<CardSuit, CardValue>>)

