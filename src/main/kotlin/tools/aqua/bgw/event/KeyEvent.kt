@file:Suppress("unused")

package tools.aqua.bgw.event

/**
 * Event that gets raised for key inputs.
 *
 * @param keyCode corresponding key code enum value.
 * @param character corresponding character string.
 * @param controlDown whether control key was pressed.
 * @param shiftDown whether shift key was pressed.
 * @param altDown whether alt key was pressed.
 */
class KeyEvent(
	val keyCode: KeyCode = KeyCode.UNDEFINED,
	val character: String = "",
	val controlDown: Boolean,
	val shiftDown: Boolean,
	val altDown: Boolean
) : InputEvent()
