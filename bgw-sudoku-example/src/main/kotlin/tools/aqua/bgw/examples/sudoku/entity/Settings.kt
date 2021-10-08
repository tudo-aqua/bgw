package tools.aqua.bgw.examples.sudoku.entity

import tools.aqua.bgw.observable.properties.BooleanProperty

data class Settings(
	var currentDifficulty : Difficulty? = null,
	var showTimer : BooleanProperty = BooleanProperty(false),
	var instantCheck : BooleanProperty = BooleanProperty(false)
)