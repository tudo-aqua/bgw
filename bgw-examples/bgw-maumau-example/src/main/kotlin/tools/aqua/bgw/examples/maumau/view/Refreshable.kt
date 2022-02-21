package tools.aqua.bgw.examples.maumau.view

import tools.aqua.bgw.examples.maumau.entity.MauMauCard
import tools.aqua.bgw.examples.maumau.entity.MauMauPlayer

interface Refreshable {
	
	/**
	 * Indicates refresh on all components.
	 */
	fun refreshAll()
	
	/**
	 * Refresh on game init received from network.
	 */
	fun onInitializeGameReceived()
	
	/**
	 * Indicates refresh after card was drawn.
	 */
	fun refreshCardDrawn(card: MauMauCard, isCurrentPlayer: Boolean)
	
	/**
	 * Indicates refresh after card was played.
	 */
	fun refreshCardPlayed(card: MauMauCard, animated: Boolean, isCurrentPlayer: Boolean)
	
	/**
	 * Indicates refresh after game stack was shuffled back.
	 */
	fun refreshGameStackShuffledBack()
	
	/**
	 * Indicates refresh after active player has ended turn.
	 */
	fun refreshAdvanceOnlinePlayer()
	
	/**
	 * Indicates swap after active player has changed.
	 */
	fun refreshSwapPlayers()
	
	/**
	 * Indicates refresh when player may take another turn.
	 */
	fun refreshPlayAgain()
	
	/**
	 * Indicates refresh after suit was selected by jack.
	 */
	fun refreshSuitSelected()
	
	/**
	 * Refresh next player.
	 */
	fun refreshEndTurn()
	
	/**
	 * Indicates that jack selection has to be shown.
	 */
	fun showJackEffectSelection()
	
	/**
	 * Indicates to show hint to draw a card.
	 */
	fun refreshHintDrawCard()
	
	/**
	 * Indicates to show hint to play a card.
	 *
	 * @param card Card to play.
	 */
	fun refreshHintPlayCard(card: MauMauCard)
	
	/**
	 * Indicates that the given [playerWon] won the game.
	 *
	 * @param playerWon Player that won.
	 */
	fun refreshEndGame(playerWon: MauMauPlayer)
	
	/**
	 * Shows a warning dialog for wrong inputs in host/join menu.
	 *
	 * @param title Title line.
	 * @param message Message to display.
	 */
	fun showConnectWarningDialog(title:String, message:String)
	
	//region Network join handshake
	/**
	 * Show dialog waiting for opponents.
	 */
	fun onCreateGameSuccess()
	
	/**
	 * Show dialog waiting for host.
	 */
	fun onJoinGameSuccess()
	
	/**
	 * Refresh on user joined.
	 */
	fun onUserJoined(sender: String)
	
	/**
	 * Refresh on user left.
	 */
	fun onUserLeft(sender: String)
	//endregion
	
	//region Network errors
	/**
	 * Show error message from creating a game.
	 */
	fun onCreateGameError(message: String)
	
	/**
	 * Show error message from joining a game.
	 */
	fun onJoinGameError(message: String)
	
	/**
	 * Show generic server error
	 */
	fun onServerError()
	//endregion
}