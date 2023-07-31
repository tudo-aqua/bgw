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

open class BackgroundRadius internal constructor(override val value: String = "") :
    StyleAttribute() {
  override val key: String = "-fx-background-radius"

  constructor(pixel: Int) : this("${pixel}px")

  companion object {
    val NONE = BackgroundRadius(0)
    val XS = BackgroundRadius(2)
    val SMALL = BackgroundRadius(4)
    val MEDIUM = BackgroundRadius(6)
    val LARGE = BackgroundRadius(8)
    val XL = BackgroundRadius(12)
    val XXL = BackgroundRadius(16)
    val XXXL = BackgroundRadius(24)
    val FULL =
        object : BackgroundRadius() {
          override val value: String
            get() = "100%"
        }
  }
}
