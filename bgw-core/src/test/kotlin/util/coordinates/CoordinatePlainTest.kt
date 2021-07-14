package util.coordinates

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import tools.aqua.bgw.util.Coordinate
import tools.aqua.bgw.util.CoordinatePlain
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CoordinatePlainTest {
	
	@BeforeEach
	fun setUp() {
	}
	
	@Test
	@DisplayName("Test y coordinates flipped")
	fun testCorners() {
		val plain = CoordinatePlain(0, 0, 5, 7)
		
		assertEquals(plain.topLeft, Coordinate(0, 0))
		assertEquals(plain.bottomLeft, Coordinate(0, 7))
		assertEquals(plain.topRight, Coordinate(5, 0))
		assertEquals(plain.bottomRight, Coordinate(5, 7))
		
		assertEquals(7.0, plain.height)
		assertEquals(5.0, plain.width)
	}
	
	@Test
	@DisplayName("Test x coordinates flipped")
	fun testXInWrongOrder() {
		assertFailsWith<IllegalArgumentException> {
			CoordinatePlain(5, 0, 0, 0)
		}
		assertFailsWith<IllegalArgumentException> {
			CoordinatePlain(Coordinate(5, 0), Coordinate(0, 0))
		}
	}
	
	@Test
	@DisplayName("Test y coordinates flipped")
	fun testYInWrongOrder() {
		assertFailsWith<IllegalArgumentException> {
			CoordinatePlain(0, 5, 0, 0)
		}
		assertFailsWith<IllegalArgumentException> {
			CoordinatePlain(Coordinate(0, 5), Coordinate(0, 0))
		}
	}
}