@file:Suppress("unused")

package tools.aqua.bgw.elements.uielements

import tools.aqua.bgw.observable.ObservableArrayList
import tools.aqua.bgw.observable.ObservableList
import tools.aqua.bgw.util.Font

/**
 * A [TableView] may be used to visualize a data table.
 *
 * The items list is used as the data model.
 * The columns list defines how the data is represented.
 * @see TableColumn
 *
 * @param height height for this [TableView]. Default: [TableView.DEFAULT_TABLEVIEW_HEIGHT].
 * @param width width for this [TableView]. Default: [TableView.DEFAULT_TABLEVIEW_WIDTH].
 * @param posX horizontal coordinate for this [TableView]. Default: 0.
 * @param posY vertical coordinate for this [TableView]. Default: 0.
 * @param font the Font for this [TableView]. Default: default [Font] constructor.
 *
 * Simplified example on how the columns list is used to represent the data:
 *
 * items:   1, 2, 3
 * columns: ("first", {x -> x+1}, 10),
 *          ("second", {x -> "nice string " + x}, 14),
 *          ("third", {x -> "" + x*x + "!"}, 10)
 *
 * Representation:
 *
 *  |first     |second        |third     |
 *  |----------|--------------|----------|
 *  |2         |nice string 1 |1!        |
 *  |3         |nice string 2 |4!        |
 *  |4         |nice string 3 |9!        |
 *
 */
open class TableView<T>(
	height: Number = DEFAULT_TABLEVIEW_HEIGHT,
	width: Number = DEFAULT_TABLEVIEW_WIDTH,
	posX: Number = 0,
	posY: Number = 0,
	font: Font = Font()
) : UIElementView(height = height, width = width, posX = posX, posY = posY, font = font) {
	/**
	 * An [ObservableList] that contains the data objects for this [TableView].
	 * The first object in this [ObservableList] will be the topmost row in the rendered [TableView].
	 */
	val items: ObservableList<T> = ObservableArrayList()
	
	/**
	 * An [ObservableList] that contains [TableColumn]s which specify how the data is represented in that column.
	 * The first [TableColumn] in this [ObservableList] will be the leftmost column in the rendered [TableView].
	 * @see TableColumn
	 */
	val columns: ObservableList<TableColumn<T>> = ObservableArrayList()
	
	/**
	 * Defines some static constants that can be used as suggested properties of a [TableView].
	 */
	companion object {
		/**
		 * Suggested [TableView] [height].
		 */
		const val DEFAULT_TABLEVIEW_HEIGHT: Int = 500
		
		/**
		 * Suggested [TableView] [width].
		 */
		const val DEFAULT_TABLEVIEW_WIDTH: Int = 400
	}
}

