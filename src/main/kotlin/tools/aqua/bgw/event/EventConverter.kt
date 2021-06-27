package tools.aqua.bgw.event

/**
 * EventConverters between JavaFX and BGW.
 */
internal class EventConverter {
	companion object {
		/**
		 * Converts the JavaFX MouseEvent to tools.aqua.bgw MouseEvent.
		 */
		internal fun javafx.scene.input.MouseEvent.toMouseEvent(): MouseEvent =
			MouseEvent(
				when (button) {
					javafx.scene.input.MouseButton.PRIMARY -> MouseButtonType.LEFT_BUTTON
					javafx.scene.input.MouseButton.SECONDARY -> MouseButtonType.RIGHT_BUTTON
					javafx.scene.input.MouseButton.MIDDLE -> MouseButtonType.MOUSE_WHEEL
					else -> MouseButtonType.OTHER
				}
			)
		
		/**
		 * Converts the JavaFX KeyEvent to tools.aqua.bgw KeyEvent.
		 */
		internal fun javafx.scene.input.KeyEvent.toKeyEvent(): KeyEvent =
			KeyEvent(
				keyCode = code.toKeyCode(),
				character = character,
				shiftDown = isShiftDown,
				altDown = isAltDown,
				controlDown = isControlDown
			)
		
		/**
		 * Converts the JavaFX KeyCode to tools.aqua.bgw KeyCode.
		 */
		internal fun javafx.scene.input.KeyCode.toKeyCode(): KeyCode = when (this) {
			javafx.scene.input.KeyCode.SHIFT -> KeyCode.SHIFT
			javafx.scene.input.KeyCode.CONTROL -> KeyCode.CONTROL
			javafx.scene.input.KeyCode.ALT -> KeyCode.ALT
			javafx.scene.input.KeyCode.ALT_GRAPH -> KeyCode.ALT_GRAPH
			javafx.scene.input.KeyCode.WINDOWS -> KeyCode.WINDOWS
			javafx.scene.input.KeyCode.CONTEXT_MENU -> KeyCode.CONTEXT_MENU
			javafx.scene.input.KeyCode.TAB -> KeyCode.TAB
			javafx.scene.input.KeyCode.CAPS -> KeyCode.CAPS
			javafx.scene.input.KeyCode.ENTER -> KeyCode.ENTER
			javafx.scene.input.KeyCode.SPACE -> KeyCode.SPACE
			javafx.scene.input.KeyCode.BACK_SPACE -> KeyCode.BACK_SPACE
			javafx.scene.input.KeyCode.PAUSE -> KeyCode.PAUSE
			javafx.scene.input.KeyCode.SCROLL_LOCK -> KeyCode.SCROLL_LOCK
			javafx.scene.input.KeyCode.ESCAPE -> KeyCode.ESCAPE
			
			javafx.scene.input.KeyCode.DELETE -> KeyCode.DELETE
			javafx.scene.input.KeyCode.HOME -> KeyCode.HOME_POS1
			javafx.scene.input.KeyCode.END -> KeyCode.END
			javafx.scene.input.KeyCode.PAGE_UP -> KeyCode.PAGE_UP
			javafx.scene.input.KeyCode.PAGE_DOWN -> KeyCode.PAGE_DOWN
			
			javafx.scene.input.KeyCode.LEFT -> KeyCode.LEFT
			javafx.scene.input.KeyCode.UP -> KeyCode.UP
			javafx.scene.input.KeyCode.RIGHT -> KeyCode.RIGHT
			javafx.scene.input.KeyCode.DOWN -> KeyCode.DOWN
			
			javafx.scene.input.KeyCode.PRINTSCREEN -> KeyCode.PRINT_SCREEN
			javafx.scene.input.KeyCode.INSERT -> KeyCode.INSERT
			
			javafx.scene.input.KeyCode.DIGIT0 -> KeyCode.DIGIT0
			javafx.scene.input.KeyCode.DIGIT1 -> KeyCode.DIGIT1
			javafx.scene.input.KeyCode.DIGIT2 -> KeyCode.DIGIT2
			javafx.scene.input.KeyCode.DIGIT3 -> KeyCode.DIGIT3
			javafx.scene.input.KeyCode.DIGIT4 -> KeyCode.DIGIT4
			javafx.scene.input.KeyCode.DIGIT5 -> KeyCode.DIGIT5
			javafx.scene.input.KeyCode.DIGIT6 -> KeyCode.DIGIT6
			javafx.scene.input.KeyCode.DIGIT7 -> KeyCode.DIGIT7
			javafx.scene.input.KeyCode.DIGIT8 -> KeyCode.DIGIT8
			javafx.scene.input.KeyCode.DIGIT9 -> KeyCode.DIGIT9
			javafx.scene.input.KeyCode.DEAD_CIRCUMFLEX -> KeyCode.CIRCUMFLEX
			javafx.scene.input.KeyCode.DEAD_ACUTE -> KeyCode.ACUTE
			
			javafx.scene.input.KeyCode.A -> KeyCode.A
			javafx.scene.input.KeyCode.B -> KeyCode.B
			javafx.scene.input.KeyCode.C -> KeyCode.C
			javafx.scene.input.KeyCode.D -> KeyCode.D
			javafx.scene.input.KeyCode.E -> KeyCode.E
			javafx.scene.input.KeyCode.F -> KeyCode.F
			javafx.scene.input.KeyCode.G -> KeyCode.G
			javafx.scene.input.KeyCode.H -> KeyCode.H
			javafx.scene.input.KeyCode.I -> KeyCode.I
			javafx.scene.input.KeyCode.J -> KeyCode.J
			javafx.scene.input.KeyCode.K -> KeyCode.K
			javafx.scene.input.KeyCode.L -> KeyCode.L
			javafx.scene.input.KeyCode.M -> KeyCode.M
			javafx.scene.input.KeyCode.N -> KeyCode.N
			javafx.scene.input.KeyCode.O -> KeyCode.O
			javafx.scene.input.KeyCode.P -> KeyCode.P
			javafx.scene.input.KeyCode.Q -> KeyCode.Q
			javafx.scene.input.KeyCode.R -> KeyCode.R
			javafx.scene.input.KeyCode.S -> KeyCode.S
			javafx.scene.input.KeyCode.T -> KeyCode.T
			javafx.scene.input.KeyCode.U -> KeyCode.U
			javafx.scene.input.KeyCode.V -> KeyCode.V
			javafx.scene.input.KeyCode.W -> KeyCode.W
			javafx.scene.input.KeyCode.X -> KeyCode.X
			javafx.scene.input.KeyCode.Y -> KeyCode.Y
			javafx.scene.input.KeyCode.Z -> KeyCode.Z
			
			javafx.scene.input.KeyCode.COMMA -> KeyCode.COMMA
			javafx.scene.input.KeyCode.PERIOD -> KeyCode.PERIOD
			javafx.scene.input.KeyCode.MINUS -> KeyCode.MINUS
			javafx.scene.input.KeyCode.LESS -> KeyCode.LESS
			javafx.scene.input.KeyCode.NUMBER_SIGN -> KeyCode.NUMBER_SIGN
			javafx.scene.input.KeyCode.PLUS -> KeyCode.PLUS
			
			javafx.scene.input.KeyCode.NUM_LOCK -> KeyCode.NUM_LOCK
			javafx.scene.input.KeyCode.NUMPAD0 -> KeyCode.NUMPAD0
			javafx.scene.input.KeyCode.NUMPAD1 -> KeyCode.NUMPAD1
			javafx.scene.input.KeyCode.NUMPAD2 -> KeyCode.NUMPAD2
			javafx.scene.input.KeyCode.NUMPAD3 -> KeyCode.NUMPAD3
			javafx.scene.input.KeyCode.NUMPAD4 -> KeyCode.NUMPAD4
			javafx.scene.input.KeyCode.NUMPAD5 -> KeyCode.NUMPAD5
			javafx.scene.input.KeyCode.NUMPAD6 -> KeyCode.NUMPAD6
			javafx.scene.input.KeyCode.NUMPAD7 -> KeyCode.NUMPAD7
			javafx.scene.input.KeyCode.NUMPAD8 -> KeyCode.NUMPAD8
			javafx.scene.input.KeyCode.NUMPAD9 -> KeyCode.NUMPAD9
			javafx.scene.input.KeyCode.DIVIDE -> KeyCode.DIVIDE
			javafx.scene.input.KeyCode.MULTIPLY -> KeyCode.MULTIPLY
			javafx.scene.input.KeyCode.SUBTRACT -> KeyCode.SUBTRACT
			javafx.scene.input.KeyCode.ADD -> KeyCode.ADD
			javafx.scene.input.KeyCode.DECIMAL -> KeyCode.DECIMAL
			
			javafx.scene.input.KeyCode.F1 -> KeyCode.F1
			javafx.scene.input.KeyCode.F2 -> KeyCode.F2
			javafx.scene.input.KeyCode.F3 -> KeyCode.F3
			javafx.scene.input.KeyCode.F4 -> KeyCode.F4
			javafx.scene.input.KeyCode.F5 -> KeyCode.F5
			javafx.scene.input.KeyCode.F6 -> KeyCode.F6
			javafx.scene.input.KeyCode.F7 -> KeyCode.F7
			javafx.scene.input.KeyCode.F8 -> KeyCode.F8
			javafx.scene.input.KeyCode.F9 -> KeyCode.F9
			javafx.scene.input.KeyCode.F10 -> KeyCode.F10
			javafx.scene.input.KeyCode.F11 -> KeyCode.F11
			javafx.scene.input.KeyCode.F12 -> KeyCode.F12
			else -> KeyCode.UNDEFINED
		}
	}
}
