package tools.aqua.bgw.builder

import ComponentViewData
import LayoutViewData
import PaneData
import react.ReactElement
import react.create
import tools.aqua.bgw.elements.layoutviews.Pane as ReactPane

object LayoutNodeBuilder {
    fun build(layoutViewData: LayoutViewData): ReactElement<*> {
        return when (layoutViewData) {
            is PaneData -> ReactPane.create { data = layoutViewData }
            else -> throw IllegalArgumentException("Unknown component type: ${LayoutViewData::class.simpleName}")
        }
    }
}