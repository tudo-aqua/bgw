package util.coordinates

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import tools.aqua.bgw.util.Coordinate
import kotlin.math.round
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class CoordinateTest {
	
	private lateinit var coordinateA: Coordinate
	private lateinit var coordinateB: Coordinate
	
	@BeforeEach
	fun setUp() {
		coordinateA = Coordinate(50, 100)
		coordinateB = Coordinate(-30, 25)
	}
	
	@Test
	@DisplayName("Test get")
	fun testGet() {
		assertEquals(50.0, coordinateA.xCoord)
		assertEquals(100.0, coordinateA.yCoord)
	}
	
	@Test
	@DisplayName("Test plus")
	fun testPlus() {
		val coordinateC = coordinateA + coordinateB
		assertEquals(20.0, coordinateC.xCoord)
		assertEquals(125.0, coordinateC.yCoord)
	}
	
	@Test
	@DisplayName("Test minus")
	fun testMinus() {
		val coordinateC = coordinateA - coordinateB
		assertEquals(80.0, coordinateC.xCoord)
		assertEquals(75.0, coordinateC.yCoord)
	}
	
	@Test
	@DisplayName("Test rotation")
	fun testRotation() {
		val coordinateC = coordinateA.rotated(90.0)
		assertEquals(-100.0, round(coordinateC.xCoord))
		assertEquals(50.0, round(coordinateC.yCoord))
	}
	
	@Test
	@DisplayName("Test rotation around pivot")
	fun testRotationPivot() {
		val coordinateC = coordinateA.rotated(90.0, Coordinate(50, 50))
		assertEquals(0.0, round(coordinateC.xCoord))
		assertEquals(50.0, round(coordinateC.yCoord))
	}
	
	@Test
	@DisplayName("Test equals and hashCode")
	fun testEquals() {
		assertEquals(coordinateA, Coordinate(coordinateA.xCoord, coordinateA.yCoord))
		assertEquals(coordinateA.hashCode(), Coordinate(coordinateA.xCoord, coordinateA.yCoord).hashCode())
		
		assertNotEquals(coordinateA, coordinateB)
		assertNotEquals(coordinateA.hashCode(), coordinateB.hashCode())
	}
}