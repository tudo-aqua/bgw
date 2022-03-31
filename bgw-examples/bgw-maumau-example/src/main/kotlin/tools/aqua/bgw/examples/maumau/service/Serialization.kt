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

package tools.aqua.bgw.examples.maumau.service

import tools.aqua.bgw.examples.maumau.entity.CardSuit
import tools.aqua.bgw.examples.maumau.entity.CardValue
import tools.aqua.bgw.examples.maumau.entity.MauMauCard
import tools.aqua.bgw.examples.maumau.entity.MauMauGame
import tools.aqua.bgw.examples.maumau.service.messages.MauMauInitGameAction
import tools.aqua.bgw.examples.maumau.service.messages.MauMauShuffleStackGameAction

/** Serialization helper for network communication. */
object Serialization {
  /** Serializes [MauMauCard] for exchange format. */
  fun MauMauCard.serialize(): String = "${cardValue}_${cardSuit}"

  /** Deserializes exchange format to [MauMauCard]. */
  fun deserializeMauMauCard(payload: String): MauMauCard {
    val split = payload.split("_")
    return MauMauCard(
        cardSuit = CardSuit.valueOf(split[1]), cardValue = CardValue.valueOf(split[0]))
  }

  /** Serializes game into game initialization exchange format. */
  fun serializeInitMessage(game: MauMauGame): MauMauInitGameAction =
      MauMauInitGameAction(
          drawStack = game.drawStack.cards.map { it.serialize() },
          gameStack = game.gameStack.cards.first().serialize(),
          hostCards = game.players[0].hand.cards.map { it.serialize() },
          yourCards = game.players[1].hand.cards.map { it.serialize() })

  /** Serializes stack cards into exchange format. */
  fun serializeStacksShuffledMessage(game: MauMauGame): MauMauShuffleStackGameAction =
      MauMauShuffleStackGameAction(
          drawStack = game.drawStack.cards.map { it.serialize() },
          gameStack = game.gameStack.cards.first().serialize(),
      )
}
