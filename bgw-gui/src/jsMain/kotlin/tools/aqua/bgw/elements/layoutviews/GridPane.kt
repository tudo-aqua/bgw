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
import tools.aqua.bgw.DroppableOptions
import tools.aqua.bgw.DroppableResult
import tools.aqua.bgw.builder.NodeBuilder
import tools.aqua.bgw.builder.ReactConverters.toKeyEventData
import tools.aqua.bgw.builder.ReactConverters.toMouseEnteredData
import tools.aqua.bgw.builder.ReactConverters.toMouseEventData
import tools.aqua.bgw.builder.ReactConverters.toMouseExitedData
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.*
import tools.aqua.bgw.event.JCEFEventDispatcher
import tools.aqua.bgw.event.applyCommonEventHandlers
import tools.aqua.bgw.handlers
import tools.aqua.bgw.useDroppable
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
    val droppable = useDroppable(object : DroppableOptions {
        override var id: String = props.data.id
        override var disabled = !props.data.isDroppable
    })

    val elementRef = useRef<Element>(null)

    bgwGridPane {
        tabIndex = 0
        id = props.data.id
        className = ClassName("gridPane")

        ref = elementRef
        useEffect {
            elementRef.current?.let { droppable.setNodeRef(it) }
        }

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
                        element.style.left = "${x}em"
                        element.style.top = "${y}em"
                    }
                }
            } else {
                useEffect(listOf(props.data)) {
                    document.getElementById(props.data.id)?.let {
                        it.style.left = "${props.data.posX}em"
                        it.style.top = "${props.data.posY}em"
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

        applyCommonEventHandlers(props.data)
    }
}
inline val bgwGridPane: IntrinsicType<HTMLAttributes<Element>>
    get() = "bgw_grid_pane".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()