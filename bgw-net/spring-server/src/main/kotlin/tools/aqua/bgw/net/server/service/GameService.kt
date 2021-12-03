package tools.aqua.bgw.net.server.service
//
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.stereotype.Service
//import tools.aqua.bgw.net.common.*
//import tools.aqua.bgw.net.server.entity.Game
//import tools.aqua.bgw.net.server.entity.GameRepository
//import tools.aqua.bgw.net.server.entity.Player
//
//@Service
//class GameService(@Autowired private val gameRepository: GameRepository, @Autowired private val messageService: MessageService) {
//	fun createGame(gameID: String, sessionID: String, password: String, initiator: Player): CreateGameResponseStatus {
//		val responseStatus: CreateGameResponseStatus = when {
//			initiator.game != null -> CreateGameResponseStatus.ALREADY_ASSOCIATED_WITH_GAME
//			gameRepository.existsId(sessionID) -> CreateGameResponseStatus.GAME_WITH_ID_ALREADY_EXISTS
//			else -> {
//				val game = Game(gameID, sessionID, password, initiator)
//				initiator.game = game
//				CreateGameResponseStatus.SUCCESS
//			}
//		}
//		return responseStatus
//	}
//
//	fun joinGame(sessionID: String, password: String, greeting: String, player: Player): JoinGameResponseStatus {
//		if (player.game != null) return JoinGameResponseStatus.ALREADY_ASSOCIATED_WITH_GAME
//
//		lateinit var game: Game
//		try {
//			game = gameRepository.getById(sessionID)
//		} catch (e : NoSuchElementException) {
//			return JoinGameResponseStatus.INVALID_ID
//		}
//
//		if (game.password != password) return JoinGameResponseStatus.INVALID_PASSWORD
//
//		game.addPlayer(player)
//
//		return JoinGameResponseStatus.SUCCESS
//	}
//
//	fun leaveGame(player: Player): DisconnectFromGameResponseStatus {
//		val game = player.game ?: return DisconnectFromGameResponseStatus.NO_ASSOCIATED_GAME
//		game.removePlayer(player)
//		return DisconnectFromGameResponseStatus.SUCCESS
//	}
//
//	@Synchronized
//	private fun Game.addPlayer(player: Player) {
//		player.game = this
//		players.forEach { messageService.sendNotification(it.session, UserJoinedNotification("")) }
//		players.add(player)
//	}
//
//	@Synchronized
//	private fun Game.removePlayer(player: Player) {
//		player.game = null
//		players.remove(player)
//		if (players.isNotEmpty()) {
//			players.forEach { messageService.sendNotification(it.session, UserDisconnectedNotification("")) }
//		} else {
//			gameRepository.remove(this)
//		}
//	}
//}