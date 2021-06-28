package observable.property

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import tools.aqua.bgw.observable.LimitedDoubleProperty
import kotlin.test.assertFailsWith

class LimitedDoublePropertyTest : PropertyTestBase() {
	
	@Test
	@DisplayName("Test upperBound < lowerBound")
	fun testBoundsWrongOrderEqual() {
		assertFailsWith<IllegalArgumentException> {
			LimitedDoubleProperty(0, 10, 5)
		}
	}
	
	@Test
	@DisplayName("Test upperBound = lowerBound")
	fun testBoundsEqual() {
		val newProperty = LimitedDoubleProperty(10, 10, 10)
		newProperty.value = 10.0
	}
	
	@Test
	@DisplayName("Test initial value out of bounds")
	fun testInitialValueOutOfBounds() {
		assertFailsWith<IllegalArgumentException> {
			LimitedDoubleProperty(15, 0, 10)
		}
	}
	
	@Test
	@DisplayName("Notify Unchanged invoking all listener")
	fun testSetOutOfBounds() {
		assertFailsWith<IllegalArgumentException> {
			property.value = -7.0
		}
	}
}