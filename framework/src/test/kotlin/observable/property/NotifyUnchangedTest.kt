package observable.property

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class NotifyUnchangedTest : PropertyTestBase() {
	
	@Test
	@DisplayName("Notify Unchanged invoking all listener")
	fun testNotifyUnchanged() {
		property.notifyUnchanged()
		assertEquals(initialValue, property.value)
		
		assertEquals(1, listener1.invokedCount)
		assertEquals(initialValue, listener1.oldValue)
		assertEquals(initialValue, listener1.newValue)
		
		assertEquals(1, listener2.invokedCount)
		assertEquals(initialValue, listener2.oldValue)
		assertEquals(initialValue, listener2.newValue)
		
		assertEquals(1, internalListener.invokedCount)
		assertEquals(initialValue, internalListener.oldValue)
		assertEquals(initialValue, internalListener.newValue)
		
		assertEquals(1, guiListener.invokedCount)
		assertEquals(initialValue, guiListener.oldValue)
		assertEquals(initialValue, guiListener.newValue)
	}
}