@file:Suppress("unused")

package tools.aqua.bgw.exception

/**
 * An exception that is thrown when an operation is attempted on an empty list
 * that actually requires elements in this list.
 */
@Deprecated("No longer used")
class EmptyListException(msg: String = "") : RuntimeException(msg)