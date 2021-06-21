package tools.aqua.bgw.event

import tools.aqua.bgw.elements.ElementView

/**
 * Event that gets raised for drag gestures.
 *
 * @param draggedElement Currently dragged ElementView
 */
class DropEvent(val draggedElement: ElementView) : Event()