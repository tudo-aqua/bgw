package tools.aqua.bgw.net.server
import tools.aqua.bgw.net.server.service.GameService
import tools.aqua.bgw.net.server.entity.Game

/**
 * The interval between consecutive checks for orphaned games in milliseconds.
 * Relevant for [GameService.removeOrphanedGames].
 */
const val ORPHANED_GAME_CHECK_RATE = 20000L

/**
 * The duration a game has to be without any players until it is considered orphaned.
 * Relevant for [Game].
 */
const val TIME_UNTIL_ORPHANED = 60000L