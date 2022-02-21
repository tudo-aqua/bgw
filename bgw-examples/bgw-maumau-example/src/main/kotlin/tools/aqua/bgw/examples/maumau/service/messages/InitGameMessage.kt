package tools.aqua.bgw.examples.maumau.service.messages

data class InitGameMessage(
	val drawStack: List<String>,
	val gameStack: List<String>,
	val hostCards: List<String>,
	val yourCards: List<String>,
)