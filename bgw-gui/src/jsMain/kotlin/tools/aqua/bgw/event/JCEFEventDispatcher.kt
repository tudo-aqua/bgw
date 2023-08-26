package tools.aqua.bgw.event

import kotlinx.browser.window
import kotlinx.serialization.encodeToString
import jsonMapper
import mapper.EventMapper

object JCEFEventDispatcher : EventDispatcher {
    init {
        val script = "window.bgwQuery = function(request) { window.cefQuery({request: request, persistent: false, onSuccess: function (response) {}, onFailure: function (error_code, error_message) {}}) }"
        js(script)
        println("JCEFEventDispatcher initialized")
    }
    override fun dispatchEvent(event: Event) {
        val json = jsonMapper.encodeToString(EventMapper.map(event))
        when(event) {
            is MouseEvent -> {
                window.asDynamic().bgwQuery(Base64.encode(json))
            }
            is KeyEvent -> {
                window.asDynamic().bgwQuery("bgwKey")
            }
        }
    }
}