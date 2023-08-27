package tools.aqua.bgw.builder

import AreaData
import GameComponentContainerData
import react.ReactElement
import react.create
import tools.aqua.bgw.elements.container.Area as ReactArea

object ContainerBuilder {
    fun build(containerViewData: GameComponentContainerData): ReactElement<*> {
        return when (containerViewData) {
            is AreaData -> ReactArea.create { data = containerViewData }
            else -> throw IllegalArgumentException("Unknown component type: ${containerViewData::class.simpleName}")
        }
    }
}