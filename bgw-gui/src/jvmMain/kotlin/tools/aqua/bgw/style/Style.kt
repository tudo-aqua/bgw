/*
 * Copyright 2025 The BoardGameWork Authors
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

import tools.aqua.bgw.observable.Observable

class Style : Observable() {
  private val declarations = mutableMapOf<String, StyleDeclaration>()

  fun getDeclarations(): Map<String, String> {
    return declarations.mapValues { it.value.toCSS() }
  }

  fun applyDeclarations(style: Style) {
    declarations.clear()
    declarations.putAll(style.declarations)
  }

  var borderRadius: BorderRadius = BorderRadius.NONE
    set(value) {
      field = value
      declarations["border-radius"] = value
      notifyGUIListener()
    }
    get() = declarations["border-radius"] as BorderRadius

  var cursor: Cursor = Cursor.DEFAULT
    set(value) {
      field = value
      declarations["cursor"] = value
      notifyGUIListener()
    }
    get() = declarations["cursor"] as Cursor
}

interface StyleDeclaration {
  fun toCSS(): String
}

class BorderRadius : StyleDeclaration {
  var topLeft: String = "0em"
  var topRight: String = "0em"
  var bottomRight: String = "0em"
  var bottomLeft: String = "0em"

  constructor(radius: Number) {
    topLeft = "${radius}em"
    topRight = "${radius}em"
    bottomRight = "${radius}em"
    bottomLeft = "${radius}em"
  }

  constructor(topLeft: Number, topRight: Number, bottomRight: Number, bottomLeft: Number) {
    this.topLeft = "${topLeft}em"
    this.topRight = "${topRight}em"
    this.bottomRight = "${bottomRight}em"
    this.bottomLeft = "${bottomLeft}em"
  }

  constructor(radius: String) : this(radius, radius, radius, radius)

  constructor(topLeft: String, topRight: String, bottomRight: String, bottomLeft: String) {
    this.topLeft = topLeft
    this.topRight = topRight
    this.bottomRight = bottomRight
    this.bottomLeft = bottomLeft
  }

  override fun toCSS(): String {
    if (topLeft == topRight && topLeft == bottomRight && topLeft == bottomLeft) {
      return topLeft
    }

    return "$topLeft $topRight $bottomRight $bottomLeft"
  }

  companion object {
    val NONE = BorderRadius(0)
    val XS = BorderRadius(2)
    val SMALL = BorderRadius(4)
    val MEDIUM = BorderRadius(8)
    val LARGE = BorderRadius(16)
    val XL = BorderRadius(32)
    val XXL = BorderRadius(64)
    val XXXL = BorderRadius(128)
    val FULL = BorderRadius("50%")
  }
}

@Deprecated("Use BorderRadius instead", ReplaceWith("BorderRadius"))
class BackgroundRadius : StyleDeclaration {
  override fun toCSS(): String {
    return ""
  }
}

class BorderWidth : StyleDeclaration {
  override fun toCSS(): String {
    TODO("Not yet implemented")
  }
}

class BorderColor : StyleDeclaration {
  override fun toCSS(): String {
    TODO("Not yet implemented")
  }
}

class Cursor private constructor(var name: String) : StyleDeclaration {

  override fun toCSS(): String {
    return this.name.lowercase()
  }

  companion object {
    val DEFAULT = Cursor("default")
    val NONE = Cursor("none")
    val CONEXT_MENU = Cursor("context-menu")
    val HELP = Cursor("help")
    val POINTER = Cursor("pointer")
    val PROGRESS = Cursor("progress")
    val WAIT = Cursor("wait")
    val CELL = Cursor("cell")
    val CROSSHAIR = Cursor("crosshair")
    val TEXT = Cursor("text")
    val VERTICAL_TEXT = Cursor("vertical-text")
    val ALIAS = Cursor("alias")
    val COPY = Cursor("copy")
    val MOVE = Cursor("move")
    val NO_DROP = Cursor("no-drop")
    val NOT_ALLOWED = Cursor("not-allowed")
    val GRAB = Cursor("grab")
    val GRABBING = Cursor("grabbing")
    val ALL_SCROLL = Cursor("all-scroll")
    val COL_RESIZE = Cursor("col-resize")
    val ROW_RESIZE = Cursor("row-resize")
    val N_RESIZE = Cursor("n-resize")
    val E_RESIZE = Cursor("e-resize")
    val S_RESIZE = Cursor("s-resize")
    val W_RESIZE = Cursor("w-resize")
    val NE_RESIZE = Cursor("ne-resize")
    val NW_RESIZE = Cursor("nw-resize")
    val SE_RESIZE = Cursor("se-resize")
    val SW_RESIZE = Cursor("sw-resize")
    val EW_RESIZE = Cursor("ew-resize")
    val NS_RESIZE = Cursor("ns-resize")
    val NESW_RESIZE = Cursor("nesw-resize")
    val NWSE_RESIZE = Cursor("nwse-resize")
    val ZOOM_IN = Cursor("zoom-in")
    val ZOOM_OUT = Cursor("zoom-out")
  }
}

class BorderStyle : StyleDeclaration {
  override fun toCSS(): String {
    TODO("Not yet implemented")
  }
}
