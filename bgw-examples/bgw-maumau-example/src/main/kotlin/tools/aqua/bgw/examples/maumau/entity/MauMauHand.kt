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

package tools.aqua.bgw.examples.maumau.entity

/** Class representing a card hand containing a set of cards. */
class MauMauHand {
  /** The cards currently in the player's hand. */
  val cards: ArrayList<MauMauCard> = ArrayList()

  /**
   * Adds a card to this hand.
   *
   * @param card Card to be added.
   */
  fun addCard(card: MauMauCard) {
    cards.add(card)
  }

  /**
   * Removes a card from this hand.
   *
   * @param card Card to be removed.
   */
  fun removeCard(card: MauMauCard): Boolean = cards.remove(card)
}
