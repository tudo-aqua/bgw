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

import tools.aqua.bgw.components.StaticComponentView
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.Visual

/**
 * Baseclass for BGW menu scenes. Extend this class in order to create your own menu scene.
 *
 * @param width [Scene] [width] in virtual coordinates. Default: [DEFAULT_SCENE_WIDTH].
 * @param height [Scene] [height] in virtual coordinates. Default: [DEFAULT_SCENE_HEIGHT].
 * @param background [BoardGameScene] [background] [Visual]. Default: [ColorVisual.WHITE].
 *
 * @see Scene
 * @see BoardGameScene
 *
 * @since 0.1
 */
open class MenuScene(
    width: Number = DEFAULT_SCENE_WIDTH,
    height: Number = DEFAULT_SCENE_HEIGHT,
    background: Visual = ColorVisual.WHITE,
    /**
     * The radius of the blur effect applied to the background of this [MenuScene]. Default:
     * [DEFAULT_BLUR_RADIUS].
     *
     * @since 0.10
     */
    val blurRadius: Double = DEFAULT_BLUR_RADIUS,
) :
    Scene<StaticComponentView<out StaticComponentView<*>>>(
        width = width, height = height, background = background) {

  init {
    opacity = DEFAULT_MENU_SCENE_OPACITY
  }
}
