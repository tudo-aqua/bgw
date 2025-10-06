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

@file:Suppress("unused")

internal object IDGenerator {
  private var idCounter = 0
  private var toggleGroupCounter = 0
  private var visualIdCounter = 0
  private var animationIdCounter = 0
  private var dialogIdCounter = 0
  private var sceneIdCounter = 0

  fun generateID(): String = "bgw-id-${idCounter++}"

  fun generateVisualID(): String = "bgw-vis-${visualIdCounter++}"

  fun generateToggleGroupID(): String = "bgw-tg-${toggleGroupCounter++}"

  fun generateAnimationID(): String = "bgw-anim-${animationIdCounter++}"

  fun generateDialogID(): String = "bgw-dialog-${dialogIdCounter++}"

  fun generateSceneID(): String = "bgw-scene-${sceneIdCounter++}"
}
