/*
 * Copyright 2022-2025 The BoardGameWork Authors
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

package tools.aqua.bgw.examples.tetris.entity

/**
 * Representation of the current tetris grid.
 *
 * @param previewPieces All pieces to preview. Size has to be 3.
 */
class Tetris(previewPieces: Array<Piece>) {
  /** Current tetris grid. */
  val tetrisGrid: Array<Array<Tile>> = Array(20) { Array(10) { Tile(null) } }

  /** The three preview tiles. */
  val preview: Array<Piece> = previewPieces

  /** Current playing piece. Initially empty 2x3. */
  var currentPiece: Piece = Piece(Array(2) { Array(3) { null } })

  /** Current y position of playing piece. Initially 0. */
  var currentYPosition: Int = 0

  /** Current x position of playing piece. Initially 5 - centered. */
  var currentXPosition: Int = 5

  /** Current points. */
  var points: Int = 0

  /**
   * Replaces current piece with given next one in preview queue and sets given [nextPreviewPiece]
   * as last in queue.
   *
   * @param nextPreviewPiece The next piece to add to the preview queue.
   */
  fun nextPiece(nextPreviewPiece: Piece) {
    // Set new piece and reset position
    currentPiece = preview[2]
    currentYPosition = 0
    currentXPosition = 5

    // move previews and append next
    preview[2] = preview[1]
    preview[1] = preview[0]
    preview[0] = nextPreviewPiece
  }

  /** Moves piece down by one. */
  fun down() {
    currentYPosition++
  }

  /** Moves piece left by one. */
  fun left() {
    currentXPosition--
  }

  /** Moves piece right by one. */
  fun right() {
    currentXPosition++
  }
}
