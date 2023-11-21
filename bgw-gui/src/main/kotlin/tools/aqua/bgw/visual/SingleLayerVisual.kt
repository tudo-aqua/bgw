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

import tools.aqua.bgw.observable.properties.LimitedDoubleProperty
import tools.aqua.bgw.observable.properties.Property
import tools.aqua.bgw.observable.properties.StringProperty
import tools.aqua.bgw.style.*

/**
 * Baseclass for single layer visuals.
 *
 * @constructor Creates a [SingleLayerVisual].
 */
sealed class SingleLayerVisual : Visual() {
  /**
   * Property for the [transparency] / alpha channel for this [Visual].
   *
   * Must be set between 0 (full transparent) and 1 (non-transparent / solid). Default: 1.
   *
   * @see transparency
   */
  val transparencyProperty: LimitedDoubleProperty = LimitedDoubleProperty(0, 1, 1)

  /**
   * The [transparency] / alpha channel for this [Visual].
   *
   * Must be set between 0 (full transparent) and 1 (non-transparent / solid). Default: 1.
   *
   * @see transparencyProperty
   */
  var transparency: Double
    get() = transparencyProperty.value
    set(value) {
      transparencyProperty.value = value
    }

  /**
   * [Property] for the css style that gets applied to this [Visual].
   *
   * This gets applied last, so it may override any changes made via other fields and functions of
   * this [Visual]. Critical failures, bugs or other undefined behaviour could occur when using this
   * feature.
   *
   * @see style
   */
  val styleProperty: StringProperty = StringProperty("")

  /**
   * Css style that gets applied to this [Visual].
   *
   * This gets applied last, so it may override any changes made via other fields and functions of
   * this [Visual]. Critical failures, bugs or other undefined behaviour could occur when using this
   * feature.
   *
   * @see styleProperty
   */
  var style: String
    get() = styleProperty.value
    set(value) {
      styleProperty.value = value
    }

  override var backgroundRadiusProperty: Property<BackgroundRadius?>
    get() = super.backgroundRadiusProperty
    set(value) {
      super.backgroundRadiusProperty = value
      style = toCSS()
    }

  override var borderRadiusProperty: Property<BorderRadius?>
    get() = super.borderRadiusProperty
    set(value) {
      super.borderRadiusProperty = value
      style = toCSS()
    }

  override var borderStyleProperty: Property<BorderStyle?>
    get() = super.borderStyleProperty
    set(value) {
      super.borderStyleProperty = value
      style = toCSS()
    }

  override var borderWidthProperty: Property<BorderWidth?>
    get() = super.borderWidthProperty
    set(value) {
      super.borderWidthProperty = value
      style = toCSS()
    }

  override var borderColorProperty: Property<BorderColor?>
    get() = super.borderColorProperty
    set(value) {
      super.borderColorProperty = value
      style = toCSS()
    }

  override var cursorProperty: Property<Cursor?>
    get() = super.cursorProperty
    set(value) {
      super.cursorProperty = value
      style = toCSS()
    }
}
