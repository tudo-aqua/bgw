package observable.property

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SetSilentTest : PropertyTestBase() {
	
	@Test
	@DisplayName("Set silent only invoking GUI listener")
	fun testSetSilent() {
		property.setSilent(newValue)
		assertEquals(newValue, property.value)
		
		assertEquals(0, listener1.invokedCount)
		assertEquals(null, listener1.oldValue)
		assertEquals(null, listener1.newValue)
		
		assertEquals(0, listener2.invokedCount)
		assertEquals(null, listener2.oldValue)
		assertEquals(null, listener2.newValue)
		
		assertEquals(0, internalListener.invokedCount)
		assertEquals(null, internalListener.oldValue)
		assertEquals(null, internalListener.newValue)
		
		assertEquals(1, guiListener.invokedCount)
		assertEquals(initialValue, guiListener.oldValue)
		assertEquals(newValue, guiListener.newValue)
	}
	
	@Test
	@DisplayName("Set silent same value invoking no listeners")
	fun testGetSetSameValue() {
		property.setSilent(initialValue)
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