/*
 * Copyright 2023-2024 The BoardGameWork Authors
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

open class BorderRadius internal constructor(override val value: String = "") : StyleAttribute() {
  override val key: String = "-fx-border-radius"
  constructor(pixel: Int) : this("${pixel}px")

  companion object {
    val NONE = BorderRadius(0)
    val XS = BorderRadius(2)
    val SMALL = BorderRadius(4)
    val MEDIUM = BorderRadius(6)
    val LARGE = BorderRadius(8)
    val XL = BorderRadius(12)
    val XXL = BorderRadius(16)
    val XXXL = BorderRadius(24)
    val FULL =
        object : BorderRadius() {
          override val value: String
            get() = "100%"
        }
  }
}
