/*
 *    Copyright 2021 The BoardGameWork Authors
 *    SPDX-License-Identifier: Apache-2.0
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package tools.aqua.bgw.builder

import javafx.scene.layout.Pane
import javafx.scene.layout.Region
import tools.aqua.bgw.elements.gameelements.CardView
import tools.aqua.bgw.elements.gameelements.DiceView
import tools.aqua.bgw.elements.gameelements.GameElementView
import tools.aqua.bgw.elements.gameelements.TokenView

/**
 * ElementNodeBuilder.
 * Factory for all BGW elements.
 */
internal class ElementNodeBuilder {
	companion object {
		/**
		 * Switches between GameElements.
		 */
		internal fun buildGameElement(gameElementView: GameElementView): Region =
			when (gameElementView) {
				is CardView ->
					buildCardView(gameElementView)
				is DiceView ->
					buildDiceView(gameElementView)
				is TokenView ->
					buildToken(gameElementView)
			}
		
		/**
		 * Builds [CardView].
		 */
		@Suppress("UNUSED_PARAMETER")
		private fun buildCardView(cardView: CardView): Region = Pane()
		
		/**
		 * Builds [DiceView].
		 */
		@Suppress("UNUSED_PARAMETER")
		private fun buildDiceView(diceView: DiceView): Region = Pane()
		
		/**
		 * Builds [TokenView].
		 */
		@Suppress("UNUSED_PARAMETER")
		private fun buildToken(tokenView: TokenView): Region = Pane()
	}
}