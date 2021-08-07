package examples.maumau.view

import examples.main.CARDS_FILE
import examples.main.IMG_HEIGHT
import examples.main.IMG_WIDTH
import examples.maumau.entity.MauMauCard
import examples.maumau.entity.MauMauPlayer
import tools.aqua.bgw.animation.DelayAnimation
import tools.aqua.bgw.animation.FlipAnimation
import tools.aqua.bgw.animation.MovementAnimation
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.CompoundVisual
import tools.aqua.bgw.visual.ImageVisual
import java.awt.Color
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

class RefreshViewController(private val mauMauViewController: MauMauViewController) : Refreshable {
	
	private val image: BufferedImage = ImageIO.read(this::class.java.classLoader.getResource(CARDS_FILE))
	private var hintOverlay: ColorVisual? = null
	
	override fun refreshCardDrawn(player: MauMauPlayer, card: MauMauCard) {
		val playerHandView = if (player == mauMauViewController.logicController.game.currentPlayer)
			mauMauViewController.mauMauGameScene.currentPlayerHand
		else
			mauMauViewController.mauMauGameScene.otherPlayerHand
		
		//transfer card
		val cardView = mauMauViewController.cardMap.forward(card)
		mauMauViewController.mauMauGameScene.drawStack.remove(cardView)
		playerHandView.add(cardView)
		
		if (player == mauMauViewController.logicController.game.currentPlayer)
			cardView.showFront()
		
		//update label
		mauMauViewController.mauMauGameScene.drawStackInfo.labelProperty.value =
			mauMauViewController.logicController.game.drawStack.size().toString()
		
		//add event handlers
		cardView.addInteraction()
		
		//hide suit selection
		showJackEffectSelection(false)
		
		//Clear overlay
		hintOverlay?.transparency = 0.0
	}
	
	override fun refreshCardPlayed(card: MauMauCard, animated: Boolean) {
		val cardView = mauMauViewController.cardMap.forward(card)
		
		//remove event handlers
		cardView.removeInteraction()
		
		//hide suit selection
		showJackEffectSelection(false)
		
		//animate card
		if (animated) {
			val anim = MovementAnimation.toComponentView(
				componentView = cardView,
				toComponentViewPosition = mauMauViewController.mauMauGameScene.gameStack,
				scene = mauMauViewController.mauMauGameScene,
				duration = 500
			)
			
			anim.onFinished = {
				mauMauViewController.mauMauGameScene.currentPlayerHand.remove(cardView)
				mauMauViewController.mauMauGameScene.gameStack.add(cardView)
				
				cardView.posX = 0.0
				cardView.posY = 0.0
				
				//update label
				mauMauViewController.mauMauGameScene.gameStackInfo.labelProperty.value =
					mauMauViewController.logicController.game.gameStack.cards.peek().cardSuit.toString()
			}
			
			mauMauViewController.mauMauGameScene.lock()
			mauMauViewController.mauMauGameScene.playAnimation(anim)
		} else {
			mauMauViewController.mauMauGameScene.currentPlayerHand.remove(cardView)
			mauMauViewController.mauMauGameScene.gameStack.push(cardView)
			
			cardView.posX = 0.0
			cardView.posY = 0.0
			
			//update label
			mauMauViewController.mauMauGameScene.gameStackInfo.labelProperty.value =
				mauMauViewController.logicController.game.gameStack.cards.peek().cardSuit.toString()
		}
		
		//Clear overlay
		hintOverlay?.transparency = 0.0
	}
	
	override fun refreshGameStackShuffledBack() {
		mauMauViewController.mauMauGameScene.gameStack.apply {
			val saved = pop()
			clear()
			push(saved)
		}
		
		
		mauMauViewController.mauMauGameScene.drawStack.addAll(
			mauMauViewController.logicController.game.drawStack.cards.map { mauMauViewController.cardMap.forward(it) }.onEach {
				it.removeInteraction()
				it.showBack()
			}
		)
	}
	
	override fun refreshPlayAgain() {
		mauMauViewController.mauMauGameScene.unlock()
	}
	
	override fun showJackEffectSelection() {
		showJackEffectSelection(true)
	}
	
	override fun refreshHintTakeCard() {
		hintOverlay = ((mauMauViewController.mauMauGameScene.drawStack.components.last().visual as CompoundVisual)
			.children.last() as ColorVisual)
			.apply {
				color = Color.YELLOW
				transparency = 0.5
			}
	}
	
	override fun refreshHintPlayCard(card: MauMauCard) {
		hintOverlay = ((mauMauViewController.cardMap.forward(card).visual as CompoundVisual)
			.children.last() as ColorVisual)
			.apply {
				color = Color.YELLOW
				transparency = 0.5
			}
	}
	
	override fun refreshAll() {
		val game = mauMauViewController.logicController.game
		val cardBack = ImageVisual(
			image,
			IMG_WIDTH,
			IMG_HEIGHT,
			2 * IMG_WIDTH,
			4 * IMG_HEIGHT
		)
		
		//Generate cards
		mauMauViewController.cardMap.clear()
		for (card in game.mauMauCards) {
			val cardFront = ImageVisual(
				image,
				IMG_WIDTH,
				IMG_HEIGHT,
				card.cardValue.ordinal * IMG_WIDTH,
				card.cardSuit.ordinal * IMG_HEIGHT,
			)
			
			val cardView = CardView(
				height = 200,
				width = 130,
				front = CompoundVisual(cardFront, ColorVisual.TRANSPARENT),
				back = CompoundVisual(cardBack, ColorVisual.TRANSPARENT)
			).apply { name = card.toString() }
			
			mauMauViewController.cardMap.add(card, cardView)
		}
		
		//Add components to stacks
		mauMauViewController.mauMauGameScene.drawStack.addAll(
			game.drawStack.cards.asReversed().map { card ->
				mauMauViewController.cardMap.forward(card).apply { showBack() }
			})
		
		mauMauViewController.mauMauGameScene.gameStack.addAll(
			game.gameStack.cards.asReversed().map { card ->
				mauMauViewController.cardMap.forward(card).apply { showFront() }
			})
		
		//update labels
		mauMauViewController.mauMauGameScene.drawStackInfo.labelProperty.value =
			mauMauViewController.logicController.game.drawStack.size().toString()
		
		mauMauViewController.mauMauGameScene.gameStackInfo.labelProperty.value =
			mauMauViewController.logicController.game.gameStack.cards.peek().cardSuit.toString()
		
		//Add elements to hands
		for (i in 0 until game.currentPlayer.hand.cards.size) {
			mauMauViewController.mauMauGameScene.currentPlayerHand.add(
				mauMauViewController.cardMap.forward(game.currentPlayer.hand.cards[i]).apply { showFront() }
			)
		}
		for (i in 0 until game.otherPlayer.hand.cards.size) {
			mauMauViewController.mauMauGameScene.otherPlayerHand.add(
				mauMauViewController.cardMap.forward(game.otherPlayer.hand.cards[i]).apply { showBack() }
			)
		}
		
		//Add EventHandler for cards on Hand
		mauMauViewController.mauMauGameScene.currentPlayerHand.components.forEach { it.addInteraction() }
		mauMauViewController.mauMauGameScene.otherPlayerHand.components.forEach { t ->
			t.removeInteraction()
			t.onMouseEntered = { mauMauViewController.mauMauGameScene.playAnimation(FlipAnimation(t, t.backVisual, t.frontVisual)) }
			t.onMouseExited = { mauMauViewController.mauMauGameScene.playAnimation(FlipAnimation(t, t.frontVisual, t.backVisual)) }
		}
		
		//hide suit selection
		showJackEffectSelection(false)
	}
	
	override fun refreshAdvancePlayer() {
		val delay = DelayAnimation(1000)
		
		delay.onFinished = {
			//swap playerHands
			val tmp = mauMauViewController.mauMauGameScene.currentPlayerHand
			mauMauViewController.mauMauGameScene.currentPlayerHand = mauMauViewController.mauMauGameScene.otherPlayerHand
			mauMauViewController.mauMauGameScene.otherPlayerHand = tmp
			
			//swap hand positions
			val tmpPosY = mauMauViewController.mauMauGameScene.currentPlayerHand.posY
			mauMauViewController.mauMauGameScene.currentPlayerHand.posY = mauMauViewController.mauMauGameScene.otherPlayerHand.posY
			mauMauViewController.mauMauGameScene.otherPlayerHand.posY = tmpPosY
			
			//add interaction and show front for all cards in currentPlayerHand
			mauMauViewController.mauMauGameScene.currentPlayerHand.components.forEach { t ->
				t.addInteraction()
				t.showFront()
				t.onMouseEntered = null
				t.onMouseExited = null
			}
			
			//add interaction and show back for all cards in otherPlayerHand
			mauMauViewController.mauMauGameScene.otherPlayerHand.components.forEach { t ->
				t.removeInteraction()
				t.showBack()
				t.onMouseEntered = {
					mauMauViewController.mauMauGameScene.playAnimation(FlipAnimation(t, t.backVisual, t.frontVisual))
				}
				t.onMouseExited = {
					mauMauViewController.mauMauGameScene.playAnimation(FlipAnimation(t, t.frontVisual, t.backVisual))
				}
			}
			
			mauMauViewController.mauMauGameScene.currentPlayerHand.rotation += 180.0
			mauMauViewController.mauMauGameScene.otherPlayerHand.rotation += 180.0
			
			mauMauViewController.mauMauGameScene.unlock()
		}
		
		mauMauViewController.mauMauGameScene.lock()
		mauMauViewController.mauMauGameScene.playAnimation(delay)
	}
	
	override fun refreshSuitSelected() {
		mauMauViewController.mauMauGameScene.gameStackInfo.label = mauMauViewController.logicController.game.nextSuit.toString()
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
		
		var overlay: ColorVisual? = null
		onDragGestureStarted = {
			overlay =
				((mauMauViewController.mauMauGameScene.gameStack.components.last().visual as CompoundVisual).children.last() as ColorVisual).apply {
					color = if (mauMauViewController.logicController.checkRules(
							mauMauViewController.cardMap.backward(this@addInteraction)
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
		onMouseClicked = { mauMauViewController.logicController.playCard(mauMauViewController.cardMap.backward(this), true) }
	}
	
	private fun CardView.removeInteraction() {
		isDraggable = false
		onMouseClicked = null
		onMouseEntered = null
		onMouseExited = null
	}
	
	private fun showJackEffectSelection(visible: Boolean) {
		mauMauViewController.mauMauGameScene.buttonDiamonds.isVisible = visible
		mauMauViewController.mauMauGameScene.buttonHearts.isVisible = visible
		mauMauViewController.mauMauGameScene.buttonSpades.isVisible = visible
		mauMauViewController.mauMauGameScene.buttonClubs.isVisible = visible
	}
}