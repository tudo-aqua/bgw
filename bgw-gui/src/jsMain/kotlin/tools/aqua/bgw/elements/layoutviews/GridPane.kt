package tools.aqua.bgw.elements.layoutviews

import GridPaneData
import csstype.PropertiesBuilder
import web.cssom.*
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
import web.dom.Element
import web.dom.document
import web.dom.getComputedStyle

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

            if(props.data.layoutFromCenter) {
                useEffect(listOf(props.data)) {
                    document.getElementById(props.data.id)?.let {
                        val rem = getComputedStyle(document.documentElement).fontSize.replace("px", "").toDouble()
                        val element = it
                        val width = element.offsetWidth / rem
                        val height = element.offsetHeight / rem
                        val x = (props.data.posX - width / 2)
                        val y = (props.data.posY - height / 2)
                        element.style.left = "${x}rem"
                        element.style.top = "${y}rem"
                    }
                }
            } else {
                useEffect(listOf(props.data)) {
                    document.getElementById(props.data.id)?.let {
                        it.style.left = "${props.data.posX}rem"
                        it.style.top = "${props.data.posY}rem"
                    }
                }
            }
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
                gap = props.data.spacing.em
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
    }
}
inline val bgwGridPane: IntrinsicType<HTMLAttributes<Element>>
    get() = "bgw_grid_pane".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()