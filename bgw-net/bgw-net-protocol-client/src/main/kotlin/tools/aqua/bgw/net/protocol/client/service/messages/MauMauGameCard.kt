/*
 * Copyright 2022-2023 The BoardGameWork Authors
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

package tools.aqua.bgw.net.protocol.client.service.messages

/**
 * MauMauGameCard data class for serialization.
 *
 * @property suit The card suit.
 * @property value The card value.
 */
data class MauMauGameCard(val suit: String, val value: String) {
  override fun toString(): String {
    val cardSuit =
        when (suit) {
          "D" -> "Diamonds"
          "H" -> "Hearts"
          "S" -> "Spades"
          "C" -> "Clubs"
          else -> suit
        }

    val cardValue =
        when (value) {
          "A" -> "Ace"
          "J" -> "Jack"
          "Q" -> "Queen"
          "K" -> "King"
          else -> value
        }

    return "$cardValue of $cardSuit"
  }
}
