package tools.aqua.bgw.examples.tetris.view

import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.examples.tetris.entity.Color
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual

/**
 * The game scene.
 */
class TetrisGameScene : BoardGameScene(background = ColorVisual.WHITE) {
	
	/**
	 * Cell size constant.
	 */
	private val cellSize = 1000.0 / 22
	
	/**
	 * Label containing start instructions.
	 */
	val startLabel: Label = Label(
		posX = 1920 / 2 - 100,
		posY = 1080 / 2 - 50,
		width = 200,
		height = 100,
		text = "Press ENTER\nto start game.",
		font = Font(size = 30, color = java.awt.Color.WHITE, fontWeight = Font.FontWeight.BOLD),
		alignment = Alignment.CENTER
	)
	
	/**
	 * Label showing the player has lost.
	 */
	val looseLabel: Label = Label(
		posX = 1920 / 2 - 100,
		posY = 1080 / 2 - 50,
		width = 200,
		height = 100,
		text = "You LOST.",
		font = Font(size = 30, color = java.awt.Color.WHITE, fontWeight = Font.FontWeight.BOLD),
		alignment = Alignment.CENTER
	).apply { isVisible = false }
	
	/**
	 * Label displaying current points.
	 */
	val pointsLabel: Label = Label(
		posX = 1250,
		posY = 40,
		text = "0 Points",
		font = Font(size = 20)
	)
	
	/**
	 * Label displaying current speed.
	 */
	val speedLabel: Label = Label(
		posX = 1250,
		posY = 100,
		width = 200,
		font = Font(size = 20)
	)
	
	/**
	 * Preview 1.
	 */
	val preview1: TetrisPreview = TetrisPreview(
		posX = 40 + 1.5 * cellSize,
		posY = 1040 - 2 * cellSize,
		cellSize = cellSize
	)
	
	/**
	 * Preview 2.
	 */
	val preview2: TetrisPreview = TetrisPreview(
		posX = 80 + 4.5 * cellSize,
		posY = 1040 - 2 * cellSize,
		cellSize = cellSize
	)
	
	/**
	 * Preview 3.
	 */
	val preview3: TetrisPreview = TetrisPreview(
		posX = 120 + 7.5 * cellSize,
		posY = 1040 - 2 * cellSize,
		cellSize = cellSize
	)
	
	/**
	 * Tetris grid.
	 */
	val tetrisGrid: GridPane<Label> = GridPane<Label>(
		posX = 1920 / 2,
		posY = 1080 / 2,
		columns = 12,
		rows = 22,
		spacing = 0,
		visual = ColorVisual.BLACK
	).apply {
		for (i in 0 until columns) {
			for (j in 0 until rows) {
				this[i, j] = Label(height = cellSize, width = cellSize)
			}
		}
		
		for (i in 0 until columns) {
			this[i, 0]!!.visual = Color.GRAY.imageVisual.copy()
			this[i, 21]!!.visual = Color.GRAY.imageVisual.copy()
		}
		
		for (i in 1 until rows - 1) {
			this[0, i]!!.visual = Color.GRAY.imageVisual.copy()
			this[11, i]!!.visual = Color.GRAY.imageVisual.copy()
		}
	}
	
	init {
		addComponents(
			preview1,
			preview2,
			preview3,
			tetrisGrid,
			pointsLabel,
			speedLabel,
			startLabel,
			looseLabel,
			Button().apply { opacity = 0.0 }) //Focus capture
	}
}