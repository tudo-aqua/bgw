package examples.concepts.visuals

import tools.aqua.bgw.components.gamecomponentviews.TokenView
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.CompoundVisual
import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.visual.TextVisual

fun main() {
	CompoundVisualsExample()
}

class CompoundVisualsExample : BoardGameApplication("CompoundVisuals example") {
	private val gameScene: BoardGameScene = BoardGameScene(background = ColorVisual.GRAY)
	
	private val token1: TokenView = TokenView(
		posX = 660,
		posY = 400,
		height = 200,
		width = 130,
		visual = CompoundVisual(
			ColorVisual.YELLOW,
			TextVisual(text = "Hint")
		)
	)
	private val token2: TokenView = TokenView(
		posX = 860,
		posY = 400,
		height = 200,
		width = 130,
		visual = CompoundVisual(
			ImageVisual(
				path = "card_deck.png",
				width = 130,
				height = 200,
				offsetX = 260,
				offsetY = 200
			),
			TextVisual(text = "3 of Diamonds")
		)
	)
	private val token3: TokenView = TokenView(
		posX = 1060,
		posY = 400,
		height = 200,
		width = 130,
		visual = CompoundVisual(
			ImageVisual(
				path = "card_deck.png",
				width = 130,
				height = 200,
				offsetX = 260,
				offsetY = 200
			),
			ColorVisual.GREEN.apply { transparency = 0.2 }
		)
	)
	
	init {
		gameScene.addComponents(token1, token2, token3)
		showGameScene(gameScene)
		show()
	}
}