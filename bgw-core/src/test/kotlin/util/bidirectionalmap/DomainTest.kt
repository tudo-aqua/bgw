package util.bidirectionalmap

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import tools.aqua.bgw.util.BidirectionalMap
import kotlin.test.assertEquals

class DomainTest : BidirectionalMapTestBase() {
	@Test
	@DisplayName("Test domain")
	fun testDomain() {
		assertEquals(setOf(0, 2), map.getDomain())
	}
	
	@Test
	@DisplayName("Test co-domain")
	fun testCoDomain() {
		assertEquals(setOf(1, 3), map.getCoDomain())
	}
	
	@Test
	@DisplayName("Test empty domain")
	fun testEmptyDomain() {
		assertEquals(setOf(), BidirectionalMap<Int, Int>().getDomain())
	}
	
	@Test
	@DisplayName("Test empty co-domain")
	fun testEmptyCoDomain() {
		assertEquals(setOf(), BidirectionalMap<Int, Int>().getCoDomain())
	}
}