package tools.aqua.bgw.examples.maumau.net

data class InitGameMessage(
	val drawStack: List<String>,
	val gameStack: List<String>,
	val hostCards: List<String>,
	val yourCards: List<String>,
)