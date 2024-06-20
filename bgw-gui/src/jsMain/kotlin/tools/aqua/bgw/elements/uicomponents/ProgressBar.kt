package tools.aqua.bgw.elements.uicomponents

import ComponentViewData
import LabelData
import ProgressBarData
import UIComponentData
import csstype.PropertiesBuilder
import web.cssom.*
import data.event.KeyEventAction
import emotion.react.css
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import react.*
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import tools.aqua.bgw.builder.ReactConverters.toKeyEventData
import tools.aqua.bgw.builder.ReactConverters.toMouseEventData
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.*
import tools.aqua.bgw.event.JCEFEventDispatcher
import tools.aqua.bgw.handlers
import tools.aqua.bgw.internalSocket
import web.dom.Element

external interface ProgressBarProps : Props {
    var data: ProgressBarData
}

fun PropertiesBuilder.cssBuilderIntern(componentViewData: ProgressBarData) {
    cssBuilder(componentViewData)
}

val ProgressBar = FC<ProgressBarProps> { props ->
    bgwProgress {
        tabIndex = 0
        id = props.data.id
        className = ClassName("progress")
        draggable = true
        css {
            cssBuilderIntern(props.data)
        }

        bgwProgressBar {
            id = "${props.data.id}--bar"
            className = ClassName("progress-bar")
            draggable = true
            css {
                width = (props.data.progress * 100).pct
                height = 100.pct
                backgroundColor = Color(props.data.barColor)
                position = Position.absolute
                left = 0.px
                top = 0.px
                transition = transition(500, "width")
            }
        }

        onContextMenu = {
            it.preventDefault()
            JCEFEventDispatcher.dispatchEvent(it.toMouseEventData(id)) 
        }
        onClick = { JCEFEventDispatcher.dispatchEvent(it.toMouseEventData(id)) }
        onKeyDown = { JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(id, KeyEventAction.PRESS)) }
        onKeyUp = { JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(id, KeyEventAction.RELEASE)) }
        onDragStart = {
            println("onDragStart Progress")
            val element = it.target as HTMLElement
            it.dataTransfer.setData("text", element.id)
        }
        onDragOver = { it.preventDefault() }
        onDragEnd = {
            it.preventDefault()
            internalSocket?.send("")
        }
    }
}

inline val bgwProgress: IntrinsicType<HTMLAttributes<Element>>
    get() = "bgw_progress".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()

inline val bgwProgressBar: IntrinsicType<HTMLAttributes<Element>>
    get() = "bgw_progress_bar".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()