/*
 * Copyright 2023 The BoardGameWork Authors
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

package tools.aqua.bgw.components

import tools.aqua.bgw.observable.lists.ObservableList

/** An interface that describes a container which can hold [ComponentView] that can be layered. */
interface LayeredContainer<T : ComponentView> {

  /** an [ObservableList] that is contained in the [LayeredContainer]. */
  val children: ObservableList<T>

  /**
   * Puts the [component] to the front inside the [LayeredContainer].
   *
   * @param component Child that is moved to the front.
   */
  fun toFront(component: T) {
    if (children.last() != component && children.contains(component)) {
      children.removeSilent(component)
      children.add(component)
    }
  }

  /**
   * Puts the [component] to the back inside the [LayeredContainer].
   *
   * @param component Child that is moved to the back.
   */
  fun toBack(component: T) {
    if (children.first() != component && children.contains(component)) {
      children.removeSilent(component)
      children.add(0, component)
    }
  }

  /**
   * Puts the [component] in the appropriate place compared to the other [children] by the [zIndex].
   *
   * @param component Child that is moved accordingly.
   * @param zIndex The value that is used to compare the order of [children].
   */
  fun setZIndex(component: T, zIndex: Int) {
    component.zIndex = zIndex
    children.sort(Comparator.comparingInt { zIndex })
  }
}
