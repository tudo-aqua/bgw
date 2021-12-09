package tools.aqua.bgw.net.server.service

import kotlinx.serialization.json.JsonElement
import org.springframework.stereotype.Service

interface ValidationService {
	fun validate(payload: JsonElement, gameID: String) : Boolean
}

@Service
class MockValidator : ValidationService {
	override fun validate(payload: JsonElement, gameID: String) : Boolean {
		return true
	}
}

