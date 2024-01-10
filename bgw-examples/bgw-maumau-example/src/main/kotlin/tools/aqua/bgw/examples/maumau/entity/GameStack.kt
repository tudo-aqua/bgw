/*
 * Copyright 2022-2024 The BoardGameWork Authors
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

/** The game stack with open cards. */
class GameStack : MauMauStack() {

  /**
   * Adds [card] on top of this stack.
   *
   * @param card Card to be played and therefore added to this stack.
   */
  fun playCard(card: MauMauCard) {
    cards.push(card)
  }

  /**
   * Removes all but the topmost card from this stack and returns them as [List]. Note: This
   * function does not shuffle the returned cards.
   *
   * @return [List] of removed cards.
   */
  fun shuffleBack(): List<MauMauCard> {
    // pop topmost card
    val saved = cards.pop()

    // save all remaining cards for return
    val tmp = cards.toList()

    // clear Stack and push topmost
    cards.clear()
    cards.push(saved)

    // return cards
    return tmp
  }
}
