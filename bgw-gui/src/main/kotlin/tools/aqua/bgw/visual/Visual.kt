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
 * @constructor Creates a [Visual].
 */
sealed class Visual : Observable() {
  /** Copies this [Visual] to a new object. */
  abstract fun copy(): Visual

  companion object {
    /** An empty [Visual]. */
    val EMPTY: Visual = CompoundVisual()
  }

  open var backgroundRadiusProperty: Property<BackgroundRadius?> = Property(null)
  open var borderRadiusProperty: Property<BorderRadius?> = Property(null)
  open var borderStyleProperty: Property<BorderStyle?> = Property(null)
  open var borderWidthProperty: Property<BorderWidth?> = Property(null)
  open var borderColorProperty: Property<BorderColor?> = Property(null)
  open var cursorProperty: Property<Cursor?> = Property(null)

  open var backgroundRadius: BackgroundRadius?
    get() = backgroundRadiusProperty.value
    set(value) {
      backgroundRadiusProperty.value = value
    }

  open var borderRadius: BorderRadius?
    get() = borderRadiusProperty.value
    set(value) {
      borderRadiusProperty.value = value
    }

  open var borderStyle: BorderStyle?
    get() = borderStyleProperty.value
    set(value) {
      borderStyleProperty.value = value
    }

  open var borderWidth: BorderWidth?
    get() = borderWidthProperty.value
    set(value) {
      borderWidthProperty.value = value
    }

  open var borderColor: BorderColor?
    get() = borderColorProperty.value
    set(value) {
      borderColorProperty.value = value
    }

  open var cursor: Cursor?
    get() = cursorProperty.value
    set(value) {
      cursorProperty.value = value
    }
}
