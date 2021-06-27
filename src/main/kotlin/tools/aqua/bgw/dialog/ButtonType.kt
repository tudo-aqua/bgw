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
	
	/**
	 * Converts ButtonType enum value to JavaFX ButtonType constant.
	 */
	internal fun toButtonType(): javafx.scene.control.ButtonType = when (this) {
		APPLY -> javafx.scene.control.ButtonType.APPLY
		OK -> javafx.scene.control.ButtonType.OK
		CANCEL -> javafx.scene.control.ButtonType.CANCEL
		CLOSE -> javafx.scene.control.ButtonType.CLOSE
		YES -> javafx.scene.control.ButtonType.YES
		NO -> javafx.scene.control.ButtonType.NO
		FINISH -> javafx.scene.control.ButtonType.FINISH
		NEXT -> javafx.scene.control.ButtonType.NEXT
		PREVIOUS -> javafx.scene.control.ButtonType.PREVIOUS
	}
	
	companion object {
		/**
		 * Converts JavaFX ButtonType constant to ButtonType enum value.
		 */
		fun fromButtonType(type: javafx.scene.control.ButtonType): ButtonType = when (type) {
			javafx.scene.control.ButtonType.APPLY -> APPLY
			javafx.scene.control.ButtonType.OK -> OK
			javafx.scene.control.ButtonType.CANCEL -> CANCEL
			javafx.scene.control.ButtonType.CLOSE -> CLOSE
			javafx.scene.control.ButtonType.YES -> YES
			javafx.scene.control.ButtonType.NO -> NO
			javafx.scene.control.ButtonType.FINISH -> FINISH
			javafx.scene.control.ButtonType.NEXT -> NEXT
			javafx.scene.control.ButtonType.PREVIOUS -> PREVIOUS
			else -> throw IllegalArgumentException()
		}
	}
}