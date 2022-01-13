package tools.aqua.bgw.net.server.service

import kotlinx.serialization.json.JsonElement
import org.springframework.stereotype.Service
import kotlin.jvm.Throws

interface ValidationService {
	@Throws(JsonSchemaNotFoundException::class)
	fun validate(payload: JsonElement, gameID: String) : Boolean
}

@Service
class MockValidator : ValidationService {
	override fun validate(payload: JsonElement, gameID: String) : Boolean {
		return true
	}
}

class JsonSchemaNotFoundException() : Exception()
