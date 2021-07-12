@file:Suppress("unused")

package tools.aqua.bgw.dialog

/**
 * Shows a dialog containing the given [message] and [buttons].
 *
 * @constructor Internal constructor, should not be used. Refer to secondary constructors.
 *
 * @param alertType the [AlertType] of the alert. Affects the displayed icon.
 * @param title title to be shown.
 * @param header headline to be shown in the dialogs content.
 * @param message message to be shown.
 * @param exception throwable to be shown in expandable content.
 * @param buttons buttons to be shown. Standard set of buttons according to [alertType] will be used if you don't pass
 * any [ButtonType]s.
 */
class Dialog(
	val alertType: AlertType,
	val title: String,
	val header: String,
	val message: String,
	val exception: Throwable? = null,
	vararg val buttons: ButtonType
) {
	
	/**
	 * Creates an [exception] [Dialog].
	 *
	 * @param title title to be shown.
	 * @param header headline to be shown in the dialogs content.
	 * @param message message to be shown.
	 * @param exception throwable to be shown in expandable content.
	 */
	constructor(title: String, header: String, message: String, exception: Throwable) :
			this(AlertType.EXCEPTION, title, header, message, exception, ButtonType.OK)
	
	/**
	 * Creates a [Dialog].
	 *
	 * @param alertType the [AlertType] of the alert. Affects the displayed icon.
	 * @param title title to be shown.
	 * @param header headline to be shown in the dialogs content.
	 * @param message message to be shown in the dialogs content.
	 * @param buttons buttons to be shown. Standard set of buttons according to [alertType] will be used if you don't pass
	 * any [ButtonType]s.
	 */
	constructor(alertType: AlertType, title: String, header: String, message: String, vararg buttons: ButtonType) :
			this(alertType, title, header, message, null, *buttons) {
		require(alertType != AlertType.EXCEPTION) {
			"To create an Exception dialog use exception dialog constructor."
		}
	}
}