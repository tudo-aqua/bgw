package tools.aqua.bgw.elements.layoutviews

import GridPaneData
import csstype.*
import data.event.KeyEventAction
import emotion.react.css
import org.w3c.dom.HTMLDivElement
import react.*
import react.dom.flushSync
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML.div
import tools.aqua.bgw.builder.NodeBuilder
import tools.aqua.bgw.builder.ReactConverters.toKeyEventData
import tools.aqua.bgw.builder.ReactConverters.toMouseEventData
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.*
import tools.aqua.bgw.event.JCEFEventDispatcher
import tools.aqua.bgw.handlers

external interface GridPaneProps : Props {
    var data : GridPaneData
}

fun PropertiesBuilder.cssBuilderIntern(componentViewData: GridPaneData) {
    cssBuilder(componentViewData)
}

val ReactGridPane = FC<GridPaneProps> { props ->
    bgwGridPane {
        tabIndex = 0
        id = props.data.id
        className = ClassName("gridPane")
        css {
            cssBuilderIntern(props.data)
            width = fit()
            height = fit()
        }

        bgwVisuals {
            className = ClassName("visuals")
            +VisualBuilder.build(props.data.visual)
        }

        bgwContents {
            className = ClassName("components")
            css {
                gridTemplateColumns = repeat(props.data.columns, minContent())
                gridTemplateRows = repeat(props.data.rows, minContent())
                display = Display.grid
                width = fit()
                height = fit()
                gap = props.data.spacing.rem
            }
            props.data.grid.forEach {
                val component = it.component
                if(component == null) {
                    div {}
                } else {
                    +NodeBuilder.build(it)
                }
            }
        }
        onContextMenu = {
            it.preventDefault()
            JCEFEventDispatcher.dispatchEvent(it.toMouseEventData(id)) 
        }
        onClick = { JCEFEventDispatcher.dispatchEvent(it.toMouseEventData(id)) }
        onKeyDown = { JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(id, KeyEventAction.PRESS)) }
        onKeyUp = { JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(id, KeyEventAction.RELEASE)) }
        onKeyPress = { JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(id, KeyEventAction.TYPE)) }
    }
}

inline val bgwGridPane: IntrinsicType<HTMLAttributes<HTMLDivElement>>
    get() = "bgw_grid_pane".unsafeCast<IntrinsicType<HTMLAttributes<HTMLDivElement>>>()