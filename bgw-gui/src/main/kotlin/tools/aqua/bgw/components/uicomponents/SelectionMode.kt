package tools.aqua.bgw.components.uicomponents

/** Enum indicating allowed selection mode. */
enum class SelectionMode {
	/** Enum constant indicating that NO selection is possible. */
	NONE,

	/** Enum constant indicating that only a single element may be selected. */
	SINGLE,

	/** Enum constant indicating that any number of selected elements is valid. */
	MULTIPLE
}