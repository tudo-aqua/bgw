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

import tools.aqua.bgw.core.Frontend
import tools.aqua.bgw.visual.*

internal object VisualBuilder {
  fun build(visual: Visual) {
    when (visual) {
      is CompoundVisual -> buildCompoundVisual(visual)
      is ColorVisual -> buildColorVisual(visual)
      is ImageVisual -> buildImageVisual(visual)
      is TextVisual -> buildTextVisual(visual)
    }
  }

  private fun buildCompoundVisual(visual: CompoundVisual) {
    visual.childrenProperty.guiListener = { _, _ -> Frontend.updateVisual(visual) }
    visual.children.forEach { build(it) }
  }

  private fun buildSingleLayerVisual(visual: SingleLayerVisual) {
    visual.transparencyProperty.guiListener = { _, _ -> Frontend.updateVisual(visual) }
    visual.styleProperty.guiListener = { Frontend.updateVisual(visual) }
    visual.filtersProperty.guiListener = { Frontend.updateVisual(visual) }
    visual.flippedProperty.guiListener = { _, _ -> Frontend.updateVisual(visual) }
    when (visual) {
      is ColorVisual -> buildColorVisual(visual)
      is ImageVisual -> buildImageVisual(visual)
      is TextVisual -> buildTextVisual(visual)
    }
  }

  private fun buildColorVisual(visual: ColorVisual) {
    visual.colorProperty.guiListener = { _, _ -> Frontend.updateVisual(visual) }
  }

  private fun buildImageVisual(visual: ImageVisual) {
    visual.pathProperty.guiListener = { _, _ -> Frontend.updateVisual(visual) }
  }

  private fun buildTextVisual(visual: TextVisual) {
    visual.textProperty.guiListener = { _, _ -> Frontend.updateVisual(visual) }
    visual.fontProperty.guiListener = { _, _ -> Frontend.updateVisual(visual) }
    visual.alignmentProperty.guiListener = { _, _ -> Frontend.updateVisual(visual) }
    visual.offsetXProperty.guiListener = { _, _ -> Frontend.updateVisual(visual) }
    visual.offsetYProperty.guiListener = { _, _ -> Frontend.updateVisual(visual) }
  }
}
