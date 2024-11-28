package tools.aqua.bgw.event

import data.event.AnimationFinishedEventData
import data.event.EventData

internal interface EventDispatcher {
    fun dispatchEvent(event: AnimationFinishedEventData)
    fun dispatchEvent(event: EventData)
}