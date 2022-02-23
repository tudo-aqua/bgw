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

/** Class representing a game of MauMau. */
class MauMauGame(player1: String = "Player 1", player2: String = "Player 2") {
  /** Collection of all cards in the game. */
  val mauMauCards: MutableCollection<MauMauCard> = ArrayList()

  /** Players. */
  val players: MutableList<MauMauPlayer> =
      mutableListOf(MauMauPlayer(player1), MauMauPlayer(player2))

  /** The draw stack. */
  val drawStack: DrawStack = DrawStack()

  /** The game stack. */
  val gameStack: GameStack = GameStack()

  /** Next suit to be placed. May differ from topmost game stack card due to jack selection. */
  var nextSuit: CardSuit = CardSuit.HEARTS

  /** Shuffles cards from game stack back to draw stack. */
  fun shuffleGameStackBack() {
    drawStack.shuffleBack(gameStack.shuffleBack())
  }
}
