package util.bidirectionalmap

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ContainsTest : BidirectionalMapTestBase() {
	@Test
	@DisplayName("Test contains of existing relation")
	fun testContainsExistingRelation() {
		assertTrue(map.contains(0, 1))
	}
	
	@Test
	@DisplayName("Test contains of relation with existing key and non-existing value")
	fun testContainsRelationExistingKey() {
		assertFalse(map.contains(0, 5))
	}
	
	@Test
	@DisplayName("Test contains of relation with non-existing key and existing value")
	fun testContainsRelationExistingValue() {
		assertFalse(map.contains(5, 1))
	}
	
	@Test
	@DisplayName("Test contains forward on existing key")
	fun testContainsForwardExisting() {
		assertTrue(map.containsForward(0))
	}
	
	@Test
	@DisplayName("Test contains forward on non-existing key")
	fun testContainsForwardNonExisting() {
		assertFalse(map.containsForward(5))
	}
	
	@Test
	@DisplayName("Test contains backward on existing value")
	fun testContainsBackwardExisting() {
		assertTrue(map.containsBackward(1))
	}
	
	@Test
	@DisplayName("Test contains backward on non-existing value")
	fun testContainsBackwardNonExisting() {
		assertFalse(map.containsBackward(5))
	}
}