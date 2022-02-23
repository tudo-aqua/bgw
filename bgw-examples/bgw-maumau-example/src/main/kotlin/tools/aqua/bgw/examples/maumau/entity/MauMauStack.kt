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

import java.util.*

/** Abstract baseclass for the stacks in the game. */
open class MauMauStack private constructor() {
  /** The cards currently in this stack. */
  val cards: Stack<MauMauCard> = Stack<MauMauCard>()

  /**
   * Returns the amount of cards currently in this stack.
   *
   * @return Number of cards in this stack.
   */
  fun size(): Int = cards.size

  /**
   * Returns whether this stack is empty.
   *
   * @return `true` if there are no cards in this stack, i.e. [size] = 0, `false` otherwise.
   */
  fun isEmpty(): Boolean = size() == 0

  /**
   * Returns the topmost card of this stack without removing it.
   *
   * @return The topmost card.
   */
  fun peek(): MauMauCard = cards.peek()
}
