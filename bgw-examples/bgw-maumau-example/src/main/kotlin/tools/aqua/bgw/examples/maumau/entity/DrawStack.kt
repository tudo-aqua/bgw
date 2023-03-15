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

/** The draw stack with hidden cards. */
class DrawStack : MauMauStack() {
  /** Pops the first card from the stack. */
  fun drawCard(): MauMauCard = cards.pop()

  /**
   * Shuffles given cards and adds them to the stack. Returns new permutation for network
   * communication.
   *
   * @param cards Cards to be shuffled.
   *
   * @return New permutation of cards on the stack.
   */
  fun shuffleBack(cards: List<MauMauCard>): List<MauMauCard> {
    val stack = cards.shuffled()
    this.cards.addAll(stack)
    return stack
  }
}
