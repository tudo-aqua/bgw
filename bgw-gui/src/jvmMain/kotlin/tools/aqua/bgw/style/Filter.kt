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

class Filter : Observable() {
  private val declarations = mutableMapOf<String, FilterDeclaration>()

  fun getDeclarations(): Map<String, String?> {
    return declarations.mapValues { it.value.toFilter() }
  }

  fun applyDeclarations(filter: Filter) {
    declarations.clear()
    declarations.putAll(filter.declarations)
  }

  var blur: BlurFilter = BlurFilter.NONE
    set(value) {
      field = value
      declarations["blur"] = value
      notifyGUIListener()
    }
    get() = declarations["blur"] as BlurFilter

  var saturation: SaturationFilter = SaturationFilter.NONE
    set(value) {
      field = value
      declarations["saturation"] = value
      notifyGUIListener()
    }
    get() = declarations["saturation"] as SaturationFilter

  var sepia: SepiaFilter = SepiaFilter.NONE
    set(value) {
      field = value
      declarations["sepia"] = value
      notifyGUIListener()
    }
    get() = declarations["sepia"] as SepiaFilter
}

interface FilterDeclaration {
  var value: String?
  fun toFilter(): String?
}

class BlurFilter(radius: Double) : FilterDeclaration {
  override var value: String? = ""

  init {
    value = if (radius > 0.0) "${radius}em" else null
  }

  override fun toFilter(): String? {
    return if (value != null) "blur($value)" else null
  }

  companion object {
    val NONE = BlurFilter(0.0)
    val SMALL = BlurFilter(4.0)
    val MEDIUM = BlurFilter(8.0)
    val LARGE = BlurFilter(16.0)
  }
}

class SaturationFilter(saturation: Double) : FilterDeclaration {
  override var value: String? = ""

  init {
    value = if (saturation != 1.0) "$saturation" else null
  }

  override fun toFilter(): String? {
    return if (value != null) "saturate($value)" else null
  }

  companion object {
    val NONE = SaturationFilter(1.0)
    val GREYSCALE = SaturationFilter(0.0)
  }
}

class SepiaFilter(sepia: Double) : FilterDeclaration {
  override var value: String? = ""

  init {
    value = if (sepia != 0.0) "$sepia" else null
  }

  override fun toFilter(): String? {
    return if (value != null) "sepia($value)" else null
  }

  companion object {
    val NONE = SepiaFilter(0.0)
    val SEPIA = SepiaFilter(1.0)
  }
}
