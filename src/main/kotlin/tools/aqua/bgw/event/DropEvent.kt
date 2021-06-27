@file:Suppress("unused")

package tools.aqua.bgw.event

import tools.aqua.bgw.elements.ElementView

/**
 * Event that gets raised for drop events after drag gestures.
 * Receiver is the drop target.
 *
 * @param draggedElement currently dragged ElementView.
 *
 * @see DragEvent
 */
class DropEvent(draggedElement: ElementView) : DragDropEvent(draggedElement)