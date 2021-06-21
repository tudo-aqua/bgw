package tools.aqua.bgw.event

/**
 * Enum for available key codes.
 *
 * @param string Key name
 * @param keyTypeMask Mask for the key's type
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
enum class KeyCode(val string: String, private val keyTypeMask: Int) {
	
	//region Function keys
	/**
	 * Constant for the **Shift** key.
	 */
	SHIFT("Shift", KeyType.MODIFIER),
	
	/**
	 * Constant for the **Ctrl** key.
	 */
	CONTROL("Ctrl", KeyType.MODIFIER),
	
	/**
	 * Constant for the **Alt** key.
	 */
	ALT("Alt", KeyType.MODIFIER),
	
	/**
	 * Constant for the **AltGr** key.
	 */
	ALT_GRAPH("AltGr", KeyType.MODIFIER),
	
	/**
	 * Constant for the **Caps Lock** key.
	 */
	CAPS("Caps", KeyType.OTHER),
	
	/**
	 * Constant for the Microsoft Windows **Win** key.
	 * It is used for both the left and right version of the key.
	 */
	WINDOWS("Win", KeyType.MODIFIER),
	
	/**
	 * Constant for the Microsoft Windows Context Menu key.
	 */
	CONTEXT_MENU("Context Menu", KeyType.OTHER),
	
	/**
	 * Constant for the **Space** key.
	 */
	SPACE("Space", KeyType.WHITESPACE),
	
	/**
	 * Constant for the **Backspace** key.
	 */
	BACK_SPACE("Backspace", KeyType.OTHER),
	
	/**
	 * Constant for the **Tab** key.
	 */
	TAB("Tab", KeyType.WHITESPACE),
	//endregion
	
	//region Digits
	/**
	 * Constant for the **1** key.
	 */
	DIGIT1("1", KeyType.DIGIT),
	
	/**
	 * Constant for the **2** key.
	 */
	DIGIT2("2", KeyType.DIGIT),
	
	/**
	 * Constant for the **3** key.
	 */
	DIGIT3("3", KeyType.DIGIT),
	
	/**
	 * Constant for the **4** key.
	 */
	DIGIT4("4", KeyType.DIGIT),
	
	/**
	 * Constant for the **5** key.
	 */
	DIGIT5("5", KeyType.DIGIT),
	
	/**
	 * Constant for the **6** key.
	 */
	DIGIT6("6", KeyType.DIGIT),
	
	/**
	 * Constant for the **7** key.
	 */
	DIGIT7("7", KeyType.DIGIT),
	
	/**
	 * Constant for the **8** key.
	 */
	DIGIT8("8", KeyType.DIGIT),
	
	/**
	 * Constant for the **9** key.
	 */
	DIGIT9("9", KeyType.DIGIT),
	
	/**
	 * Constant for the **0** key.
	 */
	DIGIT0("0", KeyType.DIGIT),
	
	/**
	 * Constant for the **^** key.
	 */
	CIRCUMFLEX("^", KeyType.OTHER),
	
	/**
	 * Constant for the **!** key.
	 */
	EXCLAMATION_MARK("!", KeyType.OTHER),
	
	/**
	 * Constant for the **"** key.
	 */
	DOUBLE_QUOTE("\"", KeyType.OTHER),
	
	/**
	 * Constant for the **$** key.
	 */
	DOLLAR("$", KeyType.OTHER),
	
	/**
	 * Constant for the **&** key.
	 */
	AMPERSAND("&", KeyType.OTHER),
	
	/**
	 * Constant for the forward slash ** / ** key.
	 */
	SLASH("/", KeyType.OTHER),
	
	/**
	 * Constant for the **(** key.
	 */
	LEFT_PARENTHESIS("(", KeyType.OTHER),
	
	/**
	 * Constant for the **)** key.
	 */
	RIGHT_PARENTHESIS(")", KeyType.OTHER),
	
	/**
	 * Constant for the **{** key.
	 */
	CURLY_BRACE_LEFT("{", KeyType.OTHER),
	
	/**
	 * Constant for the **}** key.
	 */
	CURLY_BRACE_RIGHT("}", KeyType.OTHER),
	
	/**
	 * Constant for the **[** key.
	 */
	OPEN_BRACKET("[", KeyType.OTHER),
	
	/**
	 * Constant for the **]** key.
	 */
	CLOSE_BRACKET("]", KeyType.OTHER),
	
	/**
	 * Constant for the **=** key.
	 */
	EQUALS("=", KeyType.OTHER),
	
	/**
	 * Constant for the **\** key.
	 */
	BACK_SLASH("\\", KeyType.OTHER),
	
	/**
	 * Constant for the **`** key.
	 */
	GRAVE("`", KeyType.OTHER),
	
	/**
	 * Constant for the **´** key.
	 */
	ACUTE("´", KeyType.OTHER),
	//endregion
	
	//region Letters
	/**
	 * Constant for the **A** key.
	 */
	A("A", KeyType.LETTER),
	
	/**
	 * Constant for the **B** key.
	 */
	B("B", KeyType.LETTER),
	
	/**
	 * Constant for the **C** key.
	 */
	C("C", KeyType.LETTER),
	
	/**
	 * Constant for the **D** key.
	 */
	D("D", KeyType.LETTER),
	
	/**
	 * Constant for the **E** key.
	 */
	E("E", KeyType.LETTER),
	
	/**
	 * Constant for the **F** key.
	 */
	F("F", KeyType.LETTER),
	
	/**
	 * Constant for the **G** key.
	 */
	G("G", KeyType.LETTER),
	
	/**
	 * Constant for the **H** key.
	 */
	H("H", KeyType.LETTER),
	
	/**
	 * Constant for the **I** key.
	 */
	I("I", KeyType.LETTER),
	
	/**
	 * Constant for the **J** key.
	 */
	J("J", KeyType.LETTER),
	
	/**
	 * Constant for the **K** key.
	 */
	K("K", KeyType.LETTER),
	
	/**
	 * Constant for the **L** key.
	 */
	L("L", KeyType.LETTER),
	
	/**
	 * Constant for the **M** key.
	 */
	M("M", KeyType.LETTER),
	
	/**
	 * Constant for the **N** key.
	 */
	N("N", KeyType.LETTER),
	
	/**
	 * Constant for the **O** key.
	 */
	O("O", KeyType.LETTER),
	
	/**
	 * Constant for the **P** key.
	 */
	P("P", KeyType.LETTER),
	
	/**
	 * Constant for the **Q** key.
	 */
	Q("Q", KeyType.LETTER),
	
	/**
	 * Constant for the **R** key.
	 */
	R("R", KeyType.LETTER),
	
	/**
	 * Constant for the **S** key.
	 */
	S("S", KeyType.LETTER),
	
	/**
	 * Constant for the **T** key.
	 */
	T("T", KeyType.LETTER),
	
	/**
	 * Constant for the **U** key.
	 */
	U("U", KeyType.LETTER),
	
	/**
	 * Constant for the **V** key.
	 */
	V("V", KeyType.LETTER),
	
	/**
	 * Constant for the **W** key.
	 */
	W("W", KeyType.LETTER),
	
	/**
	 * Constant for the **X** key.
	 */
	X("X", KeyType.LETTER),
	
	/**
	 * Constant for the **Y** key.
	 */
	Y("Y", KeyType.LETTER),
	
	/**
	 * Constant for the **Z** key.
	 */
	Z("Z", KeyType.LETTER),
	//endregion
	
	//region Characters
	/**
	 * Constant for the **@** key.
	 */
	AT("@", KeyType.OTHER),
	
	/**
	 * Constant for the Euro currency sign **€** key.
	 */
	EURO_SIGN("€", KeyType.OTHER),
	
	/**
	 * Constant for the **,** key.
	 */
	COMMA(",", KeyType.OTHER),
	
	/**
	 * Constant for the **;** key.
	 */
	SEMICOLON(";", KeyType.OTHER),
	
	/**
	 * Constant for the **.** key.
	 */
	PERIOD(".", KeyType.OTHER),
	
	/**
	 * Constant for the **:** key.
	 */
	COLON(":", KeyType.OTHER),
	
	/**
	 * Constant for the **-** key.
	 */
	MINUS("-", KeyType.OTHER),
	
	/**
	 * Constant for the **_** key.
	 */
	UNDERSCORE("_", KeyType.OTHER),
	
	/**
	 * Constant for the **+** key.
	 */
	PLUS("+", KeyType.OTHER),
	
	/**
	 * Constant for the ***** key.
	 */
	ASTERISK("*", KeyType.OTHER),
	
	/**
	 * Constant for the **~** key.
	 */
	TILDE("~", KeyType.OTHER),
	
	/**
	 * Constant for the **#** key.
	 */
	NUMBER_SIGN("#", KeyType.OTHER),
	
	/**
	 * Constant for the **<** key.
	 */
	LESS("<", KeyType.OTHER),
	
	/**
	 * Constant for the **>** key.
	 */
	GREATER(">", KeyType.OTHER),
	//endregion
	
	//region Numpad
	/**
	 * Constant for the **Numpad 0** key.
	 */
	NUMPAD0("0", KeyType.DIGIT or KeyType.NUMPAD),
	
	/**
	 * Constant for the **Numpad 1** key.
	 */
	NUMPAD1("1", KeyType.DIGIT or KeyType.NUMPAD),
	
	/**
	 * Constant for the **Numpad 2** key.
	 */
	NUMPAD2("2", KeyType.DIGIT or KeyType.NUMPAD),
	
	/**
	 * Constant for the **Numpad 3** key.
	 */
	NUMPAD3("3", KeyType.DIGIT or KeyType.NUMPAD),
	
	/**
	 * Constant for the **Numpad 4** key.
	 */
	NUMPAD4("4", KeyType.DIGIT or KeyType.NUMPAD),
	
	/**
	 * Constant for the **Numpad 5** key.
	 */
	NUMPAD5("5", KeyType.DIGIT or KeyType.NUMPAD),
	
	/**
	 * Constant for the **Numpad 6** key.
	 */
	NUMPAD6("6", KeyType.DIGIT or KeyType.NUMPAD),
	
	/**
	 * Constant for the **Numpad 7** key.
	 */
	NUMPAD7("7", KeyType.DIGIT or KeyType.NUMPAD),
	
	/**
	 * Constant for the **Numpad 8** key.
	 */
	NUMPAD8("8", KeyType.DIGIT or KeyType.NUMPAD),
	
	/**
	 * Constant for the **Numpad 9** key.
	 */
	NUMPAD9("9", KeyType.DIGIT or KeyType.NUMPAD),
	
	/**
	 * Constant for the **Num Lock** key.
	 */
	NUM_LOCK("Num Lock", KeyType.NUMPAD),
	
	/**
	 * Constant for the **Numpad / ** key.
	 */
	DIVIDE("/", KeyType.NUMPAD),
	
	/**
	 * Constant for the **Numpad *** key.
	 */
	MULTIPLY("*", KeyType.NUMPAD),
	
	/**
	 * Constant for the **Numpad -** key.
	 */
	SUBTRACT("-", KeyType.NUMPAD),
	
	/**
	 * Constant for the **Numpad +** key.
	 */
	ADD("+", KeyType.NUMPAD),
	
	/**
	 * Constant for the **Numpad ,** key.
	 */
	DECIMAL(",", KeyType.NUMPAD),
	
	/**
	 * Constant for the **Enter** key.
	 */
	ENTER("Enter", KeyType.OTHER),
	//endregion
	
	//region F-Keys
	/**
	 * Constant for the **Esc** key.
	 */
	ESCAPE("Esc", KeyType.OTHER),
	
	/**
	 * Constant for the F1 function key.
	 */
	F1("F1", KeyType.FUNCTION),
	
	/**
	 * Constant for the F2 function key.
	 */
	F2("F2", KeyType.FUNCTION),
	
	/**
	 * Constant for the F3 function key.
	 */
	F3("F3", KeyType.FUNCTION),
	
	/**
	 * Constant for the F4 function key.
	 */
	F4("F4", KeyType.FUNCTION),
	
	/**
	 * Constant for the F5 function key.
	 */
	F5("F5", KeyType.FUNCTION),
	
	/**
	 * Constant for the F6 function key.
	 */
	F6("F6", KeyType.FUNCTION),
	
	/**
	 * Constant for the F7 function key.
	 */
	F7("F7", KeyType.FUNCTION),
	
	/**
	 * Constant for the F8 function key.
	 */
	F8("F8", KeyType.FUNCTION),
	
	/**
	 * Constant for the F9 function key.
	 */
	F9("F9", KeyType.FUNCTION),
	
	/**
	 * Constant for the F10 function key.
	 */
	F10("F10", KeyType.FUNCTION),
	
	/**
	 * Constant for the F11 function key.
	 */
	F11("F11", KeyType.FUNCTION),
	
	/**
	 * Constant for the F12 function key.
	 */
	F12("F12", KeyType.FUNCTION),
	
	/**
	 * Constant for the **Print Screen** key.
	 */
	PRINT_SCREEN("Print", KeyType.OTHER),
	
	/**
	 * Constant for the **Scroll Lock** key.
	 */
	SCROLL_LOCK("Scroll Lock", KeyType.OTHER),
	
	/**
	 * Constant for the **Pause** key.
	 */
	PAUSE("Pause", KeyType.OTHER),
	//endregion
	
	//region Functions
	/**
	 * Constant for the **Insert** key.
	 */
	INSERT("Insert", KeyType.OTHER),
	
	/**
	 * Constant for the **Delete** key.
	 */
	DELETE("Del", KeyType.OTHER),
	
	/**
	 * Constant for the **Home** / **Pos1** key.
	 */
	HOME_POS1("Pos1", KeyType.OTHER),
	
	/**
	 * Constant for the **End** key.
	 */
	END("End", KeyType.OTHER),
	
	/**
	 * Constant for the **Page Up** key.
	 */
	PAGE_UP("Page Up", KeyType.NAVIGATION),
	
	/**
	 * Constant for the **Page Down** key.
	 */
	PAGE_DOWN("Page Down", KeyType.NAVIGATION),
	
	/**
	 * Constant for the non-numpad **left** arrow key.
	 */
	LEFT("Left", KeyType.NAVIGATION or KeyType.ARROW),
	
	/**
	 * Constant for the non-numpad **up** arrow key.
	 */
	UP("Up", KeyType.NAVIGATION or KeyType.ARROW),
	
	/**
	 * Constant for the non-numpad **right** arrow key.
	 */
	RIGHT("Right", KeyType.NAVIGATION or KeyType.ARROW),
	
	/**
	 * Constant for the non-numpad **down** arrow key.
	 */
	DOWN("Down", KeyType.NAVIGATION or KeyType.ARROW),
	//endregion
	
	/**
	 * This value is used to indicate that the keyCode is unknown.
	 * Key typed events do not have a keyCode value this value is used instead.
	 */
	UNDEFINED("Undefined", KeyType.OTHER);
	
	//region methods
	/**
	 * Returns a string representation of the key.
	 */
	override fun toString(): String = string
	
	/**
	 * Returns true if key was a modifier like Shift or Ctrl etc, false otherwise.
	 */
	fun isModifier(): Boolean = keyTypeMask and KeyType.MODIFIER != 0
	
	/**
	 * Returns true if key was a letter key A-Z, false otherwise.
	 *
	 * @see isDigit
	 */
	fun isLetter(): Boolean = keyTypeMask and KeyType.LETTER != 0
	
	/**
	 * Returns true if key was digit key 1-9 from key- or numpad, false otherwise.
	 *
	 * @see isLetter
	 */
	fun isDigit(): Boolean = keyTypeMask and KeyType.DIGIT != 0
	
	/**
	 * Returns true if key was on numpad like numpad numbers or +,-,/,* etc, false otherwise.
	 */
	fun isOnNumpad(): Boolean = keyTypeMask and KeyType.NUMPAD != 0
	
	/**
	 * Returns true if key was an arrow key, false otherwise.
	 *
	 * @see isNavigation
	 */
	fun isArrow(): Boolean = keyTypeMask and KeyType.ARROW != 0
	
	/**
	 * Returns true if key was a navigation key like arrows and page up / down, false otherwise.
	 *
	 * @see isArrow
	 */
	fun isNavigation(): Boolean = keyTypeMask and KeyType.NAVIGATION != 0
	
	/**
	 * Returns true if key was a whitespace key like space, tab etc, false otherwise.
	 */
	fun isWhitespace(): Boolean = keyTypeMask and KeyType.WHITESPACE != 0
	
	/**
	 * Returns true if key was a function key F1, F2, etc, false otherwise.
	 */
	fun isFunction(): Boolean = keyTypeMask and KeyType.FUNCTION != 0
	//endregion
	
	/**
	 * Integer mask for KeyCode type.
	 */
	internal class KeyType {
		companion object {
			/**
			 * Modifier keys like Shift or Ctrl.
			 */
			internal const val MODIFIER = 1 shl 7
			
			/**
			 * All letter keys A-Z.
			 */
			internal const val LETTER = 1 shl 6
			
			/**
			 * All digits 0-9 on Key- and Numpad.
			 */
			internal const val DIGIT = 1 shl 5
			
			/**
			 * All Keys on numpad.
			 */
			internal const val NUMPAD = 1 shl 4
			
			/**
			 * Arrow keys.
			 */
			internal const val ARROW = 1 shl 3
			
			/**
			 * Navigation keys like arrows and page up / down.
			 */
			internal const val NAVIGATION = 1 shl 2
			
			/**
			 * Whitespace like Space or Tab.
			 */
			internal const val WHITESPACE = 1 shl 1
			
			/**
			 * F1-F12 Keys.
			 */
			internal const val FUNCTION = 1
			
			/**
			 * Default case.
			 */
			internal const val OTHER = 0
		}
	}
}