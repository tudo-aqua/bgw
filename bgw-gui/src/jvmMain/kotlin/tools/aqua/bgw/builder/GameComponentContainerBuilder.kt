package tools.aqua.bgw.builder

import tools.aqua.bgw.components.DynamicComponentView
import tools.aqua.bgw.components.container.*
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.gamecomponentviews.GameComponentView
import tools.aqua.bgw.components.gamecomponentviews.HexagonView
import tools.aqua.bgw.core.Frontend

internal object GameComponentContainerBuilder {
    fun build(gameComponentContainer: GameComponentContainer<out DynamicComponentView>) {
        gameComponentContainer.observableComponents.guiListener = { _, _ -> Frontend.updateComponent(gameComponentContainer) }
        when(gameComponentContainer) {
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
        //TODO: Check for internal listeners
    }

    private fun buildHexagonGrid(hexagonGrid: HexagonGrid<out HexagonView>) {
        //TODO: Check for internal listeners
    }

    private fun buildLinearLayout(linearLayout: LinearLayout<out GameComponentView>) {
        linearLayout.spacingProperty.guiListener = { _, _ -> Frontend.updateComponent(linearLayout) }
        linearLayout.orientationProperty.guiListener = { _, _ -> Frontend.updateComponent(linearLayout) }
        linearLayout.alignmentProperty.guiListener = { _, _ -> Frontend.updateComponent(linearLayout) }
        //TODO: Check for internal listeners
    }

    private fun buildSatchel(satchel: Satchel<out GameComponentView>) {
        //TODO: Check for internal listeners
    }

}
