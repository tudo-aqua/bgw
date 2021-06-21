package tools.aqua.bgw.event

/**
 * Event that gets raised for mouse inputs.
 *
 * @param button Corresponding mouse button enum value
 */
class MouseEvent(val button: MouseButtonType) : Event()