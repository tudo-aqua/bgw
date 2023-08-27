package tools.aqua.bgw.builder

import AreaData
import GameComponentContainerData
import LayoutViewData
import LinearLayoutData
import react.ReactElement
import react.create
import tools.aqua.bgw.elements.container.Area as ReactArea
import tools.aqua.bgw.elements.container.LinearLayout as ReactLinearLayout

object ContainerBuilder {
    fun build(containerViewData: GameComponentContainerData): ReactElement<*> {
        return when (containerViewData) {
            is AreaData -> ReactArea.create { data = containerViewData }
            is LinearLayoutData -> ReactLinearLayout.create { data = containerViewData }
            else -> throw IllegalArgumentException("Unknown component type: ${containerViewData::class.simpleName}")
        }
    }
}