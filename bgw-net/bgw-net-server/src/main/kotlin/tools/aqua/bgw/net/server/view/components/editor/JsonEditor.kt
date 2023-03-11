package tools.aqua.bgw.net.server.view.components.editor

import com.fasterxml.jackson.core.exc.StreamReadException
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.json.JsonMapper
import com.vaadin.flow.component.notification.NotificationVariant
import com.vaadin.flow.shared.Registration
import de.f0rce.ace.AceEditor
import de.f0rce.ace.enums.AceMode
import de.f0rce.ace.enums.AceTheme
import tools.aqua.bgw.net.server.DARK_THEME
import tools.aqua.bgw.net.server.LIGHT_THEME
import tools.aqua.bgw.net.server.service.NotificationService
import tools.aqua.bgw.net.server.service.validation.ValidationService
import tools.aqua.bgw.net.server.view.theme.Theme

class JsonEditor(
    private val validationService: ValidationService, private val notificationService: NotificationService
) : AceEditor() {
    private val subscriptions: MutableMap<Registration, ((String) -> Unit)> = mutableMapOf()
    private val mapper = JsonMapper.builder().enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS).build()

    init {
        matchGlobalTheme()
        Theme.onChange { matchGlobalTheme() }
        mode = AceMode.json
        isUseWorker = true
        addTextListener {
            validateSchema(JsonString(it))
        }
    }

    private fun setValueSilent(value: String) {
        subscriptions.forEach { (registration, _) -> registration.remove() }
        element.callJsFunction("setValue", value).then {
            val removedSubscriptions = subscriptions.toMap()
            removedSubscriptions.forEach { (registration, callback) ->
                subscriptions.remove(registration)
                addTextListener(callback)
            }
        }
    }

    fun validateSchema(input: Json): List<String> {
        var results: List<String> = listOf()
        val schemaNode: JsonNode
        try {
            schemaNode = mapper.readTree(input)
            if (input is JsonFile) {
                this.setValueSilent(
                    mapper.writerWithDefaultPrettyPrinter().writeValueAsString(schemaNode)
                )
            }
            results = validationService.validateMetaSchema(schemaNode)
        } catch (e: StreamReadException) {
            if (input is JsonFile) notificationService.notify(
                "Couldn't parse JSON Schema! Please upload a .json File", NotificationVariant.LUMO_ERROR
            )
        }
        return results
    }

    private fun matchGlobalTheme() {
        this.theme = when (Theme.getCurrent()) {
            DARK_THEME -> EDITOR_DARK_THEME
            LIGHT_THEME -> EDITOR_LIGHT_THEME
            else -> EDITOR_DEFAULT_THEME
        }
    }

    private fun addTextListener(callback: ((String) -> Unit)) {
        val registration = addAceChangedListener { callback(it.value) }
        subscriptions[registration] = callback
    }

    companion object {
        val EDITOR_LIGHT_THEME = AceTheme.crimson_editor
        val EDITOR_DARK_THEME = AceTheme.nord_dark
        val EDITOR_DEFAULT_THEME = EDITOR_DARK_THEME
    }
}

private fun ObjectMapper.readTree(input: Json): JsonNode = when (input) {
    is JsonString -> readTree(input)
    is JsonFile -> readTree(input)
}

private fun ObjectMapper.readTree(input: JsonString): JsonNode = this.readTree(input.value)
private fun ObjectMapper.readTree(input: JsonFile): JsonNode = this.readTree(input.inputStream)
