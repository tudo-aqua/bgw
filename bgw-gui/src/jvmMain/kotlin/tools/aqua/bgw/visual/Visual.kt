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

@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package tools.aqua.bgw.visual

import IDGenerator

/**
 * Visual baseclass.
 *
 * @constructor Creates a [Visual].
 * @see SingleLayerVisual
 * @see CompoundVisual
 * @see TextVisual
 * @see ImageVisual
 * @see ColorVisual
 * @since 0.1
 */
abstract class Visual {
  internal val id = IDGenerator.generateVisualID()

  /** Copies this [Visual] to a new object. */
  abstract fun copy(): Visual

  companion object {
    /** An empty [Visual]. */
    val EMPTY: Visual = CompoundVisual()
  }
}
