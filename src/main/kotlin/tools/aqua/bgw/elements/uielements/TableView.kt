package tools.aqua.bgw.elements.uielements

import tools.aqua.bgw.observable.ObservableArrayList
import tools.aqua.bgw.observable.ObservableList
import tools.aqua.bgw.util.Font

/**
 * A TableView may be used to visualize a data table.
 *
 * The items list is used as the data model.
 * The columns list defines how the data is represented.
 * @see TableColumn
 *
 * @param height Height for this TableView. Default: 0.
 * @param width Width for this TableView. Default: 0.
 * @param posX Horizontal coordinate for this TableView. Default: 0.
 * @param posY Vertical coordinate for this TableView. Default: 0.
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
	height: Number = 0,
	width: Number = 0,
	posX: Number = 0,
	posY: Number = 0,
	font: Font = Font()
) : UIElementView(height = height, width = width, posX = posX, posY = posY, font = font) {
	/**
	 * An ObservableList that contains the data Objects for this TableView.
	 * The first Object in this ObservableList will be the topmost row in the rendered TableView.
	 */
	val items: ObservableList<T> = ObservableArrayList()
	
	/**
	 * An ObservableList that contains TableColumns which specify how the data is represented in that column.
	 * The first TableColumn in this ObservableList will be the leftmost column in the rendered TableView.
	 * @see TableColumn
	 */
	val columns: ObservableList<TableColumn<T>> = ObservableArrayList()
}

