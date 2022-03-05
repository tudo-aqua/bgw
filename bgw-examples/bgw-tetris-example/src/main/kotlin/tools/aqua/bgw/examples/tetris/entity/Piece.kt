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

package tools.aqua.bgw.examples.tetris.entity

/**
 * Piece representation boxing array type of [Tile]s.
 *
 * @property tiles Tiles array, rows first
 */
data class Piece(val tiles: Array<Array<Tile?>>) {
  /** The height of this piece. */
  val height: Int = tiles.size

  /** The width of this piece. */
  val width: Int = tiles[0].size

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is Piece) return false

    if (!tiles.contentDeepEquals(other.tiles)) return false
    if (height != other.height) return false
    if (width != other.width) return false

    return true
  }

  override fun hashCode(): Int {
    var result = tiles.contentDeepHashCode()
    result = 31 * result + height
    result = 31 * result + width
    return result
  }
}
