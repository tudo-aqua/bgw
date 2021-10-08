package tools.aqua.bgw.examples.sudoku.view.customcomponents

import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.observable.properties.Property
import tools.aqua.bgw.visual.ColorVisual

class SudokuCell(
	val boxIndex : Int,
	val rowIndex : Int,
	val colIndex : Int,
	size: Number
) : Label(
	width = size,
	height = size,
	font = blueFont,
	text = "",
	visual = ColorVisual.WHITE
) {
	
	var isFixed: Boolean = false
	
	private val valueProperty = Property<Int?>(null).apply {
		this.addListener { _, nV ->
			text = nV?.toString() ?: ""
		}
	}
	
	var value: Int?
		get() = valueProperty.value
		set(value) {
			if(isFixed)
				return
			
			valueProperty.value = value
		}
	
	var selectedEvent: ((CellSelectedEvent) -> Unit)? = null
	
	init {
		onMouseClicked = {
			select()
			selectedEvent?.invoke(CellSelectedEvent(this))
		}
	}
	
	fun setFixedValue(value: Int) {
		text = value.toString()
		font = blackFont
		isFixed = true
	}
	
	fun clear() {
		text = ""
		font = blueFont
		isFixed = false
	}
	
	private fun select() {
		visual = ColorVisual(255, 238, 143)
	}
	
	fun deselect() {
		visual = ColorVisual.WHITE
	}
}