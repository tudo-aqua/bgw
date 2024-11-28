package tools.aqua.bgw.builder

import ComponentViewData
import GridPaneData
import LayoutViewData
import PaneData
import react.ReactElement
import react.create
import tools.aqua.bgw.elements.layoutviews.ReactGridPane
import tools.aqua.bgw.elements.layoutviews.Pane as ReactPane

internal object LayoutNodeBuilder {
    fun build(layoutViewData: LayoutViewData): ReactElement<*> {
        return when (layoutViewData) {
            is PaneData -> ReactPane.create { data = layoutViewData }
            is GridPaneData -> ReactGridPane.create { data = layoutViewData }
            else -> throw IllegalArgumentException("Unknown component type: ${LayoutViewData::class.simpleName}")
        }
    }
}