package examples.maumau.view

import examples.main.CARDS_FILE
import examples.main.IMG_HEIGHT
import examples.main.IMG_WIDTH
import examples.maumau.entity.MauMauCard
import examples.maumau.entity.MauMauPlayer
import tools.aqua.bgw.animation.DelayAnimation
import tools.aqua.bgw.animation.FlipAnimation
import tools.aqua.bgw.animation.MovementAnimation
import tools.aqua.bgw.components.gamecomponents.Card
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.CompoundVisual
import tools.aqua.bgw.visual.ImageVisual
import java.awt.Color
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

class RefreshViewController(private val viewController: ViewController) : Refreshable {
	
	private val image: BufferedImage = ImageIO.read(this::class.java.classLoader.getResource(CARDS_FILE))
	private var hintOverlay: ColorVisual? = null
	
	override fun refreshCardDrawn(player: MauMauPlayer, card: MauMauCard) {
		val playerHandView = if (player == viewController.logicController.game.currentPlayer)
			viewController.gameScene.currentPlayerHand
		else
			viewController.gameScene.otherPlayerHand
		
		//transfer card
		val cardView = viewController.cardMap.forward(card)
		viewController.gameScene.drawStack.remove(cardView)
		playerHandView.add(cardView)
		
		if (player == viewController.logicController.game.currentPlayer)
			cardView.showFront()
		
		//update label
		viewController.gameScene.drawStackInfo.labelProperty.value =
			viewController.logicController.game.drawStack.size().toString()
		
		//add event handlers
		cardView.addInteraction()
		
		//hide suit selection
		showJackEffectSelection(false)
		
		//Clear overlay
		hintOverlay?.transparency = 0.0
	}
	
	override fun refreshCardPlayed(card: MauMauCard, animated: Boolean) {
		val cardView = viewController.cardMap.forward(card)
		
		//remove event handlers
		cardView.removeInteraction()
		
		//hide suit selection
		showJackEffectSelection(false)
		
		//animate card
		if (animated) {
			val anim = MovementAnimation.toComponentView(
				componentView = cardView,
				toComponentViewPosition = viewController.gameScene.gameStack,
				scene = viewController.gameScene,
				duration = 500
			)
			
			anim.onFinished = {
				viewController.gameScene.currentPlayerHand.remove(cardView)
				viewController.gameScene.gameStack.add(cardView)
				
				cardView.posX = 0.0
				cardView.posY = 0.0
				
				//update label
				viewController.gameScene.gameStackInfo.labelProperty.value =
					viewController.logicController.game.gameStack.cards.peek().cardSuit.toString()
			}
			
			viewController.gameScene.lock()
			viewController.gameScene.playAnimation(anim)
		} else {
			viewController.gameScene.currentPlayerHand.remove(cardView)
			viewController.gameScene.gameStack.push(cardView)
			
			cardView.posX = 0.0
			cardView.posY = 0.0
			
			//update label
			viewController.gameScene.gameStackInfo.labelProperty.value =
				viewController.logicController.game.gameStack.cards.peek().cardSuit.toString()
		}
		
		//Clear overlay
		hintOverlay?.transparency = 0.0
	}
	
	override fun refreshGameStackShuffledBack() {
		viewController.gameScene.gameStack.apply {
			val saved = pop()
			clear()
			push(saved)
		}
		
		
		viewController.gameScene.drawStack.addAll(
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
	
	override fun refreshHintTakeCard() {
		hintOverlay = ((viewController.gameScene.drawStack.components.last().visual as CompoundVisual)
			.children.last() as ColorVisual)
			.apply {
				color = Color.YELLOW
				transparency = 0.5
			}
	}
	
	override fun refreshHintPlayCard(card: MauMauCard) {
		hintOverlay = ((viewController.cardMap.forward(card).visual as CompoundVisual)
			.children.last() as ColorVisual)
			.apply {
				color = Color.YELLOW
				transparency = 0.5
			}
	}
	
	override fun refreshAll() {
		val game = viewController.logicController.game
		val cardBack = ImageVisual(
			image,
			IMG_WIDTH,
			IMG_HEIGHT,
			2 * IMG_WIDTH,
			4 * IMG_HEIGHT
		)
		
		//Generate cards
		viewController.cardMap.clear()
		for (card in game.mauMauCards) {
			val cardFront = ImageVisual(
				image,
				IMG_WIDTH,
				IMG_HEIGHT,
				card.cardValue.ordinal * IMG_WIDTH,
				card.cardSuit.ordinal * IMG_HEIGHT,
			)
			
			val cardView = Card(
				height = 200,
				width = 130,
				front = CompoundVisual(cardFront, ColorVisual.TRANSPARENT),
				back = CompoundVisual(cardBack, ColorVisual.TRANSPARENT)
			).apply { name = card.toString() }
			
			viewController.cardMap.add(card, cardView)
		}
		
		//Add components to stacks
		viewController.gameScene.drawStack.addAll(
			game.drawStack.cards.asReversed().map { card ->
				viewController.cardMap.forward(card).apply { showBack() }
			})
		
		viewController.gameScene.gameStack.addAll(
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
			viewController.gameScene.currentPlayerHand.add(
				viewController.cardMap.forward(game.currentPlayer.hand.cards[i]).apply { showFront() }
			)
		}
		for (i in 0 until game.otherPlayer.hand.cards.size) {
			viewController.gameScene.otherPlayerHand.add(
				viewController.cardMap.forward(game.otherPlayer.hand.cards[i]).apply { showBack() }
			)
		}
		
		//Add EventHandler for cards on Hand
		viewController.gameScene.currentPlayerHand.components.forEach { it.addInteraction() }
		viewController.gameScene.otherPlayerHand.components.forEach { t ->
			t.removeInteraction()
			t.onMouseEntered = { viewController.gameScene.playAnimation(FlipAnimation(t, t.backVisual, t.frontVisual)) }
			t.onMouseExited = { viewController.gameScene.playAnimation(FlipAnimation(t, t.frontVisual, t.backVisual)) }
		}
		
		//hide suit selection
		showJackEffectSelection(false)
	}
	
	override fun refreshAdvancePlayer() {
		val delay = DelayAnimation(1000)
		
		delay.onFinished = {
			//swap playerHands
			val tmp = viewController.gameScene.currentPlayerHand
			viewController.gameScene.currentPlayerHand = viewController.gameScene.otherPlayerHand
			viewController.gameScene.otherPlayerHand = tmp
			
			//swap hand positions
			val tmpPosY = viewController.gameScene.currentPlayerHand.posY
			viewController.gameScene.currentPlayerHand.posY = viewController.gameScene.otherPlayerHand.posY
			viewController.gameScene.otherPlayerHand.posY = tmpPosY
			
			//add interaction and show front for all cards in currentPlayerHand
			viewController.gameScene.currentPlayerHand.components.forEach { t ->
				t.addInteraction()
				t.showFront()
				t.onMouseEntered = null
				t.onMouseExited = null
			}
			
			//add interaction and show back for all cards in otherPlayerHand
			viewController.gameScene.otherPlayerHand.components.forEach { t ->
				t.removeInteraction()
				t.showBack()
				t.onMouseEntered = {
					viewController.gameScene.playAnimation(FlipAnimation(t, t.backVisual, t.frontVisual))
				}
				t.onMouseExited = {
					viewController.gameScene.playAnimation(FlipAnimation(t, t.frontVisual, t.backVisual))
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
	
	private fun Card.addInteraction() {
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
		
		var overlay: ColorVisual? = null
		onDragGestureStarted = {
			overlay =
				((viewController.gameScene.gameStack.components.last().visual as CompoundVisual).children.last() as ColorVisual).apply {
					color = if (viewController.logicController.checkRules(
							viewController.cardMap.backward(this@addInteraction)
						)
					)
						Color.GREEN
					else
						Color.RED
					
					transparency = 0.5
				}
		}
		onDragGestureEnded = { _, _ ->
			overlay?.transparency = 0.0
		}
		onMouseClicked = { viewController.logicController.playCard(viewController.cardMap.backward(this), true) }
	}
	
	private fun Card.removeInteraction() {
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