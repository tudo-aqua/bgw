package tools.aqua.bgw.event

import data.event.EventData

interface EventDispatcher {
    fun dispatchEvent(event: EventData)
}