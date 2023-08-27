package tools.aqua.bgw.builder

import tools.aqua.bgw.components.gamecomponentviews.*

object GameComponentViewBuilder {
    fun build(gameComponentView: GameComponentView) {
        when(gameComponentView) {
            is CardView -> buildCardView(gameComponentView)
            is DiceView -> buildDiceView(gameComponentView)
            is HexagonView -> buildHexagonView(gameComponentView)
            is TokenView -> buildTokenView(gameComponentView)
        }
    }

    private fun buildCardView(cardView: CardView) {}

    private fun buildDiceView(diceView: DiceView) {
        diceView.visuals.guiListener = { _, _ -> Frontend.updateComponent(diceView) }
    }

    private fun buildHexagonView(hexagonView: HexagonView) {}

    private fun buildTokenView(tokenView: TokenView) {}

}
