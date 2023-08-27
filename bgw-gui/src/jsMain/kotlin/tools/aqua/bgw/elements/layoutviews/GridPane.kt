package tools.aqua.bgw.elements.layoutviews

import GridPaneData
import csstype.ClassName
import csstype.Display
import csstype.PropertiesBuilder
import csstype.fr
import data.event.KeyEventAction
import emotion.react.css
import org.w3c.dom.HTMLDivElement
import react.FC
import react.IntrinsicType
import react.Props
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML.div
import tools.aqua.bgw.builder.NodeBuilder
import tools.aqua.bgw.builder.ReactConverters.toKeyEventData
import tools.aqua.bgw.builder.ReactConverters.toMouseEventData
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.bgwContents
import tools.aqua.bgw.elements.bgwVisuals
import tools.aqua.bgw.elements.cssBuilder
import tools.aqua.bgw.event.JCEFEventDispatcher

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
        }

        bgwVisuals {
            className = ClassName("visuals")
            VisualBuilder.build(props.data.visual).forEach {
                +it
            }
        }

        bgwContents {
            className = ClassName("components")
            css {
                gridTemplateColumns = csstype.repeat(props.data.columns, 1.fr)
                gridTemplateRows = csstype.repeat(props.data.rows, 1.fr)
                display = Display.grid
            }
            props.data.grid.forEach {
                val component = it.component
                if(component == null) {
                    div {}
                } else {
                    +NodeBuilder.build(component)
                }
            }
        }

        onClick = { JCEFEventDispatcher.dispatchEvent(it.toMouseEventData(id)) }
        onKeyDown = { JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(id, KeyEventAction.PRESS)) }
        onKeyUp = { JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(id, KeyEventAction.RELEASE)) }
        onKeyPress = { JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(id, KeyEventAction.TYPE)) }
    }
}

inline val bgwGridPane: IntrinsicType<HTMLAttributes<HTMLDivElement>>
    get() = "bgw_grid_pane".unsafeCast<IntrinsicType<HTMLAttributes<HTMLDivElement>>>()