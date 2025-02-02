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

import tools.aqua.bgw.components.gamecomponentviews.*
import tools.aqua.bgw.core.Frontend

internal object GameComponentViewBuilder {
  fun build(gameComponentView: GameComponentView) {
    when (gameComponentView) {
      is CardView -> buildCardView(gameComponentView)
      is DiceView -> buildDiceView(gameComponentView)
      is HexagonView -> buildHexagonView(gameComponentView)
      is TokenView -> buildTokenView(gameComponentView)
    }

    gameComponentView.isDraggableProperty.guiListener = { _, _ -> Frontend.updateComponent(gameComponentView) }
  }

  private fun buildCardView(cardView: CardView) {}

  private fun buildDiceView(diceView: DiceView) {
    diceView.visuals.guiListener = { _, _ -> Frontend.updateComponent(diceView) }
  }

  private fun buildHexagonView(hexagonView: HexagonView) {
    hexagonView.sizeProperty.guiListener = { _, _ -> Frontend.updateComponent(hexagonView) }
  }

  private fun buildTokenView(tokenView: TokenView) {}
}
