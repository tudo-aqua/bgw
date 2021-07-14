package util.bidirectionalmap

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LookupTest : BidirectionalMapTestBase() {
	@Test
	@DisplayName("Test forward lookup on existing element")
	fun testForwardExisting() {
		assertEquals(1, map.forward(0))
		assertEquals(1, map.forwardOrNull(0))
	}
	
	@Test
	@DisplayName("Test backward lookup on existing element")
	fun testBackwardExisting() {
		assertEquals(0, map.backward(1))
		assertEquals(0, map.backwardOrNull(1))
	}
	
	@Test
	@DisplayName("Test forward lookup on non-existing element")
	fun testForwardNonExisting() {
		assertFailsWith<NoSuchElementException> {
			map.forward(5)
		}
		assertEquals(null, map.forwardOrNull(5))
	}
	
	@Test
	@DisplayName("Test backward lookup on non-existing element")
	fun testBackwardNonExisting() {
		assertFailsWith<NoSuchElementException> {
			map.backward(5)
		}
		assertEquals(null, map.backwardOrNull(5))
	}
}