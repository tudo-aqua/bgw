package tools.aqua.bgw.elements.gamecomponentviews

import TokenViewData
import csstype.PropertiesBuilder
import web.cssom.*
import data.event.DragEventAction
import data.event.KeyEventAction
import emotion.react.css
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import react.*
import react.dom.html.HTMLAttributes
import tools.aqua.bgw.builder.ReactConverters.toDragEventData
import tools.aqua.bgw.builder.ReactConverters.toKeyEventData
import tools.aqua.bgw.builder.ReactConverters.toMouseEventData
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.bgwText
import tools.aqua.bgw.elements.bgwVisuals
import tools.aqua.bgw.elements.cssBuilder
import tools.aqua.bgw.event.JCEFEventDispatcher
import tools.aqua.bgw.handlers
import web.dom.Element

external interface TokenViewProps : Props {
    var data: TokenViewData
}

fun PropertiesBuilder.cssBuilderIntern(componentViewData: TokenViewData) {
    cssBuilder(componentViewData)
}

var dxToken = 0.0
var dyToken = 0.0

val TokenView = FC<TokenViewProps> { props ->
    bgwTokenView {
        id = props.data.id
        className = ClassName("tokenView")
        draggable = props.data.isDraggable
        css {
            cssBuilderIntern(props.data)
        }

        bgwVisuals {
            className = ClassName("visuals")
            +VisualBuilder.build(props.data.visual)
        }

        onContextMenu = {
            it.preventDefault()
            JCEFEventDispatcher.dispatchEvent(it.toMouseEventData(id)) 
        }
        onClick = { JCEFEventDispatcher.dispatchEvent(it.toMouseEventData(id)) }
        onKeyDown = { JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(id, KeyEventAction.PRESS)) }
        onKeyUp = { JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(id, KeyEventAction.RELEASE)) }
        onDragStart = {
            val rect = it.target.asDynamic().getBoundingClientRect()
            dxToken = it.clientX - rect.x.unsafeCast<Double>()
            dyToken = it.clientY - rect.y.unsafeCast<Double>()
            val element = it.target as HTMLElement
            it.dataTransfer.setData("text", element.id)
            JCEFEventDispatcher.dispatchEvent(it.toDragEventData(id, DragEventAction.START))
        }
        onDragOver = { it.preventDefault() }
        onDragEnd = { it.preventDefault() }
    }
}

inline val bgwTokenView: IntrinsicType<HTMLAttributes<Element>>
    get() = "bgw_token_view".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()