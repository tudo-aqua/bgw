package tools.aqua.bgw.builder

import Button
import ComponentView
import react.ReactElement

object NodeBuilder {
    fun build(componentView: ComponentView): ReactElement<*> {
        return when (componentView) {
            is Button -> ButtonBuilder.build(componentView)
            else -> throw Exception("Unknown component type")
        }
    }
}