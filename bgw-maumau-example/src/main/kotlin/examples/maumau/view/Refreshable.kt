package examples.maumau.view

import examples.maumau.entity.MauMauCard
import examples.maumau.entity.MauMauPlayer

interface Refreshable {
	
	fun refreshAll()
	
	fun refreshCardsDrawn(player: MauMauPlayer, cards: Collection<MauMauCard>) {
		for (card in cards) {
			refreshCardDrawn(player, card)
		}
	}
	
	fun refreshCardDrawn(player: MauMauPlayer, card: MauMauCard)
	
	fun refreshCardPlayed(card: MauMauCard, animated: Boolean)
	
	fun refreshGameStackShuffledBack()
	
	fun refreshAdvancePlayer()
	
	fun refreshPlayAgain()
	
	fun refreshSuitSelected()
	
	fun showJackEffectSelection()
	
	fun refreshHintTakeCard()
	
	fun refreshHintPlayCard(card: MauMauCard)
}