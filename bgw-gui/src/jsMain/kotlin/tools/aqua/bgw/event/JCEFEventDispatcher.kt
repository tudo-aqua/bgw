package tools.aqua.bgw.event

import data.event.AnimationFinishedEventData
import data.event.EventData
import data.event.KeyEventData
import kotlinx.browser.window
import kotlinx.serialization.encodeToString
import jsonMapper

object JCEFEventDispatcher : EventDispatcher {
    init { initialize() }

    override fun dispatchEvent(event: AnimationFinishedEventData) {
        val json = jsonMapper.encodeToString(event)
        try {
            window.asDynamic().bgwAnimationQuery(Base64.encode(json))
        } catch (e: Throwable) {
            println("Error while dispatching event: $e")
        }
    }

    override fun dispatchEvent(event: EventData) {
        val json = jsonMapper.encodeToString(event)
        try {
            window.asDynamic().bgwQuery(Base64.encode(json))
        } catch (e: Throwable) {
            println("Error while dispatching event: $e")
        }
    }

    private fun initialize() {
        js("window.bgwQuery = function(request) { window.cefQuery({request: request, persistent: false, onSuccess: function (response) {}, onFailure: function (error_code, error_message) {}}) }")
        js("window.bgwAnimationQuery = function(request) { window.cefAnimationQuery({request: request, persistent: false, onSuccess: function (response) {}, onFailure: function (error_code, error_message) {}}) }")
    }
}