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

open class BorderWidth private constructor(override val value: String) : StyleAttribute() {
  override val key: String = "-fx-border-width"

  constructor(pixel: Int = 0) : this("${pixel}px")
  // constructor(rem: Double = 0.0) : this("${rem}rem")

  companion object {
    val NONE = BorderWidth(0)
    val XS = BorderWidth(2)
    val SMALL = BorderWidth(4)
    val MEDIUM = BorderWidth(6)
    val LARGE = BorderWidth(8)
    val XL = BorderWidth(12)
    val XXL = BorderWidth(16)
    val XXXL = BorderWidth(24)
    val FULL = BorderWidth("100%")
  }
}
