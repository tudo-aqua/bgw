@file:Suppress("unused")

package tools.aqua.bgw.exception

/**
 * An exception that is thrown when an operation is attempted on an empty stack
 * that actually requires elements in this stack.
 */
class EmptyStackException(msg: String = "") : RuntimeException(msg)