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

@file:Suppress("DuplicatedCode")

package tools.aqua.bgw.builder

import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.Frontend
import tools.aqua.bgw.core.MenuScene

internal object SceneBuilder {
  fun build(boardGameScene: BoardGameScene) {
    boardGameScene.lockedProperty.guiListener = { _, _ -> Frontend.updateScene() }
    boardGameScene.internalLockedProperty.guiListener = { _, _ -> Frontend.updateScene() }
    boardGameScene.rootComponents.guiListener = { _, _ -> Frontend.updateScene() }
    boardGameScene.components.forEach { ComponentViewBuilder.build(it) }
    boardGameScene.backgroundProperty.guiListener = { _, _ -> Frontend.updateScene() }
    boardGameScene.opacityProperty.guiListener = { _, _ -> Frontend.updateScene() }
  }

  fun build(menuScene: MenuScene) {
    menuScene.rootComponents.guiListener = { _, _ -> Frontend.updateScene() }
    menuScene.components.forEach { ComponentViewBuilder.build(it) }
    menuScene.backgroundProperty.guiListener = { _, _ -> Frontend.updateScene() }
    menuScene.opacityProperty.guiListener = { _, _ -> Frontend.updateScene() }
  }
}
