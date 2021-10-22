package tools.aqua.bgw.examples.sudoku.entity

import tools.aqua.bgw.observable.properties.BooleanProperty

/**
 * Settings data class.
 *
 * @param currentDifficulty The current difficulty.
 * @param showTimer Whether to show the timer.
 * @param instantCheck Whether to check the sudoku on every digit entered.
 */
data class Settings(
	var currentDifficulty : Difficulty? = null,
	var showTimer : BooleanProperty = BooleanProperty(false),
	var instantCheck : BooleanProperty = BooleanProperty(false)
)