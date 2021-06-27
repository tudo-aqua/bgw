package tools.aqua.bgw.event

import tools.aqua.bgw.elements.ElementView

/**
 * Superclass for drag drop events.
 *
 * @param draggedElement currently dragged ElementView.
 */
abstract class DragDropEvent(val draggedElement: ElementView) : Event()