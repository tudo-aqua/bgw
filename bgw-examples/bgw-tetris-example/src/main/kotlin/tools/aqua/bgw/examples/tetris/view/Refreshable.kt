package tools.aqua.bgw.examples.tetris.view

import tools.aqua.bgw.examples.tetris.entity.Tetris

interface Refreshable {
	/**
	 * Refreshes tetris grid.
	 *
	 * @param tetris Tetris to display.
	 */
	fun refresh(tetris : Tetris)
	
	/**
	 * Hides start instructions.
	 */
	fun hideStartInstructions()
	
	/**
	 * Shows loose text.
	 */
	fun loose()
	
	/**
	 * Refreshes speed label.
	 *
	 * @param speed New speed to display.
	 */
	fun refreshSpeed(speed: Long)
	
	/**
	 * Refreshes points label.
	 *
	 * @param points New points to display.
	 */
	fun refreshPoints(points: Int)
}