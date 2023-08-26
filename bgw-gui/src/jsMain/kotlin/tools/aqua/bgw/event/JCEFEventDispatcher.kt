package tools.aqua.bgw.event

import kotlinx.browser.window
import kotlinx.serialization.encodeToString
import jsonMapper
import mapper.EventMapper

object JCEFEventDispatcher : EventDispatcher {
    init { initialize() }
    override fun dispatchEvent(event: Event) {
        val json = jsonMapper.encodeToString(EventMapper.map(event))
        try {
            window.asDynamic().bgwQuery(Base64.encode(json))
        } catch (e: Throwable) {
            println("Error while dispatching event: $e")
        }
    }

    private fun initialize() {
        val script = "window.bgwQuery = function(request) { window.cefQuery({request: request, persistent: false, onSuccess: function (response) {}, onFailure: function (error_code, error_message) {}}) }"
        js(script)
    }
}