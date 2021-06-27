@file:Suppress("unused")

package tools.aqua.bgw.event

/**
 * Interface for event handlers.
 */
@FunctionalInterface
fun interface EventHandler<T : Event> {
	/**
	 * Handle method for raised events.
	 */
	fun handle(event: T)
}