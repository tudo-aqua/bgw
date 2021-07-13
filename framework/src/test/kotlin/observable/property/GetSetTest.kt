package observable.property

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GetSetTest : PropertyTestBase() {
	
	@Test
	@DisplayName("Get and set invoking all listeners")
	fun testGetSet() {
		property.value = newValue
		assertEquals(newValue, property.value)
		
		assertEquals(1, listener1.invokedCount)
		assertEquals(initialValue, listener1.oldValue)
		assertEquals(newValue, listener1.newValue)
		
		assertEquals(1, listener2.invokedCount)
		assertEquals(initialValue, listener2.oldValue)
		assertEquals(newValue, listener2.newValue)
		
		assertEquals(1, internalListener.invokedCount)
		assertEquals(initialValue, internalListener.oldValue)
		assertEquals(newValue, internalListener.newValue)
		
		assertEquals(1, guiListener.invokedCount)
		assertEquals(initialValue, guiListener.oldValue)
		assertEquals(newValue, guiListener.newValue)
	}
	
	@Test
	@DisplayName("Get and set same value invoking no listeners")
	fun testGetSetSameValue() {
		property.value = initialValue
		assertEquals(initialValue, property.value)
		
		assertEquals(0, listener1.invokedCount)
		assertEquals(null, listener1.oldValue)
		assertEquals(null, listener1.newValue)
		
		assertEquals(0, listener2.invokedCount)
		assertEquals(null, listener2.oldValue)
		assertEquals(null, listener2.newValue)
		
		assertEquals(0, internalListener.invokedCount)
		assertEquals(null, internalListener.oldValue)
		assertEquals(null, internalListener.newValue)
		
		assertEquals(0, guiListener.invokedCount)
		assertEquals(null, guiListener.oldValue)
		assertEquals(null, guiListener.newValue)
	}
}