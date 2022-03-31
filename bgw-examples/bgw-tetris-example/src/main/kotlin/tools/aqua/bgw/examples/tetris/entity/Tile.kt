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

import tools.aqua.bgw.visual.ImageVisual

/**
 * Tile representation.
 *
 * @property color The Tile's color.
 */
data class Tile(val color: Color?) {
  /** Associated [ImageVisual]. */
  val imageVisual: ImageVisual? = color?.run { imageVisual.copy() }
}

/**
 * Enum for available colors.
 *
 * @property imageVisual Associated [ImageVisual].
 */
enum class Color(val imageVisual: ImageVisual) {
  GRAY(ImageVisual("gray.png")),
  BLUE(ImageVisual("blue.png")),
  CYAN(ImageVisual("cyan.png")),
  GREEN(ImageVisual("green.png")),
  ORANGE(ImageVisual("orange.png")),
  PURPLE(ImageVisual("purple.png")),
  RED(ImageVisual("red.png")),
  YELLOW(ImageVisual("yellow.png")),
}
