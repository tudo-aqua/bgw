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

package tools.aqua.bgw.examples.tetris.service

import tools.aqua.bgw.examples.tetris.entity.Piece
import tools.aqua.bgw.examples.tetris.entity.Tetris

/** Utility class to check tetris rules. */
object TetrisChecker {
  /**
   * Checks for collisions between current tetris grid and the piece.
   *
   * @param tetris Fixed tetris grid.
   * @param piece Current piece.
   * @param offsetY Y offset between currentYPosition and position to check.
   * @param offsetX X offset between currentXPosition and position to check.
   */
  fun checkCollision(
      tetris: Tetris,
      piece: Piece = tetris.currentPiece,
      offsetY: Int = 0,
      offsetX: Int = 0
  ): Boolean {
    val grid = tetris.tetrisGrid

    for (y in 0 until piece.height) {
      for (x in 0 until piece.width) {
        val yPos = y + tetris.currentYPosition + offsetY - 1
        val xPos = x + tetris.currentXPosition + offsetX - 1

        if (yPos < 0) continue

        if (yPos > 19 || xPos > 9 || xPos < 0) return true

        if (piece.tiles[y][x] != null && grid[yPos][xPos].imageVisual != null) return true
      }
    }
    return false
  }

  /**
   * Checks whether given row is full.
   *
   * @param tetris Tetris to check.
   * @param row Row to check.
   */
  fun isRowFull(tetris: Tetris, row: Int): Boolean {
    for (x in 0 until 10) {
      if (tetris.tetrisGrid[row][x].imageVisual == null) return false
    }
    return true
  }
}
