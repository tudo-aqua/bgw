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

package tools.aqua.bgw.examples.maumau.service.network

import tools.aqua.bgw.examples.maumau.entity.*
import tools.aqua.bgw.examples.maumau.service.network.messages.MauMauGameCard
import tools.aqua.bgw.examples.maumau.service.network.messages.MauMauInitGameAction
import tools.aqua.bgw.examples.maumau.service.network.messages.MauMauShuffleStackGameAction

/**
 * Serialization helper for network communication. Implements an adapter between model instances and
 * exchange format.
 */
object NetworkSerialization {
  /** Serializes [MauMauCard] for exchange format. */
  fun MauMauCard.serialize(): MauMauGameCard =
      MauMauGameCard(serializeCardSuit(cardSuit), serializeCardValue(cardValue))

  /** Deserializes exchange format to [MauMauCard]. */
  fun MauMauGameCard.deserialize(): MauMauCard =
      MauMauCard(cardSuit = deserializeCardSuit(suit), cardValue = deserializeCardValue(value))

  /** Serializes game into game initialization exchange format. */
  fun serializeInitMessage(game: MauMauGame): MauMauInitGameAction =
      MauMauInitGameAction(
          hostCards = game.players[0].hand.cards.map { it.serialize() },
          yourCards = game.players[1].hand.cards.map { it.serialize() },
          drawStack = game.drawStack.cards.map { it.serialize() },
          gameStack = game.gameStack.cards.first().serialize())

  /** Serializes stack cards into exchange format. */
  fun serializeStacksShuffledMessage(game: MauMauGame): MauMauShuffleStackGameAction =
      MauMauShuffleStackGameAction(
          drawStack = game.drawStack.cards.map { it.serialize() },
          gameStack = game.gameStack.cards.first().serialize(),
      )

  /** Serializes [GameActionType] into exchange format. */
  fun serializeGameAction(action: GameActionType): String =
      when (action) {
        GameActionType.PLAY -> "PLAY_CARD"
        GameActionType.DRAW -> "DRAW_CARD"
        GameActionType.REQUEST_DRAW_TWO -> "OPPONENT_DRAW_TWO_CARDS"
        GameActionType.REQUEST_SUIT -> "REQUEST_SUIT_SELECTION"
        GameActionType.END_TURN -> "END_TURN"
      }

  /** Deserializes [GameActionType] from exchange format. */
  fun deserializeGameAction(action: String): GameActionType =
      when (action) {
        "PLAY_CARD" -> GameActionType.PLAY
        "DRAW_CARD" -> GameActionType.DRAW
        "OPPONENT_DRAW_TWO_CARDS" -> GameActionType.REQUEST_DRAW_TWO
        "REQUEST_SUIT_SELECTION" -> GameActionType.REQUEST_SUIT
        "END_TURN" -> GameActionType.END_TURN
        else -> throw NoSuchElementException("Invalid json enum value: $action")
      }

  // region Helper
  /** Serializes [CardSuit] into exchange format. */
  private fun serializeCardSuit(suit: CardSuit): String =
      when (suit) {
        CardSuit.CLUBS -> "C"
        CardSuit.SPADES -> "S"
        CardSuit.HEARTS -> "H"
        CardSuit.DIAMONDS -> "D"
      }

  /** Deserializes [CardSuit] from exchange format. */
  private fun deserializeCardSuit(suit: String): CardSuit =
      when (suit) {
        "C" -> CardSuit.CLUBS
        "S" -> CardSuit.SPADES
        "H" -> CardSuit.HEARTS
        "D" -> CardSuit.DIAMONDS
        else -> throw NoSuchElementException("Invalid json enum value: $suit")
      }

  /** Serializes [CardValue] into exchange format. */
  private fun serializeCardValue(value: CardValue): String =
      when (value) {
        CardValue.ACE -> "A"
        CardValue.TWO, CardValue.THREE, CardValue.FOUR, CardValue.FIVE, CardValue.SIX ->
            throw NoSuchElementException("This card value does not exist in MauMau.")
        CardValue.SEVEN -> "7"
        CardValue.EIGHT -> "8"
        CardValue.NINE -> "9"
        CardValue.TEN -> "10"
        CardValue.JACK -> "J"
        CardValue.QUEEN -> "Q"
        CardValue.KING -> "K"
      }

  /** Deserializes [CardValue] from exchange format. */
  private fun deserializeCardValue(value: String): CardValue =
      when (value) {
        "7" -> CardValue.SEVEN
        "8" -> CardValue.EIGHT
        "9" -> CardValue.NINE
        "10" -> CardValue.TEN
        "J" -> CardValue.JACK
        "Q" -> CardValue.QUEEN
        "K" -> CardValue.KING
        "A" -> CardValue.ACE
        else -> throw NoSuchElementException("Invalid json enum value: $value")
      }
  // endregion
}
