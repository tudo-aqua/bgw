/*
 * Copyright 2022 The BoardGameWork Authors
 * SPDX-License-Identifier: Apache-2.0
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tools.aqua.bgw.examples.maumau.service.network.messages

import tools.aqua.bgw.net.common.GameAction
import tools.aqua.bgw.net.common.annotations.GameActionClass

/**
 * GameActionMessage data class for serialization.
 *
 * @property action Associated game action.
 * @property card Played card.
 */
@GameActionClass
data class MauMauGameAction(val action: String, val card: MauMauGameCard? = null) : GameAction() {
	override fun printToString(): String {
		val text = when(action) {
			"PLAY_CARD" -> "Played the $card."
			"DRAW_CARD" -> "Drawn the $card from the stack."
			"OPPONENT_DRAW_TWO_CARDS" -> "The opponent must take two cards."
			"REQUEST_SUIT_SELECTION" -> "Requesting suit ${card?.suit}."
			"END_TURN" -> "End of turn."
			else -> ""
		}

		return "Type: $action\n" +
				"Card: $card\n" +
				text
	}
}
