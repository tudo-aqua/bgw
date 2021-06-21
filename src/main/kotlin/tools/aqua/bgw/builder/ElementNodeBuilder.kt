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
		internal fun buildCardView(cardView: CardView): Region {
			cardView.currentSideProperty.setGUIListenerAndInvoke(cardView.currentSide) {
				cardView.currentVisual = it.visual
			}
			return Pane()
		}
		
		internal fun buildDiceView(diceView: DiceView): Region {
			diceView.currentSideProperty.setGUIListenerAndInvoke(diceView.currentSide) {
				diceView.currentVisual = it - 1
			}
			return Pane()
		}
		
		@Suppress("UNUSED_PARAMETER")
		internal fun buildToken(tokenView: TokenView): Region = Pane()
	}
}