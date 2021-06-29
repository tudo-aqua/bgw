package util.bidirectionalmap

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RemoveTest : BidirectionalMapTestBase() {
	@Test
	@DisplayName("Test remove of existing relation")
	fun testRemoveExistingRelation() {
		assertTrue(map.remove(0, 1))
		assertFalse(map.contains(0, 1))
		assertEquals(1, map.size)
	}
	
	@Test
	@DisplayName("Test remove of relation with existing key and non-existing value")
	fun testRemoveRelationExistingKey() {
		assertFalse(map.remove(0, 5))
		assertTrue(map.contains(0, 1))
		assertEquals(2, map.size)
	}
	
	@Test
	@DisplayName("Test remove of relation with non-existing key and existing value")
	fun testRemoveRelationExistingValue() {
		assertFalse(map.remove(5, 1))
		assertTrue(map.contains(0, 1))
		assertEquals(2, map.size)
	}
	
	@Test
	@DisplayName("Test remove forward on existing key")
	fun testRemoveForwardExisting() {
		assertTrue(map.removeForward(0))
		assertFalse(map.contains(0, 1))
		assertEquals(1, map.size)
	}
	
	@Test
	@DisplayName("Test remove forward on non-existing key")
	fun testRemoveForwardNonExisting() {
		assertFalse(map.removeForward(5))
		assertEquals(2, map.size)
	}
	
	@Test
	@DisplayName("Test remove backward on existing value")
	fun testRemoveBackwardExisting() {
		assertTrue(map.removeBackward(1))
		assertFalse(map.contains(0, 1))
		assertEquals(1, map.size)
	}
	
	@Test
	@DisplayName("Test remove backward on non-existing value")
	fun testRemoveBackwardNonExisting() {
		assertFalse(map.removeBackward(5))
		assertEquals(2, map.size)
	}
	
	@Test
	@DisplayName("Test clear")
	fun testClear() {
		map.clear()
		assertEquals(0, map.size)
	}
	
	@Test
	@DisplayName("Test isEmpty and isNotEmpty")
	fun testIsEmpty() {
		assertFalse(map.isEmpty())
		assertTrue(map.isNotEmpty())
		
		map.clear()
		
		assertTrue(map.isEmpty())
		assertFalse(map.isNotEmpty())
	}
}