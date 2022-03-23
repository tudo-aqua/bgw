/*
 * Copyright 2021-2022 The BoardGameWork Authors
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

@file:Suppress("unused")

package tools.aqua.bgw.components.uicomponents

import tools.aqua.bgw.observable.properties.Property
import tools.aqua.bgw.util.Font

/**
 * A [TableColumn] may be used to represent a column in a [TableView].
 *
 * @constructor Creates a [TableColumn].
 *
 * @param T [TableView] content type.
 * @property title The title for this [TableColumn]. It gets displayed in the header row of the
 * [TableView].
 * @property width The [width] for this [TableColumn].
 * @param font [Font] to be used for this [TableColumn]. Default: default [Font] constructor.
 * @property formatFunction The format function for this [TableColumn]. It gets applied to each item
 * in the [TableView] to get a [String] for its cell.
 *
 * @see TableView
 */
open class TableColumn<T>(
    val title: String,
    val width: Number,
    font: Font = Font(),
    formatFunction: (T) -> String
) {

  /**
   * [Property] for the [Font] of this [TableColumn].
   *
   * @see Font
   */
  val fontProperty: Property<Font> = Property(font)

  /**
   * [Font] of this [TableColumn].
   *
   * @see Font
   * @see fontProperty
   */
  var font: Font
    get() = fontProperty.value
    set(value) {
      fontProperty.value = value
    }

  /**
   * [Property] for the [formatFunction] that gets used to obtain a [String] representation for each
   * item.
   *
   * If the value is `null`, the [toString] function of the item is used instead.
   *
   * @see formatFunction
   */
  val formatFunctionProperty: Property<((T) -> String)?> = Property(formatFunction)

  /**
   * The [formatFunction] that gets used to obtain a [String] representation for each item.
   *
   * If the value is `null`, the [toString] function of the item is used instead.
   *
   * @see formatFunctionProperty
   */
  var formatFunction: ((T) -> String)?
    get() = formatFunctionProperty.value
    set(value) {
      formatFunctionProperty.value = value
    }
}
