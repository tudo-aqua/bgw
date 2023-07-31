/*
 * Copyright 2023 The BoardGameWork Authors
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

package tools.aqua.bgw.style

import java.awt.Color

open class BorderColor internal constructor(override val value: String = "") : StyleAttribute() {
  override val key: String = "-fx-border-color"

  constructor(r: Int, g: Int, b: Int) : this("rgb($r, $g, $b)")
  constructor(color: Color) : this(color.red, color.green, color.blue)

  companion object {
    val TRANSPARENT = BorderColor("transparent")
    val WHITE = BorderColor(Color.WHITE)
    val LIGHT_GRAY = BorderColor(Color.LIGHT_GRAY)
    val GRAY = BorderColor(Color.GRAY)
    val DARK_GRAY = BorderColor(Color.DARK_GRAY)
    val BLACK = BorderColor(Color.BLACK)
    val RED = BorderColor(Color.RED)
    val PINK = BorderColor(Color.PINK)
    val ORANGE = BorderColor(Color.ORANGE)
    val YELLOW = BorderColor(Color.YELLOW)
    val GREEN = BorderColor(Color.GREEN)
    val MAGENTA = BorderColor(Color.MAGENTA)
    val CYAN = BorderColor(Color.CYAN)
    val BLUE = BorderColor(Color.BLUE)
  }
}
