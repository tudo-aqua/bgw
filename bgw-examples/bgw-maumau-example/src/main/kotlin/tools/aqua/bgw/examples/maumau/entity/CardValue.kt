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

package tools.aqua.bgw.examples.maumau.entity

import java.util.*

/** Enum over all card values. */
enum class CardValue {
  /** Ace. */
  ACE,

  /** Two. */
  TWO,

  /** Three. */
  THREE,

  /** Four. */
  FOUR,

  /** Five. */
  FIVE,

  /** Six. */
  SIX,

  /** Seven. */
  SEVEN,

  /** Eight. */
  EIGHT,

  /** Nine. */
  NINE,

  /** Ten. */
  TEN,

  /** Jack. */
  JACK,

  /** Queen. */
  QUEEN,

  /** King. */
  KING;

  companion object {
    /** Returns a complete deck from ACE to KING including TWO to SIX. */
    @Suppress("unused")
    fun completeDeck(): EnumSet<CardValue> =
        EnumSet.of(ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING)

    /** Returns a short deck from ACE to KING excluding TWO to SIX. */
    fun shortDeck(): EnumSet<CardValue> =
        EnumSet.of(ACE, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING)
  }
}
