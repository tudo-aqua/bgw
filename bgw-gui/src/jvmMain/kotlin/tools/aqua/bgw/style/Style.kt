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

import tools.aqua.bgw.visual.SingleLayerVisual

/**
 * Class that combines different styles applied to a [SingleLayerVisual].
 *
 * @see BorderRadius
 * @see Cursor
 *
 * @since 0.10
 */
class Style : StylingDeclarationObservable() {
  /**
   * Defines the roundness of the corners of the [SingleLayerVisual].
   *
   * @see BorderRadius
   */
  var borderRadius: BorderRadius = BorderRadius.NONE
    set(value) {
      field = value
      declarations["border-radius"] = value
      notifyGUIListener()
    }
    get() = declarations["border-radius"] as BorderRadius

  /**
   * Defines the cursor visible while hovering over the [SingleLayerVisual].
   *
   * @see Cursor
   */
  var cursor: Cursor = Cursor.DEFAULT
    set(value) {
      field = value
      declarations["cursor"] = value
      notifyGUIListener()
    }
    get() = declarations["cursor"] as Cursor
}

/**
 * Enum class representing the roundness of the corners of a [SingleLayerVisual].
 *
 * @since 0.9
 */
class BorderRadius : StylingDeclaration {
  private var topLeft: String = "0em"
  private var topRight: String = "0em"
  private var bottomRight: String = "0em"
  private var bottomLeft: String = "0em"

  /**
   * Creates a [BorderRadius] with the same roundness for all corners.
   *
   * @constructor Creates a [BorderRadius] with the same roundness for all corners.
   *
   * @param radius Roundness in pixels.
   *
   * @since 0.10
   */
  constructor(radius: Number) {
    topLeft = "${radius}em"
    topRight = "${radius}em"
    bottomRight = "${radius}em"
    bottomLeft = "${radius}em"
  }

  /**
   * Creates a [BorderRadius] with different roundness for each corner.
   *
   * @constructor Creates a [BorderRadius] with different roundness for each corner.
   *
   * @param topLeft Roundness in pixels for the top left corner.
   * @param topRight Roundness in pixels for the top right corner.
   * @param bottomRight Roundness in pixels for the bottom right corner.
   * @param bottomLeft Roundness in pixels for the bottom left corner.
   *
   * @since 0.10
   */
  constructor(topLeft: Number, topRight: Number, bottomRight: Number, bottomLeft: Number) {
    this.topLeft = "${topLeft}em"
    this.topRight = "${topRight}em"
    this.bottomRight = "${bottomRight}em"
    this.bottomLeft = "${bottomLeft}em"
  }

  /**
   * Creates a [BorderRadius] with the same roundness for all corners.
   *
   * @constructor Creates a [BorderRadius] with the same roundness for all corners.
   *
   * @param radius Roundness.
   *
   * @since 0.10
   */
  constructor(radius: String) : this(radius, radius, radius, radius)

  /**
   * Creates a [BorderRadius] with different roundness for each corner.
   *
   * @constructor Creates a [BorderRadius] with different roundness for each corner.
   *
   * @param topLeft Roundness for the top left corner.
   * @param topRight Roundness for the top right corner.
   * @param bottomRight Roundness for the bottom right corner.
   * @param bottomLeft Roundness for the bottom left corner.
   *
   * @since 0.10
   */
  constructor(topLeft: String, topRight: String, bottomRight: String, bottomLeft: String) {
    this.topLeft = topLeft
    this.topRight = topRight
    this.bottomRight = bottomRight
    this.bottomLeft = bottomLeft
  }

  override fun toValue(): String {
    if (topLeft == topRight && topLeft == bottomRight && topLeft == bottomLeft) {
      return topLeft
    }

    return "$topLeft $topRight $bottomRight $bottomLeft"
  }

  companion object {
    /** No roundness. */
    val NONE = BorderRadius(0)
    /** 2 pixels roundness. */
    val XS = BorderRadius(2)
    /** 4 pixels roundness. */
    val SMALL = BorderRadius(4)
    /** 8 pixels roundness. */
    val MEDIUM = BorderRadius(8)
    /** 16 pixels roundness. */
    val LARGE = BorderRadius(16)
    /** 32 pixels roundness. */
    val XL = BorderRadius(32)
    /** 64 pixels roundness. */
    val XXL = BorderRadius(64)
    /** 128 pixels roundness. */
    val XXXL = BorderRadius(128)
    /** Full roundness i.e. circular. */
    val FULL = BorderRadius("50%")
  }
}

/**
 * Enum class representing the roundness of the corners of a background.
 *
 * @since 0.9
 */
@Deprecated("BackgroundRadius is no longer used as of BGW 0.10.", ReplaceWith("BorderRadius"))
internal class BackgroundRadius : StylingDeclaration {
  override fun toValue(): String {
    return ""
  }
}

/**
 * Enum class representing the width of the border of a [SingleLayerVisual].
 *
 * @since 0.9
 */
internal class BorderWidth : StylingDeclaration {
  override fun toValue(): String {
    TODO("Not yet implemented")
  }
}

/**
 * Enum class representing the color of the border of a [SingleLayerVisual].
 *
 * @since 0.9
 */
internal class BorderColor : StylingDeclaration {
  override fun toValue(): String {
    TODO("Not yet implemented")
  }
}

/**
 * Enum representing the cursor visible while hovering over a [SingleLayerVisual].
 *
 * @since 0.9
 */
class Cursor private constructor(var name: String) : StylingDeclaration {
  override fun toValue(): String {
    return this.name.lowercase()
  }

  companion object {
    /** Default cursor. */
    val DEFAULT = Cursor("default")

    /** No cursor. */
    val NONE = Cursor("none")

    /** Context menu cursor. */
    val CONEXT_MENU = Cursor("context-menu")

    /** Help cursor, usually a question mark. */
    val HELP = Cursor("help")

    /** Pointer cursor, usually a hand. */
    val POINTER = Cursor("pointer")

    /** Progress cursor, usually a spinning wheel. */
    val PROGRESS = Cursor("progress")

    /** Wait cursor, usually an hourglass or spinning wheel. */
    val WAIT = Cursor("wait")

    /** Cell cursor, usually a cross. */
    val CELL = Cursor("cell")

    /** Crosshair cursor. */
    val CROSSHAIR = Cursor("crosshair")

    /** Text cursor, usually an I-beam. */
    val TEXT = Cursor("text")

    /** Vertical text cursor. */
    val VERTICAL_TEXT = Cursor("vertical-text")

    /** Alias cursor, usually an arrow with a shortcut icon. */
    val ALIAS = Cursor("alias")

    /** Copy cursor, usually an arrow with a plus sign. */
    val COPY = Cursor("copy")

    /** Move cursor, usually a four-way arrow. */
    val MOVE = Cursor("move")

    /** No drop cursor, usually a hand with a no symbol. */
    val NO_DROP = Cursor("no-drop")

    /** Not allowed cursor, usually a no symbol. */
    val NOT_ALLOWED = Cursor("not-allowed")

    /** Grab cursor, usually a hand. */
    val GRAB = Cursor("grab")

    /** Grabbing cursor, usually a closed hand. */
    val GRABBING = Cursor("grabbing")

    /** All scroll cursor, usually a four-way arrow. */
    val ALL_SCROLL = Cursor("all-scroll")

    /** Column resize cursor, usually a horizontal double arrow. */
    val COL_RESIZE = Cursor("col-resize")

    /** Row resize cursor, usually a vertical double arrow. */
    val ROW_RESIZE = Cursor("row-resize")

    /** North resize cursor, usually an upward arrow. */
    val N_RESIZE = Cursor("n-resize")

    /** East resize cursor, usually a rightward arrow. */
    val E_RESIZE = Cursor("e-resize")

    /** South resize cursor, usually a downward arrow. */
    val S_RESIZE = Cursor("s-resize")

    /** West resize cursor, usually a leftward arrow. */
    val W_RESIZE = Cursor("w-resize")

    /** North-east resize cursor, usually an upward-right arrow. */
    val NE_RESIZE = Cursor("ne-resize")

    /** North-west resize cursor, usually an upward-left arrow. */
    val NW_RESIZE = Cursor("nw-resize")

    /** South-east resize cursor, usually a downward-right arrow. */
    val SE_RESIZE = Cursor("se-resize")

    /** South-west resize cursor, usually a downward-left arrow. */
    val SW_RESIZE = Cursor("sw-resize")

    /** East-west resize cursor, usually a horizontal double arrow. */
    val EW_RESIZE = Cursor("ew-resize")

    /** North-south resize cursor, usually a vertical double arrow. */
    val NS_RESIZE = Cursor("ns-resize")

    /** North-east-south-west resize cursor, usually a diagonal double arrow. */
    val NESW_RESIZE = Cursor("nesw-resize")

    /** North-west-south-east resize cursor, usually a diagonal double arrow. */
    val NWSE_RESIZE = Cursor("nwse-resize")

    /** Zoom in cursor, usually a magnifying glass with a plus sign. */
    val ZOOM_IN = Cursor("zoom-in")

    /** Zoom out cursor, usually a magnifying glass with a minus sign. */
    val ZOOM_OUT = Cursor("zoom-out")
  }
}

/**
 * Enum class representing the style of the border of a [SingleLayerVisual].
 *
 * @since 0.9
 */
internal class BorderStyle : StylingDeclaration {
  override fun toValue(): String {
    TODO("Not yet implemented")
  }
}
