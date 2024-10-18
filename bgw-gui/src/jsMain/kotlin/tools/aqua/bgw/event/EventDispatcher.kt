package tools.aqua.bgw.event

import data.event.AnimationFinishedEventData
import data.event.EventData

interface EventDispatcher {
    fun dispatchEvent(event: AnimationFinishedEventData)
    fun dispatchEvent(event: EventData)
}