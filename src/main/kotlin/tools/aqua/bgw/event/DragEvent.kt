package tools.aqua.bgw.event

import tools.aqua.bgw.elements.ElementView

/**
 * Event that gets raised for drag gestures.
 *
 * @param draggedElement Currently dragged ElementView
 * @param dragTargets list of all ElementViews that accepted the drag gesture
 */
class DragEvent(val draggedElement: ElementView, val dragTargets: List<ElementView> = listOf()) : Event()