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

package tools.aqua.bgw.components.uicomponents

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.StaticComponentView
import tools.aqua.bgw.components.gamecomponentviews.GameComponentView
import tools.aqua.bgw.observable.properties.Property
import tools.aqua.bgw.observable.properties.StringProperty
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.Visual

/**
 * Baseclass for all UI components.
 *
 * @param posX Horizontal coordinate for this [UIComponent].
 * @param posY Vertical coordinate for this [UIComponent].
 * @param width Width for this [UIComponent].
 * @param height Height for this [UIComponent].
 * @param font Font for this [UIComponent]. Usage depends on subclass.
 * @param visual Background [Visual].
 *
 * @see Font
 * @see Visual
 * @see StaticComponentView
 */
abstract class UIComponent(
    posX: Number,
    posY: Number,
    width: Number,
    height: Number,
    font: Font,
    visual: Visual
) :
    StaticComponentView<UIComponent>(
        posX = posX, posY = posY, width = width, height = height, visual = visual) {

  /**
   * [Property] for the [internalCSS].
   *
   * @see internalCSS
   */
  internal var internalCSSProperty: StringProperty = StringProperty("")

  /** Field that is used for internal styling purposes. */
  internal var internalCSS: String
    get() = internalCSSProperty.value
    set(value) {
      internalCSSProperty.value = value
    }

  /**
   * [Property] for the [Font] of this [UIComponent]. Usage depends on subclass.
   *
   * @see Font
   */
  internal val fontProperty: Property<Font> = Property(font)

  /**
   * [Font] of this [UIComponent]. Usage depends on subclass.
   *
   * @see Font
   */
  var font: Font
    get() = fontProperty.value
    set(value) {
      fontProperty.value = value
    }

  /**
   * [Property] for the css style that gets applied to this [UIComponent]'s background.
   *
   * This gets applied last, so it may override any changes made via other fields and functions of
   * this [UIComponent]. Critical failures, bugs or other undefined behaviour could occur when using
   * this feature.
   *
   * @see backgroundStyle
   */
  @Deprecated("The property is no longer used as of BGW 0.10.", ReplaceWith("this.visual.style"))
  internal val backgroundStyleProperty: StringProperty = StringProperty("")

  /**
   * Css style that gets applied to this [UIComponent]'s background.
   *
   * This gets applied last, so it may override any changes made via other fields and functions of
   * this [UIComponent]. Critical failures, bugs or other undefined behaviour could occur when using
   * this feature.
   */
  @Deprecated(
      "CSS Styling is no longer supported as of BGW 0.10.", ReplaceWith("this.visual.style"))
  var backgroundStyle: String
    get() = backgroundStyleProperty.value
    set(value) {
      backgroundStyleProperty.value = value
    }

  /**
   * [Property] for the css style that gets applied to this [UIComponent].
   *
   * This gets applied last, so it may override any changes made via other fields and functions of
   * this [UIComponent]. Critical failures, bugs or other undefined behaviour could occur when using
   * this feature.
   *
   * @see componentStyle
   */
  @Deprecated("The property is no longer used as of BGW 0.10.", ReplaceWith("this.visual.style"))
  internal val componentStyleProperty: StringProperty = StringProperty("")

  /**
   * Css style that gets applied to this [UIComponent].
   *
   * This gets applied last, so it may override any changes made via other fields and functions of
   * this [UIComponent]. Critical failures, bugs or other undefined behaviour could occur when using
   * this feature.
   */
  @Deprecated(
      "CSS Styling is no longer supported as of BGW 0.10.", ReplaceWith("this.visual.style"))
  var componentStyle: String
    get() = componentStyleProperty.value
    set(value) {
      componentStyleProperty.value = value
    }

  /** @throws UnsupportedOperationException If [GameComponentView] does not support children. */
  override fun removeChild(component: ComponentView) {
    throw UnsupportedOperationException("This $this ComponentView has no children.")
  }
}
