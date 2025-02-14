/*
 * Copyright 2025 The BoardGameWork Authors
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

package tools.aqua.bgw.style

import tools.aqua.bgw.observable.Observable

/**
 * Interface representing a style declaration.
 *
 * @since 0.10
 */
interface StylingDeclaration {
  /**
   * Converts the declaration to a styling string.
   *
   * @return String representation of the styling (only for internal use).
   */
  fun toValue(): String
}

/**
 * Abstract class representing an observable styling declaration.
 *
 * @since 0.10
 */
abstract class StylingDeclarationObservable : Observable() {
  internal val declarations = mutableMapOf<String, StylingDeclaration>()

  internal fun mapDeclarations(): Map<String, String> {
    return declarations.mapValues { it.value.toValue() }
  }

  internal fun applyDeclarations(observable: StylingDeclarationObservable) {
    declarations.clear()
    declarations.putAll(observable.declarations)
  }
}
