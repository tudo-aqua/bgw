package tools.aqua.bgw.net.server.view.components

import de.f0rce.ace.AceEditor
import de.f0rce.ace.enums.AceMode
import de.f0rce.ace.enums.AceTheme
import tools.aqua.bgw.net.server.view.theme.Theme

class JsonEditor : AceEditor() {

    init {
        matchGlobalTheme()
        Theme.onChange { matchGlobalTheme() }
        mode = AceMode.json
        isReadOnly = true
        value = """
          {
            "schema": "http://json-schema.org/draft-07/schema",
            "type": "object",
            "required": [
              "string",
              "int"
            ],
            "properties": {
              "string": {
                "type": "string"
              },
              "int": {
                "type": "integer"
              }
            },
            "additionalProperties": false
          }
      """.trimIndent()
    }

    private fun matchGlobalTheme() {
        this.theme = when (Theme.getCurrent()) {
            Theme.DARK_MODE -> DARK_THEME
            Theme.LIGHT_MODE -> LIGHT_THEME
            else -> DEFAULT_THEME
        }
    }

    companion object {
        val LIGHT_THEME = AceTheme.crimson_editor
        val DARK_THEME = AceTheme.nord_dark
        val DEFAULT_THEME = DARK_THEME
    }
}