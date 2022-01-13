package tools.aqua.bgw.net.server.service

import kotlinx.serialization.json.JsonElement
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import tools.aqua.bgw.net.server.entity.JsonSchemaRepository
import kotlin.jvm.Throws

interface ValidationService {
	@Throws(JsonSchemaNotFoundException::class)
	fun validate(payload: JsonElement, gameID: String) : Boolean
}

@Service
class MockValidator(val jsonSchemaRepository: JsonSchemaRepository) : ValidationService {
	override fun validate(payload: JsonElement, gameID: String) : Boolean {
		jsonSchemaRepository.getById(gameID)
		return true
	}
}

class JsonSchemaNotFoundException() : Exception()
