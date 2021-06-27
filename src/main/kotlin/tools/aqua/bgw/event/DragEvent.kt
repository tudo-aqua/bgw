@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package tools.aqua.bgw.event

import tools.aqua.bgw.elements.ElementView

/**
 * Event that gets raised for drag gestures.
 * Receiver is the dragged element.
 *
 * @param dragTargets list of all ElementViews that accepted the drag gesture in case of a dragGestureEnded event.
 * Contains all accepting ElementViews in the order they accepted.
 * Empty on other drag events pre drop.
 *
 * @see DropEvent
 */
class DragEvent(draggedElement: ElementView, val dragTargets: List<ElementView> = listOf()) :
	DragDropEvent(draggedElement)