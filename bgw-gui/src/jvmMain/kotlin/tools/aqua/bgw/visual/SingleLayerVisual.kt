/*
 * Copyright 2021-2025 The BoardGameWork Authors
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

import tools.aqua.bgw.observable.properties.LimitedDoubleProperty
import tools.aqua.bgw.observable.properties.Property
import tools.aqua.bgw.style.Filter
import tools.aqua.bgw.style.Flip
import tools.aqua.bgw.style.Style

/**
 * Baseclass for single layer visuals.
 *
 * @see Visual
 * @see CompoundVisual
 * @see TextVisual
 * @see ImageVisual
 * @see ColorVisual
 *
 * @since 0.1
 */
sealed class SingleLayerVisual(rot: Double) : Visual() {
  /**
   * Property for the [transparency] / alpha channel for this [Visual].
   *
   * Must be set between 0 (full transparent) and 1 (non-transparent / solid). Default: 1.
   *
   * @see transparency
   */
  internal val transparencyProperty: LimitedDoubleProperty = LimitedDoubleProperty(0, 1, 1)

  /**
   * The [transparency] / alpha channel for this [Visual].
   *
   * Must be set between 0 (full transparent) and 1 (non-transparent / solid). Default: 1.
   */
  var transparency: Double
    get() = transparencyProperty.value
    set(value) {
      transparencyProperty.value = value
    }

  internal val rotationProperty: Property<Double> = Property(rot)

  /**
   * The rotation of this [Visual] in degrees.
   *
   * Must be set between 0 and 360. Default: 0.
   */
  val rotation: Double
    get() = rotationProperty.value

  /**
   * Property for the [style] applied to this [Visual].
   *
   * @see style
   */
  internal val styleProperty: Style = Style()

  /**
   * Additional styling that gets applied to this [Visual].
   *
   * Critical failures, bugs or other undefined behaviour could occur when using this feature.
   *
   * @since 0.10
   */
  val style: Style
    get() = styleProperty

  /**
   * Property for the [filters] applied to this [Visual].
   *
   * @see filters
   */
  internal val filtersProperty: Filter = Filter()

  /**
   * Additional filters that get applied to this [Visual].
   *
   * Critical failures, bugs or other undefined behaviour could occur when using this feature.
   *
   * @since 0.10
   */
  val filters: Filter
    get() = filtersProperty

  /**
   * Property for the [flipped] state of this [Visual].
   *
   * @see flipped
   */
  internal val flippedProperty: Property<Flip> = Property(Flip.NONE)

  /**
   * The [flipped] state of this [Visual].
   *
   * @see Flip
   *
   * @since 0.10
   */
  var flipped: Flip
    get() = flippedProperty.value
    set(value) {
      flippedProperty.value = value
    }
}
