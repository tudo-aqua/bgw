package tools.aqua.bgw.examples.tetris.view

import tools.aqua.bgw.examples.tetris.entity.Tetris

interface Refreshable {
	fun refresh(tetris : Tetris)
	fun hideStartInstructions()
	fun loose()
}