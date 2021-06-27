@file:Suppress("unused")

package tools.aqua.bgw.exception

/**
 * An exception that is thrown when trying to inherit from a top-level framework class which is prohibited.
 */
internal class IllegalInheritanceException(inheritance: Any, supertype: Class<*>) :
	RuntimeException(
		"Illegal direct inheritance of "
				+ inheritance::class.java.name
				+ " from supertype "
				+ supertype.name
				+ " has no legal rendering."
	)
