package tools.aqua.bgw.examples.sudoku.view.customcomponents

import tools.aqua.bgw.util.Font
import java.awt.Color

/**
 * Black [Font] for fixed digits.
 */
val blackFont: Font = Font(size = 40, color = Color.BLACK, fontWeight = Font.FontWeight.BOLD)

/**
 * Blue [Font] for regular digits.
 */
val blueFont: Font = Font(size = 40, color = Color.BLUE, fontWeight = Font.FontWeight.BOLD)

/**
 * [Color] for selected cells.
 */
val selectedColor : Color = Color(255, 238, 143)

/**
 * [Color] for errors in cells
 */
val errorColor : Color = Color(255,150,150)

/**
 * [Color] for for finished game
 */
val wonColor : Color = Color(150,255,150)