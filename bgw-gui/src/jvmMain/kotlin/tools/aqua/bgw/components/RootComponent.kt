/*
 * Copyright 2021-2024 The BoardGameWork Authors
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

package tools.aqua.bgw.components

import tools.aqua.bgw.core.Scene
import tools.aqua.bgw.visual.Visual

/**
 * The root component in the view hierarchy of a [Scene].
 *
 * @constructor Creates a [RootComponent].
 *
 * @param T Generic [ComponentView].
 * @property scene Scene of this root component.
 */
class RootComponent<T : ComponentView> internal constructor(val scene: Scene<T>) :
    ComponentView(posX = 0, posY = 0, width = 0, height = 0, visual = Visual.EMPTY),
    LayeredContainer<T> {
  /**
   * Removes [component] from the [scene].
   *
   * @param component Child to be removed.
   *
   * @throws IllegalArgumentException If the child's type is incompatible with scene's type.
   */
  override fun removeChild(component: ComponentView) {
    try {
      @Suppress("UNCHECKED_CAST") this.scene.removeComponents(component as T)
    } catch (_: ClassCastException) {
      throw IllegalArgumentException("$component type is incompatible with scene's type.")
    }
  }

  /**
   * Puts the [component] to the front inside the [LayeredContainer] and Changes its [zIndex]
   * accordingly.
   *
   * @param component Child that is moved to the front.
   */
  override fun toFront(component: T) {
    zIndexProperty.value = this.scene.rootComponents.last().zIndex
    if (this.scene.rootComponents.last() != component &&
        this.scene.rootComponents.contains(component)) {
      this.scene.rootComponents.removeSilent(component)
      this.scene.rootComponents.add(component)
    }
  }

  /**
   * Puts the [component] to the back inside the [LayeredContainer] and Changes its [zIndex]
   * accordingly.
   *
   * @param component Child that is moved to the back.
   */
  override fun toBack(component: T) {
    zIndexProperty.value = this.scene.rootComponents.first().zIndex
    if (this.scene.rootComponents.first() != component &&
        this.scene.rootComponents.contains(component)) {
      this.scene.rootComponents.removeSilent(component)
      this.scene.rootComponents.add(0, component)
    }
  }

  /**
   * Puts the [component] in the appropriate place compared to the other components by the [zIndex]
   * and Changes its [zIndex] accordingly.
   *
   * @param component Child that is moved accordingly.
   * @param zIndex The value that is used to compare the order of components.
   */
  override fun setZIndex(component: T, zIndex: Int) {
    component.zIndexProperty.value = zIndex
    //TODO: Does not modify the list
    this.scene.rootComponents.sortedBy { it.zIndex }
  }
}
