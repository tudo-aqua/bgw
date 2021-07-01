package tools.aqua.bgw.builder

import javafx.scene.layout.Pane
import javafx.scene.layout.Region
import tools.aqua.bgw.elements.gameelements.CardView
import tools.aqua.bgw.elements.gameelements.DiceView
import tools.aqua.bgw.elements.gameelements.TokenView

/**
 * ElementNodeBuilder.
 * Factory for all BGW elements.
 */
internal class ElementNodeBuilder {
	companion object {
		@Suppress("UNUSED_PARAMETER")
		internal fun buildCardView(cardView: CardView): Region = Pane()
		
		@Suppress("UNUSED_PARAMETER")
		internal fun buildDiceView(diceView: DiceView): Region = Pane()
		
		@Suppress("UNUSED_PARAMETER")
		internal fun buildToken(tokenView: TokenView): Region = Pane()
	}
}