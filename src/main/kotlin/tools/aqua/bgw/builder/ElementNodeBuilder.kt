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