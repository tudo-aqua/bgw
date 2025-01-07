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

import tools.aqua.bgw.components.DynamicComponentView
import tools.aqua.bgw.components.container.*
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.gamecomponentviews.GameComponentView
import tools.aqua.bgw.components.gamecomponentviews.HexagonView
import tools.aqua.bgw.core.Frontend

internal object GameComponentContainerBuilder {
  fun build(gameComponentContainer: GameComponentContainer<out DynamicComponentView>) {
    gameComponentContainer.observableComponents.guiListener = { _, _ ->
      Frontend.updateComponent(gameComponentContainer)
    }
    when (gameComponentContainer) {
      is Area -> buildArea(gameComponentContainer)
      is CardStack -> buildCardStack(gameComponentContainer)
      is HexagonGrid -> buildHexagonGrid(gameComponentContainer)
      is LinearLayout -> buildLinearLayout(gameComponentContainer)
      is Satchel -> buildSatchel(gameComponentContainer)
    }
    gameComponentContainer.components.forEach { ComponentViewBuilder.build(it) }
  }

  private fun buildArea(area: Area<out GameComponentView>) {}

  private fun buildCardStack(cardStack: CardStack<out CardView>) {
    cardStack.alignmentProperty.guiListener = { _, _ -> Frontend.updateComponent(cardStack) }
  }

  private fun buildHexagonGrid(hexagonGrid: HexagonGrid<out HexagonView>) {}

  private fun buildLinearLayout(linearLayout: LinearLayout<out GameComponentView>) {
    linearLayout.spacingProperty.guiListener = { _, _ -> Frontend.updateComponent(linearLayout) }
    linearLayout.orientationProperty.guiListener = { _, _ ->
      Frontend.updateComponent(linearLayout)
    }
    linearLayout.alignmentProperty.guiListener = { _, _ -> Frontend.updateComponent(linearLayout) }
  }

  private fun buildSatchel(satchel: Satchel<out GameComponentView>) {}
}
