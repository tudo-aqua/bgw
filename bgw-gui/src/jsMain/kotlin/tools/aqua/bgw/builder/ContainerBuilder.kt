package tools.aqua.bgw.builder

import AreaData
import CardStackData
import GameComponentContainerData
import LayoutViewData
import LinearLayoutData
import SatchelData
import react.ReactElement
import react.create
import tools.aqua.bgw.elements.container.Area as ReactArea
import tools.aqua.bgw.elements.container.LinearLayout as ReactLinearLayout
import tools.aqua.bgw.elements.container.CardStack as ReactCardStack
import tools.aqua.bgw.elements.container.Satchel as ReactSatchel

internal object ContainerBuilder {
    fun build(containerViewData: GameComponentContainerData): ReactElement<*> {
        return when (containerViewData) {
            is AreaData -> ReactArea.create { data = containerViewData }
            is LinearLayoutData -> ReactLinearLayout.create { data = containerViewData }
            is CardStackData -> ReactCardStack.create { data = containerViewData }
            is SatchelData -> ReactSatchel.create { data = containerViewData }
            else -> throw IllegalArgumentException("Unknown component type: ${containerViewData::class.simpleName}")
        }
    }
}