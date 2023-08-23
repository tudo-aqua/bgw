package tools.aqua.bgw.builder

import Button
import ButtonData
import ComponentView
import ComponentViewData
import Label
import LabelData
import react.ReactElement

object NodeBuilder {
    fun build(componentViewData: ComponentViewData): ReactElement<*> {
        return when (componentViewData) {
            is ButtonData -> ButtonBuilder.build(componentViewData)
            is LabelData -> LabelBuilder.build(componentViewData)
            else -> throw IllegalArgumentException("Unknown component type: ${componentViewData::class.simpleName}")
        }
    }
}