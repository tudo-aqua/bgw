@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package tools.aqua.bgw.event

import tools.aqua.bgw.elements.ElementView

/**
 * Event that gets raised for drop gestures.
 * Receiver is the dragged element.
 *
 * @param dragTargets list of all [ElementView]s that accepted the drag gesture in case of a [dragGestureEnded] event.
 * Contains all accepting ElementViews in the order they accepted.
 * Empty on other drag events pre drop.
 *
 * @see DragEvent
 */
open class DropEvent(draggedElement: ElementView, val dragTargets: List<ElementView> = listOf()) :
	DragEvent(draggedElement)