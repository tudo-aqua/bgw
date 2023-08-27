/*
 * Copyright 2021-2023 The BoardGameWork Authors
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

@file:Suppress("unused")

package tools.aqua.bgw.core

import kotlinx.serialization.Serializable
import tools.aqua.bgw.core.HorizontalAlignment.LEFT
import tools.aqua.bgw.core.HorizontalAlignment.RIGHT
import tools.aqua.bgw.core.VerticalAlignment.BOTTOM
import tools.aqua.bgw.core.VerticalAlignment.TOP

/**
 * Used to define a centering behaviour. Encapsulates [VerticalAlignment] and [HorizontalAlignment].
 *
 * @property verticalAlignment Vertical alignment component.
 * @property horizontalAlignment Horizontal alignment component.
 *
 * @see BoardGameScene
 * @see BoardGameApplication
 * @see VerticalAlignment
 * @see HorizontalAlignment
 */
enum class Alignment(
    val verticalAlignment: VerticalAlignment,
    val horizontalAlignment: HorizontalAlignment
) {
  /** [Alignment] in the top left corner. */
  TOP_LEFT(TOP, LEFT),

  /** [Alignment] in the top right corner. */
  TOP_RIGHT(TOP, RIGHT),

  /** [Alignment] on the top centered horizontally. */
  TOP_CENTER(TOP, HorizontalAlignment.CENTER),

  /** [Alignment] in the bottom left corner. */
  BOTTOM_LEFT(BOTTOM, LEFT),

  /** [Alignment] in the bottom right corner. */
  BOTTOM_RIGHT(BOTTOM, RIGHT),

  /** [Alignment] on the bottom centered horizontally. */
  BOTTOM_CENTER(BOTTOM, HorizontalAlignment.CENTER),

  /** [Alignment] on the left centered vertically. */
  CENTER_LEFT(VerticalAlignment.CENTER, LEFT),

  /** [Alignment] on the right centered vertically. */
  CENTER_RIGHT(VerticalAlignment.CENTER, RIGHT),

  /** [Alignment] centered horizontally and vertically. */
  CENTER(VerticalAlignment.CENTER, HorizontalAlignment.CENTER);

  companion object {
    /**
     * Creates [Alignment] instance out of [VerticalAlignment] and [HorizontalAlignment].
     *
     * @param v Vertical component.
     * @param h Horizontal component.
     */
    fun of(v: VerticalAlignment, h: HorizontalAlignment): Alignment =
        when {
          v == TOP && h == LEFT -> TOP_LEFT
          v == TOP && h == RIGHT -> TOP_RIGHT
          v == TOP && h == HorizontalAlignment.CENTER -> TOP_CENTER
          v == BOTTOM && h == LEFT -> BOTTOM_LEFT
          v == BOTTOM && h == RIGHT -> BOTTOM_RIGHT
          v == BOTTOM && h == HorizontalAlignment.CENTER -> BOTTOM_CENTER
          v == VerticalAlignment.CENTER && h == LEFT -> CENTER_LEFT
          v == VerticalAlignment.CENTER && h == RIGHT -> CENTER_RIGHT
          v == VerticalAlignment.CENTER && h == HorizontalAlignment.CENTER -> CENTER
          else -> throw UnsupportedOperationException()
        }
  }
}
