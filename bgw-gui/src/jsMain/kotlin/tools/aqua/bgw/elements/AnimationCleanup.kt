/*
 * Copyright 2025-2026 The BoardGameWork Authors
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

package tools.aqua.bgw.elements

import ComponentViewData
import react.useLayoutEffect
import tools.aqua.bgw.Animator

/**
 * Hook to clean up anime.js animations when a component's animation finishes. This should be called
 * in React components to revert timeline animations when the animationFinishedSinceLastUpdate flag
 * is set.
 */
internal fun useAnimationCleanup(componentData: ComponentViewData) {
  useLayoutEffect(componentData.finishedAnimations) {
    componentData.finishedAnimations.forEach {
      Animator.removeAnimationTypesFromTimeline(componentData.id, it)
    }
  }
}
