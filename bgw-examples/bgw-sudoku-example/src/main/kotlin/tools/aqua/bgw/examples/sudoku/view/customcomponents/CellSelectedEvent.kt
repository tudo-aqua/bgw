package tools.aqua.bgw.examples.sudoku.view.customcomponents

import tools.aqua.bgw.event.Event

/**
 * Event for cell selection.
 *
 * @param cell Selected [SudokuCell].
 */
class CellSelectedEvent(val cell : SudokuCell) : Event()