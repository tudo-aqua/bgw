package tools.aqua.bgw.event

import ComponentViewData
import SceneData
import data.event.KeyEventAction
import tools.aqua.bgw.builder.ReactConverters.toKeyEventData
import tools.aqua.bgw.builder.ReactConverters.toMouseEnteredData
import tools.aqua.bgw.builder.ReactConverters.toMouseEventData
import tools.aqua.bgw.builder.ReactConverters.toMouseExitedData
import web.dom.Element
import react.dom.html.HTMLAttributes
import tools.aqua.bgw.builder.ReactConverters.toMousePressedEventData
import tools.aqua.bgw.builder.ReactConverters.toMouseReleasedEventData

internal fun HTMLAttributes<Element>.applyCommonEventHandlers(props : ComponentViewData) {
    onContextMenu = {
        it.preventDefault()
        JCEFEventDispatcher.dispatchEvent(it.toMouseEventData(props.id))
    }
    onClick = { JCEFEventDispatcher.dispatchEvent(it.toMouseEventData(props.id)) }
    onMouseDown = { JCEFEventDispatcher.dispatchEvent(it.toMousePressedEventData(props.id)) }
    onMouseUp = { JCEFEventDispatcher.dispatchEvent(it.toMouseReleasedEventData(props.id)) }

    onAuxClick = { JCEFEventDispatcher.dispatchEvent(it.toMouseEventData(props.id)) }
    onKeyDown = {
        JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(props.id, KeyEventAction.PRESS))
        JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(props.id, KeyEventAction.TYPE))
    }
    onKeyUp = { JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(props.id, KeyEventAction.RELEASE)) }

    if(props.hasMouseEnteredEvent) {
        onMouseEnter = { JCEFEventDispatcher.dispatchEvent(it.toMouseEnteredData(props.id)) }
    }

    if(props.hasMouseExitedEvent) {
        onMouseLeave = { JCEFEventDispatcher.dispatchEvent(it.toMouseExitedData(props.id)) }
    }
}