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
import tools.aqua.bgw.components.gamecomponents.CardView
import tools.aqua.bgw.components.gamecomponents.DiceView
import tools.aqua.bgw.components.gamecomponents.GameComponent
import tools.aqua.bgw.components.gamecomponents.TokenView

/**
 * [ComponentNodeBuilder].
 * Factory for all BGW components.
 */
internal class ComponentNodeBuilder {
	companion object {
		/**
		 * Switches between GameComponents.
		 */
		internal fun buildGameComponent(gameComponent: GameComponent): Region =
			when (gameComponent) {
				is CardView ->
					buildCardView(gameComponent)
				is DiceView ->
					buildDiceView(gameComponent)
				is TokenView ->
					buildToken(gameComponent)
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