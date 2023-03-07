/*
 * Copyright 2021-2023 The BoardGameWork Authors
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

package tools.aqua.bgw.visual

import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.observable.properties.DoubleProperty
import tools.aqua.bgw.observable.properties.Property
import tools.aqua.bgw.observable.properties.StringProperty
import tools.aqua.bgw.util.Font

/**
 * A visual displaying text.
 *
 * @constructor Creates a [TextVisual] with given [text].
 *
 * @param text Text to display.
 * @param font [Font] to be used for the [text]. Default: default [Font] constructor.
 * @param alignment [Alignment] for the [text]. Default: [Alignment.CENTER].
 * @param offsetX The horizontal offset of the [text] from its anchorpoint. Default: 0.
 * @param offsetY The vertical offset of the [text] from its anchorpoint. Default: 0.
 */
open class TextVisual(
    text: String,
    font: Font = Font(),
    alignment: Alignment = Alignment.CENTER,
    offsetX: Number = 0,
    offsetY: Number = 0,
) : SingleLayerVisual() {
  /**
   * [Property] for the displayed [text].
   *
   * @see text
   */
  val textProperty: StringProperty = StringProperty(text)

  /**
   * The displayed [text].
   *
   * @see textProperty
   */
  var text: String
    get() = textProperty.value
    set(value) {
      textProperty.value = value
    }

  /**
   * [Property] for the displayed [text] [Font].
   *
   * @see font
   */
  val fontProperty: Property<Font> = Property(font)

  /**
   * The displayed [text] [Font].
   *
   * @see fontProperty
   */
  var font: Font
    get() = fontProperty.value
    set(value) {
      fontProperty.value = value
    }

  /**
   * [Property] for the [text] [Alignment].
   *
   * @see alignment
   */
  val alignmentProperty: Property<Alignment> = Property(alignment)

  /**
   * The [text] [Alignment].
   *
   * @see alignmentProperty
   */
  var alignment: Alignment
    get() = alignmentProperty.value
    set(value) {
      alignmentProperty.value = value
    }

  /**
   * [Property] for the x-axis [text] offset.
   *
   * @see offsetX
   */
  val offsetXProperty: DoubleProperty = DoubleProperty(offsetX)

  /**
   * The x-axis [text] offset.
   *
   * @see offsetXProperty
   */
  var offsetX: Double
    get() = offsetXProperty.value
    set(value) {
      offsetXProperty.value = value
    }

  /**
   * [Property] for the y-axis [text] offset.
   *
   * @see offsetY
   */
  val offsetYProperty: DoubleProperty = DoubleProperty(offsetY)

  /**
   * The y-axis [text] offset.
   *
   * @see offsetYProperty
   */
  var offsetY: Double
    get() = offsetYProperty.value
    set(value) {
      offsetYProperty.value = value
    }

  /** Copies this [TextVisual] to a new object. */
  override fun copy(): TextVisual =
      TextVisual(text, font).apply {
        transparency = this@TextVisual.transparency
        alignment = this@TextVisual.alignment
        offsetX = this@TextVisual.offsetX
        offsetY = this@TextVisual.offsetY
        style = this@TextVisual.style
      }
}
