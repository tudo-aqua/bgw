package tools.aqua.bgw.elements.gamecomponentviews

import CardViewData
import csstype.ClassName
import csstype.PropertiesBuilder
import data.event.DragEventAction
import data.event.KeyEventAction
import emotion.react.css
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import react.FC
import react.IntrinsicType
import react.Props
import react.dom.html.HTMLAttributes
import tools.aqua.bgw.builder.ReactConverters.toDragEventData
import tools.aqua.bgw.builder.ReactConverters.toKeyEventData
import tools.aqua.bgw.builder.ReactConverters.toMouseEventData
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.bgwVisuals
import tools.aqua.bgw.elements.cssBuilder
import tools.aqua.bgw.event.JCEFEventDispatcher

external interface CardViewProps : Props {
    var data: CardViewData
}

fun PropertiesBuilder.cssBuilderIntern(componentViewData: CardViewData) {
    cssBuilder(componentViewData)
}

var dxCard = 0.0
var dyCard = 0.0

val CardView = FC<CardViewProps> { props ->
    bgwCardView {
        id = props.data.id
        className = ClassName("cardView")
        draggable = props.data.isDraggable
        css {
            cssBuilderIntern(props.data)
        }

        bgwVisuals {
            className = ClassName("visuals")
            +VisualBuilder.build(props.data.currentVisual)
        }

        onContextMenu = {
            it.preventDefault()
            JCEFEventDispatcher.dispatchEvent(it.toMouseEventData(id))
        }
        onClick = { JCEFEventDispatcher.dispatchEvent(it.toMouseEventData(id)) }
        onKeyDown = { JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(id, KeyEventAction.PRESS)) }
        onKeyUp = { JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(id, KeyEventAction.RELEASE)) }
        onKeyPress = { JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(id, KeyEventAction.TYPE)) }
        onDragStart = {
            val rect = it.target.asDynamic().getBoundingClientRect()
            dxCard = it.clientX - rect.x.unsafeCast<Double>()
            dyCard = it.clientY - rect.y.unsafeCast<Double>()
            val element = it.target as HTMLElement
            it.dataTransfer.setData("text", element.id)
            JCEFEventDispatcher.dispatchEvent(it.toDragEventData(id, DragEventAction.START))
        }
        onDragOver = { it.preventDefault() }
        onDragEnd = { it.preventDefault() }
    }
}

inline val bgwCardView: IntrinsicType<HTMLAttributes<HTMLDivElement>>
    get() = "bgw_card_view".unsafeCast<IntrinsicType<HTMLAttributes<HTMLDivElement>>>()