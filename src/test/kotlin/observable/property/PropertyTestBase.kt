package observable.property

import org.junit.jupiter.api.BeforeEach
import tools.aqua.bgw.observable.LimitedDoubleProperty

open class PropertyTestBase {
	protected val initialValue: Double = 13.0
	protected val newValue: Double = 42.0
	protected val lowerBound: Double = -5.0
	protected val upperBound: Double = 50.0
	
	protected lateinit var property: LimitedDoubleProperty
	protected val listener1: TestListener = TestListener()
	protected val listener2: TestListener = TestListener()
	protected val internalListener: TestListener = TestListener()
	protected val guiListener: TestListener = TestListener()
	
	@BeforeEach
	fun setUp() {
		property = LimitedDoubleProperty(initialValue, lowerBound, upperBound)
		property.addListener(listener1)
		property.addListener(listener2)
		property.internalListener = internalListener
		property.guiListener = guiListener
	}
	
	class TestListener : ((Double, Double) -> Unit) {
		var invokedCount: Int = 0
		var oldValue: Double? = null
		var newValue: Double? = null
		
		override fun invoke(oV: Double, nV: Double) {
			invokedCount++
			oldValue = oV
			newValue = nV
		}
	}
}