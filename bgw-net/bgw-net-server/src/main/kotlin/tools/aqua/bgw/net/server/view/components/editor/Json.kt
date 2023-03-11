package tools.aqua.bgw.net.server.view.components.editor

import java.io.InputStream

sealed class Json
class JsonString(val value: String) : Json()
class JsonFile(val inputStream: InputStream) : Json()