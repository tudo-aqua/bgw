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

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.components.layoutviews.LayoutView
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.core.Frontend

internal object LayoutViewBuilder {
  fun build(layoutView: LayoutView<out ComponentView>) {
    when (layoutView) {
      is GridPane<*> -> buildGrid(layoutView)
      is Pane<*> -> buildPane(layoutView)
    }
  }

  private fun buildGrid(gridPane: GridPane<*>) {
    gridPane.updateGui = { Frontend.updateComponent(gridPane) }
    gridPane
        .mapNotNull { it.component }
        .forEach { component -> ComponentViewBuilder.build(component) }
  }

  private fun buildPane(pane: Pane<*>) {
    pane.observableComponents.guiListener = { a, b ->
      val oldSet = a.toSet()
      val newSet = b.toSet()

      val removed = oldSet - newSet
      val added = newSet - oldSet

      removed.forEach { Frontend.removeComponent(it, pane.id) }

      added.forEach { Frontend.addComponent(it, pane.id) }
    }
    pane.components.forEach { component -> ComponentViewBuilder.build(component) }
  }
}
