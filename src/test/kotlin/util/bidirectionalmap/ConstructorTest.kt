package util.bidirectionalmap

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import tools.aqua.bgw.util.BidirectionalMap
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ConstructorTest : BidirectionalMapTestBase() {
	@Test
	@DisplayName("Test empty constructor")
	fun testEmptyConstructor() {
		val lMap = BidirectionalMap<Int, Int>()
		
		assertTrue(lMap.isEmpty())
	}
	
	@Test
	@DisplayName("Test constructor with elements")
	fun testConstructorWithElements() {
		val item1 = Pair(0, 1)
		val item2 = Pair(2, 3)
		val lMap = BidirectionalMap(item1, item2)
		
		assertEquals(2, lMap.size)
		assertTrue(lMap.contains(item1.first, item1.second))
		assertTrue(lMap.contains(item2.first, item2.second))
	}
	
	@Test
	@DisplayName("Test constructor with duplicate key")
	fun testConstructorWithDuplicateKey() {
		val item1 = Pair(0, 1)
		val item2 = Pair(0, 2)
		
		val lMap = BidirectionalMap(item1, item2)
		
		assertEquals(1, lMap.size)
		assertTrue(lMap.contains(item1.first, item1.second))
		assertFalse(lMap.contains(item2.first, item2.second))
	}
	
	@Test
	@DisplayName("Test constructor with duplicate value")
	fun testConstructorWithDuplicateValue() {
		val item1 = Pair(0, 1)
		val item2 = Pair(1, 1)
		
		val lMap = BidirectionalMap(item1, item2)
		
		assertEquals(1, lMap.size)
		assertTrue(lMap.contains(item1.first, item1.second))
		assertFalse(lMap.contains(item2.first, item2.second))
	}
}