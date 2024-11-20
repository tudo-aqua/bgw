package tools.aqua.bgw.event

import ComponentViewData
import data.event.KeyEventAction
import tools.aqua.bgw.builder.ReactConverters.toKeyEventData
import tools.aqua.bgw.builder.ReactConverters.toMouseEnteredData
import tools.aqua.bgw.builder.ReactConverters.toMouseEventData
import tools.aqua.bgw.builder.ReactConverters.toMouseExitedData
import web.dom.Element
import react.dom.html.HTMLAttributes

fun HTMLAttributes<Element>.applyCommonEventHandlers(props : ComponentViewData) {
    onContextMenu = {
        it.preventDefault()
        JCEFEventDispatcher.dispatchEvent(it.toMouseEventData(props.id))
    }
    onClick = { JCEFEventDispatcher.dispatchEvent(it.toMouseEventData(props.id)) }
    onKeyDown = { JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(props.id, KeyEventAction.PRESS)) }
    onKeyUp = { JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(props.id, KeyEventAction.RELEASE)) }

    if(props.hasMouseEnteredEvent) {
        onMouseEnter = { JCEFEventDispatcher.dispatchEvent(it.toMouseEnteredData(props.id)) }
    }

    if(props.hasMouseExitedEvent) {
        onMouseLeave = { JCEFEventDispatcher.dispatchEvent(it.toMouseExitedData(props.id)) }
    }
}