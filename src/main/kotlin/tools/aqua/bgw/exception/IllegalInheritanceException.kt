@file:Suppress("unused")

package tools.aqua.bgw.exception

internal class IllegalInheritanceException(inheritance: Any, supertype: Class<*>) :
	RuntimeException(
		"Illegal direct inheritance of "
				+ inheritance::class.java.name
				+ " from supertype "
				+ supertype.name
				+ " has no legal rendering."
	)
