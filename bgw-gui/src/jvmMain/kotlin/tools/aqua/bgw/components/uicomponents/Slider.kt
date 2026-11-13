/*
 * Copyright 2026 The BoardGameWork Authors
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

import tools.aqua.bgw.core.Color
import tools.aqua.bgw.core.DEFAULT_SLIDER_HEIGHT
import tools.aqua.bgw.core.DEFAULT_SLIDER_WIDTH
import tools.aqua.bgw.observable.properties.DoubleProperty
import tools.aqua.bgw.observable.properties.Property
import tools.aqua.bgw.style.BorderRadius
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.Visual

/**
 * A numeric [Slider] for selecting a value from a bounded range.
 *
 * The [visual] is the track of the [Slider] and fills the whole component. The [thumbVisual] marks
 * the selected [value] on the track. It is a square whose side is the [height] of a horizontal and
 * the [width] of a vertical [Slider].
 *
 * User interaction updates [value] and invokes [onValueChanged]. All numeric values must be finite,
 * [minValue] must not be greater than [maxValue], and [step] must be greater than zero. Values
 * outside the configured range are clamped to the closest bound.
 *
 * @param posX Horizontal coordinate for this [Slider]. Default: 0.
 * @param posY Vertical coordinate for this [Slider]. Default: 0.
 * @param width Width for this [Slider]. Default: [DEFAULT_SLIDER_WIDTH].
 * @param height Height for this [Slider]. Default: [DEFAULT_SLIDER_HEIGHT].
 * @param visual [Visual] of the track of this [Slider]. Default: a light gray [ColorVisual].
 * @param thumbVisual [Visual] of the thumb of this [Slider]. Default: a dark gray, round
 *   [ColorVisual].
 * @param minValue Initial lower bound. Default: 0.
 * @param maxValue Initial upper bound. Default: 100.
 * @param value Initial selected value. Default: [minValue].
 * @param step Interval between selectable values. Default: 1.
 * @param orientation [Orientation] of this [Slider]. Default: [Orientation.HORIZONTAL].
 * @constructor Creates a [Slider].
 * @see Visual
 * @see UIComponent
 * @since 0.11.1
 */
open class Slider(
    posX: Number = 0,
    posY: Number = 0,
    width: Number = DEFAULT_SLIDER_WIDTH,
    height: Number = DEFAULT_SLIDER_HEIGHT,
    visual: Visual = ColorVisual(Color.LIGHT_GRAY).apply { style.borderRadius = BorderRadius.XXXL },
    thumbVisual: Visual =
        ColorVisual(Color.DARK_GRAY).apply { style.borderRadius = BorderRadius.FULL },
    minValue: Double = 0.0,
    maxValue: Double = 100.0,
    value: Double = minValue,
    step: Double = 1.0,
    orientation: Orientation = Orientation.HORIZONTAL
) :
    UIComponent(
        posX = posX, posY = posY, width = width, height = height, font = Font(), visual = visual) {

  init {
    requireFinite("minValue", minValue)
    requireFinite("maxValue", maxValue)
    require(minValue <= maxValue) { "minValue must not be greater than maxValue." }
    requireFinite("value", value)
    requireValidStep(step)
  }

  /** [Property] for the lower bound of this [Slider]. */
  internal val minValueProperty: DoubleProperty = DoubleProperty(minValue)

  /** Lower bound of this [Slider]. */
  var minValue: Double
    get() = minValueProperty.value
    set(value) {
      requireFinite("minValue", value)
      require(value <= maxValue) { "minValue must not be greater than maxValue." }
      minValueProperty.value = value
      this.value = this.value
    }

  /** [Property] for the upper bound of this [Slider]. */
  internal val maxValueProperty: DoubleProperty = DoubleProperty(maxValue)

  /** Upper bound of this [Slider]. */
  var maxValue: Double
    get() = maxValueProperty.value
    set(value) {
      requireFinite("maxValue", value)
      require(value >= minValue) { "maxValue must not be less than minValue." }
      maxValueProperty.value = value
      this.value = this.value
    }

  /** [Property] for the selected value of this [Slider]. */
  internal val valueProperty: DoubleProperty = DoubleProperty(value.coerceIn(minValue, maxValue))

  /** Selected value of this [Slider]. Values outside the range are clamped. */
  var value: Double
    get() = valueProperty.value
    set(value) {
      requireFinite("value", value)
      valueProperty.value = value.coerceIn(minValue, maxValue)
    }

  /** [Property] for the interval between selectable values of this [Slider]. */
  internal val stepProperty: DoubleProperty = DoubleProperty(step)

  /** Interval between selectable values of this [Slider]. */
  var step: Double
    get() = stepProperty.value
    set(value) {
      requireValidStep(value)
      stepProperty.value = value
    }

  /** [Property] for the [thumbVisual] of this [Slider]. */
  internal val thumbVisualProperty: Property<Visual> = Property(thumbVisual)

  /** [Visual] of the thumb that marks the selected [value]. */
  var thumbVisual: Visual
    get() = thumbVisualProperty.value
    set(value) {
      thumbVisualProperty.value = value
    }

  /** [Property] for the [Orientation] of this [Slider]. */
  internal val orientationProperty: Property<Orientation> = Property(orientation)

  /** [Orientation] of this [Slider]. */
  var orientation: Orientation
    get() = orientationProperty.value
    set(value) {
      orientationProperty.value = value
    }

  init {
    valueProperty.internalListener = { _, newValue -> onValueChanged?.invoke(newValue) }
  }

  /**
   * Gets invoked whenever the selected [value] changes.
   *
   * @since 0.11.1
   */
  var onValueChanged: ((Double) -> Unit)? = null

  private companion object {
    fun requireFinite(name: String, value: Double) {
      require(value.isFinite()) { "$name must be finite." }
    }

    fun requireValidStep(value: Double) {
      requireFinite("step", value)
      require(value > 0.0) { "step must be greater than zero." }
    }
  }
}
