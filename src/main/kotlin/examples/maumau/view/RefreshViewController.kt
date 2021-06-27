package examples.maumau.view

import examples.main.CARDS_FILE
import examples.main.IMG_HEIGHT
import examples.main.IMG_WIDTH
import examples.maumau.model.MauMauCard
import examples.maumau.model.MauMauPlayer
import tools.aqua.bgw.animation.DelayAnimation
import tools.aqua.bgw.animation.FlipAnimation
import tools.aqua.bgw.animation.MovementAnimation
import tools.aqua.bgw.elements.gameelements.CardView
import tools.aqua.bgw.event.EventHandler
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.CompoundVisual
import tools.aqua.bgw.visual.ImageVisual
import java.awt.Color
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

class RefreshViewController(private val viewController: ViewController) : Refreshable {
	
	private val image: BufferedImage = ImageIO.read(this::class.java.classLoader.getResource(CARDS_FILE))
	
	override fun refreshCardDrawn(player: MauMauPlayer, card: MauMauCard) {
		val playerHandView = if (player == viewController.logicController.game.currentPlayer)
			viewController.gameScene.currentPlayerHand
		else
			viewController.gameScene.otherPlayerHand
		
		//transfer card
		val cardView = viewController.cardMap.forward(card)
		viewController.gameScene.drawStackView.removeElement(cardView)
		playerHandView.addElement(cardView)
		
		if (player == viewController.logicController.game.currentPlayer)
			cardView.showFront()
		
		//update label
		viewController.gameScene.drawStackInfo.labelProperty.value =
			viewController.logicController.game.drawStack.size().toString()
		
		//add event handlers
		cardView.addInteraction()
		
		//hide suit selection
		showJackEffectSelection(false)
	}
	
	override fun refreshCardPlayed(card: MauMauCard, animated: Boolean) {
		val cardView = viewController.cardMap.forward(card)
		
		//remove event handlers
		cardView.removeInteraction()
		
		//hide suit selection
		showJackEffectSelection(false)
		
		//animate card
		if (animated) {
			//val anim = MovementAnimation(elementView = cardView, toX = 580.0, toY = -390.0, duration = 500)
			val anim = MovementAnimation.toElementView(
				elementView = cardView,
				toElementViewPosition = viewController.gameScene.gameStackView,
				scene = viewController.gameScene,
				duration = 500
			)
			
			anim.onFinished = EventHandler {
				viewController.gameScene.currentPlayerHand.removeElement(cardView)
				viewController.gameScene.gameStackView.addElement(cardView)
				
				cardView.posX = 0.0
				cardView.posY = 0.0
				
				//update label
				viewController.gameScene.gameStackInfo.labelProperty.value =
					viewController.logicController.game.gameStack.cards.peek().cardSuit.toString()
			}
			
			viewController.gameScene.lock()
			viewController.gameScene.playAnimation(anim)
		} else {
			viewController.gameScene.currentPlayerHand.removeElement(cardView)
			viewController.gameScene.gameStackView.push(cardView)
			
			cardView.posX = 0.0
			cardView.posY = 0.0
			
			//update label
			viewController.gameScene.gameStackInfo.labelProperty.value =
				viewController.logicController.game.gameStack.cards.peek().cardSuit.toString()
		}
	}
	
	override fun refreshGameStackShuffledBack() {
		viewController.gameScene.gameStackView.apply {
			val saved = pop()
			removeAll()
			push(saved)
		}
		
		
		viewController.gameScene.drawStackView.addAllElements(
			viewController.logicController.game.drawStack.cards.map { viewController.cardMap.forward(it) }.onEach {
				it.removeInteraction()
				it.showBack()
			}
		)
	}
	
	override fun refreshPlayAgain() {
		viewController.gameScene.unlock()
	}
	
	override fun showJackEffectSelection() {
		showJackEffectSelection(true)
	}
	
	override fun refreshAll() {
		val game = viewController.logicController.game
		val cardBack = ImageVisual(image.getSubimage(2 * IMG_WIDTH, 4 * IMG_HEIGHT, IMG_WIDTH, IMG_HEIGHT))
		
		//Generate cards
		viewController.cardMap.clear()
		for (card in game.mauMauCards) {
			val cardFront = ImageVisual(
				image.getSubimage(
					card.cardValue.ordinal * IMG_WIDTH,
					card.cardSuit.ordinal * IMG_HEIGHT,
					IMG_WIDTH,
					IMG_HEIGHT
				)
			)
			
			val cardView = CardView(height = 200, width = 130, front = cardFront, back = cardBack)
			cardView.visuals.addAll(
				listOf(
					CompoundVisual(cardFront, ColorVisual(Color.GREEN).apply { transparency = 0.5 }),
					CompoundVisual(cardFront, ColorVisual(Color.RED).apply { transparency = 0.5 })
				)
			)
			
			viewController.cardMap.add(card, cardView)
		}
		
		//Add elements to stacks
		viewController.gameScene.drawStackView.addAllElements(
			game.drawStack.cards.asReversed().map { card ->
				viewController.cardMap.forward(card).apply { showBack() }
			})
		
		viewController.gameScene.gameStackView.addAllElements(
			game.gameStack.cards.asReversed().map { card ->
				viewController.cardMap.forward(card).apply { showFront() }
			})
		
		//update labels
		viewController.gameScene.drawStackInfo.labelProperty.value =
			viewController.logicController.game.drawStack.size().toString()
		
		viewController.gameScene.gameStackInfo.labelProperty.value =
			viewController.logicController.game.gameStack.cards.peek().cardSuit.toString()
		
		//Add elements to hands
		for (i in 0 until game.currentPlayer.hand.cards.size) {
			viewController.gameScene.currentPlayerHand.addElement(
				viewController.cardMap.forward(game.currentPlayer.hand.cards[i]).apply { showFront() }
			)
		}
		for (i in 0 until game.otherPlayer.hand.cards.size) {
			viewController.gameScene.otherPlayerHand.addElement(
				viewController.cardMap.forward(game.otherPlayer.hand.cards[i]).apply { showBack() }
			)
		}
		
		//Add EventHandler for cards on Hand
		viewController.gameScene.currentPlayerHand.elements.forEach { it.addInteraction() }
		viewController.gameScene.otherPlayerHand.elements.forEach { t ->
			t.removeInteraction()
			t.onMouseEntered = { viewController.gameScene.playAnimation(FlipAnimation(t, 1, 0)) }
			t.onMouseExited = { viewController.gameScene.playAnimation(FlipAnimation(t, 0, 1)) }
		}
		
		//hide suit selection
		showJackEffectSelection(false)
	}
	
	override fun refreshAdvancePlayer() {
		val delay = DelayAnimation(1000)
		
		delay.onFinished = EventHandler {
			//swap playerHands
			val tmp = viewController.gameScene.currentPlayerHand
			viewController.gameScene.currentPlayerHand = viewController.gameScene.otherPlayerHand
			viewController.gameScene.otherPlayerHand = tmp
			
			//swap hand positions
			val tmpPosY = viewController.gameScene.currentPlayerHand.posY
			viewController.gameScene.currentPlayerHand.posY = viewController.gameScene.otherPlayerHand.posY
			viewController.gameScene.otherPlayerHand.posY = tmpPosY
			
			//add interaction and show front for all cards in currentPlayerHand
			viewController.gameScene.currentPlayerHand.elements.forEach { t ->
				t.addInteraction()
				t.showFront()
				t.onMouseEntered = null
				t.onMouseExited = null
			}
			
			//add interaction and show back for all cards in otherPlayerHand
			viewController.gameScene.otherPlayerHand.elements.forEach { t ->
				t.removeInteraction()
				t.showBack()
				t.onMouseEntered = {
					viewController.gameScene.playAnimation(FlipAnimation(t, 1, 0))
				}
				t.onMouseExited = {
					viewController.gameScene.playAnimation(FlipAnimation(t, 0, 1))
				}
			}
			
			viewController.gameScene.currentPlayerHand.rotation += 180.0
			viewController.gameScene.otherPlayerHand.rotation += 180.0
			
			viewController.gameScene.unlock()
		}
		
		viewController.gameScene.lock()
		viewController.gameScene.playAnimation(delay)
	}
	
	override fun refreshSuitSelected() {
		viewController.gameScene.gameStackInfo.label = viewController.logicController.game.nextSuit.toString()
		showJackEffectSelection(false)
	}
	
	private fun CardView.addInteraction() {
		/*onMouseEntered = {
			viewController.gameScene.playAnimation(
				MovementAnimation(
					this,
					fromY = 0.0,
					toY = -50.0,
					duration = 500
				)
			)
		}
		onMouseExited = {
			viewController.gameScene.playAnimation(
				MovementAnimation(
					this,
					fromY = -50.0,
					toY = 0.0,
					duration = 500
				)
			)
		}*/
		isDraggable = true
		
		var topCard: CardView? = null
		onDragGestureStarted = {
			topCard = viewController.gameScene.gameStackView.elements.last()
			topCard!!.showVisual(if (viewController.logicController.checkRules(viewController.cardMap.backward(this))) 2 else 3)
		}
		onDragGestureEnded = { _, _ ->
			topCard?.showFront()
		}
		onMouseClicked = { viewController.logicController.playCard(viewController.cardMap.backward(this), true) }
	}
	
	private fun CardView.removeInteraction() {
		isDraggable = false
		onMouseClicked = null
		onMouseEntered = null
		onMouseExited = null
	}
	
	private fun showJackEffectSelection(visible: Boolean) {
		viewController.gameScene.buttonDiamonds.isVisible = visible
		viewController.gameScene.buttonHearts.isVisible = visible
		viewController.gameScene.buttonSpades.isVisible = visible
		viewController.gameScene.buttonClubs.isVisible = visible
	}
}