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

package tools.aqua.bgw.builder

import javafx.scene.layout.Pane
import javafx.scene.layout.Region
import javafx.scene.paint.*
import tools.aqua.bgw.components.gamecomponentviews.*

/** [ComponentNodeBuilder]. Factory for all BGW components. */
object ComponentNodeBuilder {
  /** Switches between GameComponents. */
  internal fun buildGameComponent(gameComponentView: GameComponentView): Region =
      when (gameComponentView) {
        is CardView -> buildCardView(gameComponentView)
        is DiceView -> buildDiceView(gameComponentView)
        is TokenView -> buildTokenView(gameComponentView)
        is HexagonView -> buildHexagonView(gameComponentView)
      }

  /** Builds [CardView]. */
  @Suppress("UNUSED_PARAMETER") private fun buildCardView(cardView: CardView): Region = Pane()

  /** Builds [DiceView]. */
  @Suppress("UNUSED_PARAMETER") private fun buildDiceView(diceView: DiceView): Region = Pane()

  /** Builds [TokenView]. */
  @Suppress("UNUSED_PARAMETER") private fun buildTokenView(tokenView: TokenView): Region = Pane()

  /** Builds [HexagonView]. */
  private fun buildHexagonView(hexagonView: HexagonView): Region =
      HexagonBuilder.buildHexagonView(hexagonView)
}
