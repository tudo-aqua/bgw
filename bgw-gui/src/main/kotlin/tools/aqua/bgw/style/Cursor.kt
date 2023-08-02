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

open class Cursor internal constructor(override val value: String = "") : StyleAttribute() {
  override val key: String = "-fx-cursor"

  companion object {
    val NULL = Cursor("null")
    val CROSSHAIR = Cursor("crosshair")
    val E_RESIZE = Cursor("e-resize")
    val H_RESIZE = Cursor("h-resize")
    val NE_RESIZE = Cursor("ne-resize")
    val NW_RESIZE = Cursor("nw-resize")
    val N_RESIZE = Cursor("n-resize")
    val SE_RESIZE = Cursor("se-resize")
    val SW_RESIZE = Cursor("sw-resize")
    val S_RESIZE = Cursor("s-resize")
    val W_RESIZE = Cursor("w-resize")
    val V_RESIZE = Cursor("v-resize")
    val TEXT = Cursor("text")
    val WAIT = Cursor("wait")
  }
}
