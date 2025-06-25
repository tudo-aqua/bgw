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
 * Class that combines different filters applied to a [SingleLayerVisual].
 *
 * @see BlurFilter
 * @see SaturationFilter
 * @see SepiaFilter
 * @since 0.10
 */
class Filter : StylingDeclarationObservable() {
  /**
   * Defines the blur filter applied to the [SingleLayerVisual].
   *
   * @see BlurFilter
   */
  var blur: BlurFilter = BlurFilter.NONE
    set(value) {
      field = value
      declarations["blur"] = value
      notifyGUIListener()
    }
    get() = declarations["blur"] as BlurFilter

  /**
   * Defines the saturation filter applied to the [SingleLayerVisual].
   *
   * @see SaturationFilter
   */
  var saturation: SaturationFilter = SaturationFilter.NONE
    set(value) {
      field = value
      declarations["saturation"] = value
      notifyGUIListener()
    }
    get() = declarations["saturation"] as SaturationFilter

  /**
   * Defines the sepia filter applied to the [SingleLayerVisual].
   *
   * @see SepiaFilter
   */
  var sepia: SepiaFilter = SepiaFilter.NONE
    set(value) {
      field = value
      declarations["sepia"] = value
      notifyGUIListener()
    }
    get() = declarations["sepia"] as SepiaFilter
}

/**
 * Enum class representing the blur filter applied to a [SingleLayerVisual].
 *
 * @param radius Radius must be greater or equal to 0.
 * @constructor Creates a [BlurFilter] with the given radius.
 * @since 0.10
 */
class BlurFilter(radius: Double) : StylingDeclaration {
  internal var value: String? = ""

  init {
    value = if (radius > 0.0) "calc(var(--bgwUnit) * ${radius})" else null
  }

  override fun toValue(): String {
    return if (value != null) "blur($value)" else ""
  }

  companion object {
    /** No blur filter applied. */
    val NONE = BlurFilter(0.0)
    /** 4 pixel blur filter applied. */
    val SMALL = BlurFilter(4.0)
    /** 8 pixel blur filter applied. */
    val MEDIUM = BlurFilter(8.0)
    /** 16 pixel blur filter applied. */
    val LARGE = BlurFilter(16.0)
  }
}

/**
 * Enum class representing the saturation filter applied to a [SingleLayerVisual].
 *
 * @param saturation Saturation value must be greater or equal to 0.
 * @constructor Creates a [SaturationFilter] with the given saturation value.
 * @since 0.10
 */
class SaturationFilter(saturation: Double) : StylingDeclaration {
  internal var value: String? = ""

  init {
    value = if (saturation != 1.0) "$saturation" else null
  }

  override fun toValue(): String {
    return if (value != null) "saturate($value)" else ""
  }

  companion object {
    val NONE = SaturationFilter(1.0)
    val GREYSCALE = SaturationFilter(0.0)
  }
}

/**
 * Enum class representing the sepia filter applied to a [SingleLayerVisual].
 *
 * @param sepia Sepia value must be between 0 and 1.
 * @constructor Creates a [SepiaFilter] with the given sepia value.
 * @since 0.10
 */
class SepiaFilter(sepia: Double) : StylingDeclaration {
  internal var value: String? = ""

  init {
    value = if (sepia != 0.0) "$sepia" else null
  }

  override fun toValue(): String {
    return if (value != null) "sepia($value)" else ""
  }

  companion object {
    /** No sepia filter applied. */
    val NONE = SepiaFilter(0.0)
    /** Full sepia filter applied. */
    val SEPIA = SepiaFilter(1.0)
  }
}
