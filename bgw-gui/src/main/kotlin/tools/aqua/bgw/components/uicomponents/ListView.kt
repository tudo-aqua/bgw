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

@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package tools.aqua.bgw.components.uicomponents

import tools.aqua.bgw.core.DEFAULT_LIST_VIEW_HEIGHT
import tools.aqua.bgw.core.DEFAULT_LIST_VIEW_WIDTH
import tools.aqua.bgw.observable.properties.Property
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.Visual

/**
 * A [ListView] displaying its items next to each other in the given orientation.
 *
 * The [formatFunction] is used to gain a [String] representation of each item. If no
 * [formatFunction] is specified the [toString] function gets used instead.
 *
 * @constructor Creates a [ListView].
 *
 * @param T Generic [ListView] content.
 * @param posX Horizontal coordinate for this [ListView]. Default: 0.
 * @param posY Vertical coordinate for this [ListView]. Default: 0.
 * @param width Width for this [ListView]. Default: [DEFAULT_LIST_VIEW_WIDTH].
 * @param height Height for this [ListView]. Default: [DEFAULT_LIST_VIEW_HEIGHT].
 * @param items Initial list of items for this [ListView]. Default: empty list.
 * @param font [Font] to be used for this [ListView]. Default: default [Font] constructor.
 * @param visual Background [Visual]. Default: [ColorVisual.WHITE].
 * @param orientation Orientation for this [ListView]. Default: [Orientation.VERTICAL].
 * @param selectionMode Selection mode to be used for this [ListView]. Default:
 * [SelectionMode.SINGLE].
 * @param selectionBackground Background for selected items in this [ListView]. Default:
 * [ColorVisual.BLUE].
 * @param formatFunction The [formatFunction] that is used to represent the items. Default: `null`.
 */
open class ListView<T>(
    posX: Number = 0,
    posY: Number = 0,
    width: Number = DEFAULT_LIST_VIEW_WIDTH,
    height: Number = DEFAULT_LIST_VIEW_HEIGHT,
    items: List<T> = emptyList(),
    font: Font = Font(),
    visual: Visual = ColorVisual.WHITE,
    orientation: Orientation = Orientation.VERTICAL,
    selectionMode: SelectionMode = SelectionMode.SINGLE,
    selectionBackground: ColorVisual = ColorVisual.BLUE,
    formatFunction: ((T) -> String)? = null
) :
    StructuredDataView<T>(
        posX = posX,
        posY = posY,
        width = width,
        height = height,
        items = items,
        font = font,
        visual = visual,
        selectionMode = selectionMode,
        selectionBackground = selectionBackground) {
  /**
   * [Property] for the [Orientation] of this [ListView].
   *
   * @see orientation
   */
  val orientationProperty: Property<Orientation> = Property(orientation)

  /**
   * [Orientation] of this [ListView] displayed.
   *
   * @see orientationProperty
   */
  var orientation: Orientation
    get() = orientationProperty.value
    set(value) {
      orientationProperty.value = value
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
