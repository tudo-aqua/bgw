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

package tools.aqua.bgw.core

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.observable.properties.BooleanProperty
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.Visual

/**
 * Baseclass for BGW game scenes.
 *
 * Extend this class in order to create your own game scene.
 *
 * @param width [Scene] [width] in virtual coordinates. Default: [DEFAULT_SCENE_WIDTH].
 * @param height [Scene] [height] in virtual coordinates. Default: [DEFAULT_SCENE_HEIGHT].
 * @param background [BoardGameScene] [background] [Visual]. Default: [ColorVisual.WHITE].
 */
open class BoardGameScene(
    width: Number = DEFAULT_SCENE_WIDTH,
    height: Number = DEFAULT_SCENE_HEIGHT,
    background: Visual = ColorVisual.WHITE
) : Scene<ComponentView>(width = width, height = height, background = background) {

  /**
   * Property that indicates if this [BoardGameScene] is locked from user input.
   *
   * @see lock
   * @see unlock
   */
  internal val lockedProperty: BooleanProperty = BooleanProperty(false)

  /**
   * Property that indicates if this [BoardGameScene] is locked from user input because of menu
   * scene.
   */
  internal val internalLockedProperty: BooleanProperty = BooleanProperty(false)

  /**
   * Locks [Scene] from any user input.
   *
   * @see unlock
   */
  fun lock() {
    lockedProperty.value = true
  }

  /**
   * Unlocks [Scene] for user input.
   *
   * @see lock
   */
  fun unlock() {
    lockedProperty.value = false
  }
}
