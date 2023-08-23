package tools.aqua.bgw.builder

import ButtonData
import ComponentViewData
import LabelData
import LayoutViewData
import react.ReactElement
import react.create
import tools.aqua.bgw.elements.uicomponents.Button as ReactButton
import tools.aqua.bgw.elements.uicomponents.Label as ReactLabel

object NodeBuilder {
    fun build(componentViewData: ComponentViewData): ReactElement<*> {
        return when (componentViewData) {
            is LabelData -> ReactLabel.create { data = componentViewData }
            is ButtonData -> ReactButton.create { data = componentViewData }
            is LayoutViewData -> LayoutNodeBuilder.build(componentViewData)
            else -> throw IllegalArgumentException("Unknown component type: ${componentViewData::class.simpleName}")
        }
    }
}