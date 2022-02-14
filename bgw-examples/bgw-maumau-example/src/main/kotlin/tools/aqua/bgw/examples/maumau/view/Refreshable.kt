package tools.aqua.bgw.examples.maumau.view

import tools.aqua.bgw.examples.maumau.entity.MauMauCard
import tools.aqua.bgw.examples.maumau.entity.MauMauPlayer

interface Refreshable {
	
	/**
	 * Indicates refresh on all components.
	 */
	fun refreshAll()
	
	/**
	 * Indicates refresh after cards were drawn.
	 */
	fun refreshCardsDrawn(cards: Collection<MauMauCard>, isCurrentPlayer: Boolean) {
		for (card in cards) {
			refreshCardDrawn(card, isCurrentPlayer)
		}
	}
	
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
	 * Indicates refresh after active player has changed.
	 */
	fun refreshAdvancePlayer()
	
	/**
	 * Indicates refresh when player may take another turn.
	 */
	fun refreshPlayAgain()
	
	/**
	 * Indicates refresh after suit was selected by jack.
	 */
	fun refreshSuitSelected()
	
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
	
	fun onCreateGameSuccess()
	fun onJoinGameSuccess()
	
	fun onCreateGameError(message: String)
	fun onJoinGameError(message: String)
	
	fun onUserJoined(sender: String)
	fun onUserLeft(sender: String)
	
	fun onServerError()
	fun onInitializeGameReceived()
	fun refreshEndTurn()
}