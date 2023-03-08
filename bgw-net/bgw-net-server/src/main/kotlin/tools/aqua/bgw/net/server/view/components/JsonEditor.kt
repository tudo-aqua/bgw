package tools.aqua.bgw.net.server.view.components

import com.vaadin.flow.shared.Registration
import de.f0rce.ace.AceEditor
import de.f0rce.ace.enums.AceMode
import de.f0rce.ace.enums.AceTheme
import tools.aqua.bgw.net.server.view.theme.Theme

class JsonEditor : AceEditor() {
    private val subscriptions: MutableMap<Registration, ((String) -> Unit)> = mutableMapOf()

    init {
        matchGlobalTheme()
        Theme.onChange { matchGlobalTheme() }
        mode = AceMode.json
    }

    fun setValueSilent(value: String) {
        subscriptions.forEach { (registration, _) -> registration.remove() }
        element.callJsFunction("setValue", value).then {
            val removedSubscriptions = subscriptions.toMap()
            removedSubscriptions.forEach { (registration, callback) ->
                subscriptions.remove(registration)
                addTextListener(callback)
            }
        }
    }

    private fun matchGlobalTheme() {
        this.theme = when (Theme.getCurrent()) {
            Theme.DARK_MODE -> DARK_THEME
            Theme.LIGHT_MODE -> LIGHT_THEME
            else -> DEFAULT_THEME
        }
    }

    fun addTextListener(callback: ((String) -> Unit)) {
        val registration = addAceChangedListener { callback(it.value) }
        subscriptions[registration] = callback
    }

    companion object {
        val LIGHT_THEME = AceTheme.crimson_editor
        val DARK_THEME = AceTheme.nord_dark
        val DEFAULT_THEME = DARK_THEME
    }
}