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

@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package tools.aqua.bgw.visual

import tools.aqua.bgw.observable.Observable
import tools.aqua.bgw.observable.properties.Property
import tools.aqua.bgw.style.*

/**
 * Visual baseclass.
 *
 * @property backgroundRadiusProperty Property for [backgroundRadius].
 * @property borderRadiusProperty Property for [borderRadius].
 * @property borderStyleProperty Property for [borderStyle].
 * @property borderWidthProperty Property for [borderWidth].
 * @property borderColorProperty Property for [borderColor].
 * @property cursorProperty Property for [cursor].
 * @property backgroundRadius Background radius.
 * @property borderRadius Border radius.
 * @property borderStyle Border style.
 * @property borderWidth Border width.
 * @property borderColor Border color.
 * @property cursor Cursor.
 *
 * @constructor Creates a [Visual].
 */
sealed class Visual : Observable() {
  /** Copies this [Visual] to a new object. */
  abstract fun copy(): Visual

  companion object {
    /** An empty [Visual]. */
    val EMPTY: Visual = CompoundVisual()
  }

  var backgroundRadiusProperty: Property<BackgroundRadius?> = Property(null)

  var borderRadiusProperty: Property<BorderRadius?> = Property(null)

  var borderStyleProperty: Property<BorderStyle?> = Property(null)

  var borderWidthProperty: Property<BorderWidth?> = Property(null)

  var borderColorProperty: Property<BorderColor?> = Property(null)

  var cursorProperty: Property<Cursor?> = Property(null)

  /**
   * Background radius.
   *
   * @see BackgroundRadius
   */
  open var backgroundRadius: BackgroundRadius?
    get() = backgroundRadiusProperty.value
    set(value) {
      backgroundRadiusProperty.value = value
    }

  /**
   * Border radius.
   *
   * @see BorderRadius
   */
  open var borderRadius: BorderRadius?
    get() = borderRadiusProperty.value
    set(value) {
      borderRadiusProperty.value = value
    }

  /**
   * Border style.
   *
   * @see BorderStyle
   */
  open var borderStyle: BorderStyle?
    get() = borderStyleProperty.value
    set(value) {
      borderStyleProperty.value = value
    }

  /**
   * Border width.
   *
   * @see BorderWidth
   */
  open var borderWidth: BorderWidth?
    get() = borderWidthProperty.value
    set(value) {
      borderWidthProperty.value = value
    }

  /**
   * Border color.
   *
   * @see BorderColor
   */
  open var borderColor: BorderColor?
    get() = borderColorProperty.value
    set(value) {
      borderColorProperty.value = value
    }

  /**
   * Cursor.
   *
   * @see Cursor
   */
  open var cursor: Cursor?
    get() = cursorProperty.value
    set(value) {
      cursorProperty.value = value
    }
}
