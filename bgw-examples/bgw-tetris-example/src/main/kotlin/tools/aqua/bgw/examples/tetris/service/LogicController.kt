package tools.aqua.bgw.examples.tetris.service


import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.event.KeyCode
import tools.aqua.bgw.examples.tetris.entity.Piece
import tools.aqua.bgw.examples.tetris.entity.Tetris
import tools.aqua.bgw.examples.tetris.entity.Tile
import tools.aqua.bgw.examples.tetris.view.Refreshable
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate


/**
 * Controller managing game actions.
 */
class LogicController(private val view: Refreshable) {
	
	/**
	 * piece generator
	 */
	val generator: PieceGenerator = PieceGenerator()
	
	/**
	 * current tools.aqua.bgw.examples.tetris instance.
	 */
	var tetris: Tetris = Tetris(generator.generate3())
	
	/**
	 * Game timer.
	 */
	val timer: Timer = Timer()
	
	/**
	 * mutex
	 */
	private val mutex: Any = Any()
	
	/**
	 * Running state
	 */
	var running: Boolean = false
	
	/**
	 * Blocked state from down button
	 */
	var blocked : Boolean = false
	
	fun start(speed : Int) {
		synchronized(mutex) {
			if(running)
				return
			
			timer.scheduleAtFixedRate(
				delay = speed.toLong(),
				period = speed.toLong()
			) {
				BoardGameApplication.runOnGUIThread {
					movePiece()
				}
			}
			
			running = true
			view.hideStartInstructions()
		}
	}
	
	fun nextPiece(): Piece = synchronized(mutex) {
		tetris.nextPiece(generator.getRandomPiece()).also { view.refresh(tetris) }
	}
	
	fun navigate(keyCode: KeyCode) {
		synchronized(mutex) {
			when (keyCode) {
				KeyCode.LEFT ->
					if (!TetrisChecker.checkCollision(tetris, 0, -1) && !blocked)
						tetris.left()
				
				KeyCode.RIGHT ->
					if (!TetrisChecker.checkCollision(tetris, 0, 1) && !blocked)
						tetris.right()
				
				KeyCode.DOWN -> {
					while (!TetrisChecker.checkCollision(tetris, 1, 0))
						tetris.movePiece()
					blocked = true
				}
			}
			view.refresh(tetris)
		}
	}
	
	fun movePiece() {
		if (TetrisChecker.checkCollision(tetris, 1, 0))
			fixPiece()
		else
			view.refresh(tetris.movePiece())
	}
	
	fun fixPiece() {
		synchronized(mutex) {
			val piece = tetris.currentPiece
			
			if(tetris.currentYPosition <= 0) {
				loose()
				return
			}
			
			for (y in 0 until piece.height) {
				for (x in 0 until piece.width) {
					piece.tiles[y][x]?.let {
						tetris.tetris[y + tetris.currentYPosition - 1][x + tetris.currentXPosition - 1] = it
					}
				}
			}
			
			clearRows()
			nextPiece()
			blocked = false
		}
	}
	
	fun clearRows() {
		synchronized(mutex) {
			val grid = tetris.tetris
			var row = 19
			var cleared = 0
			
			while (row >= 0) {
				if (TetrisChecker.isRowFull(tetris, row)) {
					for (y in row downTo 0) {
						for (x in 0 until 10) {
							grid[y][x] = if (y == 0) Tile(null) else grid[y - 1][x]
						}
					}
					cleared++
				} else {
					row--
				}
			}
			
			tetris.addPoints(when(cleared) {
				4 -> 1200
				3 -> 300
				2 -> 100
				1 -> 40
				else -> 0
			})
		}
	}
	
	fun loose() {
		running = false
		timer.cancel()
		view.loose()
	}
}