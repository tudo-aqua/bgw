@file:Suppress("unused")

package tools.aqua.bgw.event

import tools.aqua.bgw.elements.ElementView

/**
 * Event that gets raised for drag events.
 *
 * @param draggedElement currently dragged ElementView.
 *
 * @see DropEvent
 */
open class DragEvent(val draggedElement: ElementView) : Event()