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

package tools.aqua.bgw.observable.properties

import tools.aqua.bgw.observable.ValueObservable

/**
 * Property baseclass providing observable fields.
 *
 * @constructor Creates a [ReadonlyProperty] with given initial value.
 *
 * @param T Type of boxed data.
 * @param initialValue Initial value of this property.
 *
 * @see Property
 * @see ReadonlyBooleanProperty
 * @see ReadonlyDoubleProperty
 * @see ReadonlyIntegerProperty
 * @see ReadonlyStringProperty
 *
 * @since 0.3
 */
open class ReadonlyProperty<T>(initialValue: T) : ValueObservable<T>() {

  /** Value of this property. */
  private var boxedValue: T = initialValue

  /** Value of this property. */
  open var value: T
    get() = boxedValue
    internal set(value) {
      val savedValue = boxedValue
      if (boxedValue != value) {
        boxedValue = value
        notifyChange(savedValue, value)
      }
    }

  /**
   * Overrides [value] of this property without notifying listeners.
   */
  internal open fun setSilent(value: T) {
    if (boxedValue != value) {
      boxedValue = value
    }
  }

  internal open fun setInternal(value: T) {
    val savedValue = boxedValue
    if (boxedValue != value) {
      boxedValue = value
      // notifyInternalListener(savedValue, value)
    }
  }

  /** Notifies all listeners with current value. */
  fun notifyUnchanged(): Unit = notifyChange(boxedValue, boxedValue)
}
