package tools.aqua.bgw.elements.uielements

/**
 * A TableColumn may be used to represent a column in a TableView.
 * @see TableView
 */
data class TableColumn<T>(
	/**
	 * The title of this TableColumn.
	 * It gets rendered in the header row of the TableView.
	 * @see TableView
	 */
	val title: String,
	
	/**
	 * The width for this TableColumn.
	 */
	val width: Number,
	
	/**
	 * The format function for this TableColumn.
	 * It gets applied to each item in the TableView to get a String for the column.
	 * @see TableView
	 */
	val formatFunction: (T) -> String,
)