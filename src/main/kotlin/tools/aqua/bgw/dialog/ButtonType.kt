@file:Suppress("unused")

package tools.aqua.bgw.dialog

/**
 * Enum for all available button types.
 *
 * @param text Displayed button text
 */
enum class ButtonType(private val text: String) {
	/**
	 * A [ButtonType] that displays "Apply".
	 */
	APPLY("APPLY"),
	
	/**
	 * A [ButtonType] that displays "OK".
	 */
	OK("OK"),
	
	/**
	 * A [ButtonType] that displays "Cancel".
	 */
	CANCEL("CANCEL"),
	
	
	/**
	 * A [ButtonType] that displays "Close".
	 */
	CLOSE("CLOSE"),
	
	/**
	 * A [ButtonType] that displays "Yes".
	 */
	YES("YES"),
	
	/**
	 * A [ButtonType] that displays "No".
	 */
	NO("NO"),
	
	/**
	 * A [ButtonType] that displays "Finish".
	 */
	FINISH("FINISH"),
	
	/**
	 * A [ButtonType] that displays "Next".
	 */
	NEXT("NEXT"),
	
	/**
	 * A[ButtonType] that displays "Previous".
	 */
	PREVIOUS("PREVIOUS");
}