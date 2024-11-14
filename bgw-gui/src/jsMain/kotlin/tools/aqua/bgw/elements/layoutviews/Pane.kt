package tools.aqua.bgw.elements.layoutviews

import ComponentViewData
import LayoutViewData
import PaneData
import csstype.PropertiesBuilder
import web.cssom.*
import data.event.DragEventAction
import data.event.KeyEventAction
import emotion.react.css
import kotlinx.browser.document
import kotlinx.dom.appendElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.Node
import react.*
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.div
import tools.aqua.bgw.DroppableOptions
import tools.aqua.bgw.DroppableResult
import tools.aqua.bgw.builder.NodeBuilder
import tools.aqua.bgw.builder.ReactConverters.toDragEventData
import tools.aqua.bgw.builder.ReactConverters.toKeyEventData
import tools.aqua.bgw.builder.ReactConverters.toMouseEventData
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.bgwContents
import tools.aqua.bgw.elements.bgwVisuals
import tools.aqua.bgw.elements.cssBuilder
import tools.aqua.bgw.elements.gamecomponentviews.bgwTokenView
import tools.aqua.bgw.elements.gamecomponentviews.cssBuilderIntern
import tools.aqua.bgw.event.JCEFEventDispatcher
import tools.aqua.bgw.handlers
import tools.aqua.bgw.useDroppable
import web.dom.Element

external interface PaneProps : Props {
    var data: PaneData
}

fun PropertiesBuilder.cssBuilderIntern(componentViewData: PaneData) {
    cssBuilder(componentViewData)
}

val Pane = FC<PaneProps> { props ->
    var droppable : DroppableResult? = null

    if(props.data.isDroppable) {
        droppable = useDroppable(object : DroppableOptions {
            override var id: String = props.data.id
        })
    }

    val elementRef = useRef<Element>(null)

    bgwPane {
        tabIndex = 0
        id = props.data.id
        className = ClassName("pane")
        css {
            cssBuilderIntern(props.data)
        }

        if(props.data.isDroppable) {
            ref = elementRef
            useEffect {
                elementRef.current?.let { droppable!!.setNodeRef(it) }
            }
        }

        bgwVisuals {
            className = ClassName("visuals")
            +VisualBuilder.build(props.data.visual)
        }

        bgwContents {
            className = ClassName("components")
            props.data.components.forEach {
                +NodeBuilder.build(it)
            }
        }

        onContextMenu = {
            it.preventDefault()
            JCEFEventDispatcher.dispatchEvent(it.toMouseEventData(id))
        }
        onClick = { JCEFEventDispatcher.dispatchEvent(it.toMouseEventData(id)) }
        onKeyDown = { JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(id, KeyEventAction.PRESS)) }
        onKeyUp = { JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(id, KeyEventAction.RELEASE)) }
        onDragOver = { it.preventDefault() }
    }
}

inline val bgwPane: IntrinsicType<HTMLAttributes<Element>>
    get() = "bgw_pane".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()