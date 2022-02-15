package tools.aqua.bgw.net.server
import tools.aqua.bgw.net.server.entity.Game
import tools.aqua.bgw.net.server.service.GameService

/**
 * The interval between consecutive checks for orphaned games in milliseconds.
 * @see [GameService.removeOrphanedGames]

 */
const val ORPHANED_GAME_CHECK_RATE = 20000L

/**
 * The duration a game has to be without any players until it is considered orphaned.
 * @see [GameService.removeOrphanedGames]
 * @see [Game]
 */
const val TIME_UNTIL_ORPHANED = 60000L

/**
 * The String representation of the example game id.
 */
const val EXAMPLE_GAME_ID = "example"

/**
 * The String representation of the MauMau game id.
 */
const val MAUMAU_GAME_ID = "MauMau"

/**
 * The String representation of the URL of the example_schema.json relative to projects resources.
 */
const val EXAMPLE_SCHEMA_JSON_URL_STRING = "/example_schema.json"

/**
 * The String representation of the URL of the maumau_init_schema.json relative to projects resources.
 */
const val MAUMAU_INIT_SCHEMA_JSON_URL_STRING = "/maumau_init_schema.json"

/**
 * The String representation of the URL of the maumau_game_schema.json relative to projects resources.
 */
const val MAUMAU_GAME_SCHEMA_JSON_URL_STRING = "/maumau_game_schema.json"

/**
 * The String representation of the URL of the maumau_end_schema.json relative to projects resources.
 */
const val MAUMAU_END_SCHEMA_JSON_URL_STRING = "/maumau_end_schema.json"

/**
 * The String representation of the URL of the meta_schema.json relative to projects resources.
 */
const val META_SCHEMA_JSON_URL_STRING = "/meta_schema.json"