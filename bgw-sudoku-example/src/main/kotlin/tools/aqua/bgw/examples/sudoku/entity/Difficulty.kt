package tools.aqua.bgw.examples.sudoku.entity

/**
 * Enum class for the three difficulties
 *
 * @param file csv file for sudoku puzzles
 */
enum class Difficulty(val file: String) {
	/**
	 * Easy difficulty
	 */
	EASY("easy.csv"),
	
	/**
	 * Medium difficulty
	 */
	MEDIUM("medium.csv"),
	
	/**
	 * Hard difficulty
	 */
	HARD("hard.csv");
}