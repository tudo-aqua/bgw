package util.stack

import org.junit.jupiter.api.BeforeEach
import tools.aqua.bgw.util.Stack

open class StackTestBase {
	protected val order: List<Int> = listOf(3,2,5,4,1)
	
	protected lateinit var stack: Stack<Int>
	protected lateinit var emptyStack: Stack<Int>
	
	@BeforeEach
	fun setUp() {
		stack = Stack(order.reversed())
		emptyStack = Stack()
	}
}