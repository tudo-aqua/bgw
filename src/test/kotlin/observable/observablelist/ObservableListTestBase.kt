package observable.observablelist

import org.junit.jupiter.api.BeforeEach
import tools.aqua.bgw.observable.ObservableArrayList
import tools.aqua.bgw.observable.ObservableList

open class ObservableListTestBase {
	
	protected lateinit var list: ObservableList<Int>
	protected val unordered: List<Int> = listOf(13, 25, 17, 13, -4)
	protected val ordered: List<Int> = listOf(-4, 13, 13, 17, 25)
	
	private val listener: TestListener = TestListener()
	
	@BeforeEach
	fun setUp() {
		list = ObservableArrayList(unordered)
		list.addListener(listener)
	}
	
	protected fun checkNotNotified(): Boolean = listener.invokedCount == 0
	
	protected fun checkNotified(count: Int = 1): Boolean = listener.invokedCount == count
	
	class TestListener : (() -> Unit) {
		var invokedCount: Int = 0
		
		override fun invoke() {
			invokedCount++
		}
	}
}