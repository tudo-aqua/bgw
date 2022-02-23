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

package tools.aqua.bgw.observable.properties

/**
 * Property baseclass providing observable fields.
 *
 * @constructor Creates a [Property] with given initial value.
 *
 * @param T Type of boxed data.
 * @param initialValue Initial value of this property.
 */
open class Property<T>(initialValue: T) : ReadonlyProperty<T>(initialValue = initialValue) {
  override var value: T
    get() = super.value
    public set(value) {
      super.value = value
    }
}
