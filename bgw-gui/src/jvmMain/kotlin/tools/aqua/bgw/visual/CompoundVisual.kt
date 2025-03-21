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

import tools.aqua.bgw.core.Frontend
import tools.aqua.bgw.observable.lists.ObservableArrayList
import tools.aqua.bgw.observable.lists.ObservableList

/**
 * A compound visual containing stacked [SingleLayerVisual]s.
 *
 * Hint: Each [SingleLayerVisual] besides the bottom should have opacity in order to display all
 * layers properly.
 *
 * @constructor Creates a [CompoundVisual] with given children as [List].
 *
 * @param children Children [SingleLayerVisual]s in the order they should be displayed, where the
 * first [SingleLayerVisual] gets displayed at the bottom of the stack.
 *
 * @see Visual
 * @see ColorVisual
 * @see TextVisual
 * @see ImageVisual
 *
 * @since 0.1
 */
open class CompoundVisual(children: List<SingleLayerVisual>) : Visual() {

  internal var updateGui: (() -> Unit)? = null

  /**
   * [ObservableList] for the [children] of this stack. The first [SingleLayerVisual] gets displayed
   * at the bottom of the stack.
   *
   * @see children
   */
  internal val childrenProperty: ObservableArrayList<SingleLayerVisual> =
      ObservableArrayList(children)

  /**
   * The [children] of this stack. The first [SingleLayerVisual] gets displayed at the bottom of the
   * stack.
   */
  var children: List<SingleLayerVisual>
    get() = childrenProperty.toList()
    set(value) {
      childrenProperty.clear()
      childrenProperty.addAll(value)
    }

  init {
    childrenProperty.internalListener = { _, _ -> Frontend.updateVisual(this) }
  }

  /**
   * [CompoundVisual] constructor with vararg parameter for initial children.
   *
   * @param children Children [SingleLayerVisual]s in the order they should be displayed, where the
   * first [SingleLayerVisual] gets displayed at the bottom of the stack.
   *
   * @see Visual
   * @see ColorVisual
   * @see TextVisual
   * @see ImageVisual
   *
   * @since 0.1
   */
  constructor(vararg children: SingleLayerVisual) : this(children.toList())

  /** Copies this [CompoundVisual] to a new object recursively including children. */
  override fun copy(): CompoundVisual =
      CompoundVisual(children.map { it.copy() as SingleLayerVisual }.toList())
}
