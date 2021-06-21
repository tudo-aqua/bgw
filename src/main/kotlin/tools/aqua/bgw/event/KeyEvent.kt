package tools.aqua.bgw.event

/**
 * Event that gets raised for key inputs.
 *
 * @param keyCode Corresponding key code enum value
 * @param character Corresponding character string
 * @param controlDown Whether control key was pressed
 * @param shiftDown Whether shift key was pressed
 * @param altDown Whether alt key was pressed
 */
class KeyEvent(
	val keyCode: KeyCode = KeyCode.UNDEFINED,
	val character: String = "",
	val controlDown: Boolean,
	val shiftDown: Boolean,
	val altDown: Boolean
) : Event()
