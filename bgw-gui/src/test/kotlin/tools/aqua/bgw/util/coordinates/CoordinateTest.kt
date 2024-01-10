/*
 * Copyright 2021-2024 The BoardGameWork Authors
 * SPDX-License-Identifier: Apache-2.0
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tools.aqua.bgw.util.coordinates

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import tools.aqua.bgw.DOUBLE_TOLERANCE
import tools.aqua.bgw.util.Coordinate

/** Test Coordinate functions. */
class CoordinateTest {
  /** Coordinate A. */
  private lateinit var coordinateA: Coordinate

  /** Coordinate B. */
  private lateinit var coordinateB: Coordinate

  /** Resets coordinates before each run. */
  @BeforeEach
  fun setUp() {
    coordinateA = Coordinate(50, 100)
    coordinateB = Coordinate(-30, 25)
  }

  /** Tests constructor. */
  @Test
  @DisplayName("Test constructor")
  fun testConstructor() {
    val c00 = Coordinate()
    val c10 = Coordinate(xCoord = 1)
    val c01 = Coordinate(yCoord = 1)
    val c11 = Coordinate(xCoord = 1, yCoord = 1)

    assertEquals(0.0, c00.xCoord)
    assertEquals(0.0, c00.yCoord)

    assertEquals(1.0, c10.xCoord)
    assertEquals(0.0, c10.yCoord)

    assertEquals(0.0, c01.xCoord)
    assertEquals(1.0, c01.yCoord)

    assertEquals(1.0, c11.xCoord)
    assertEquals(1.0, c11.yCoord)
  }

  /** Tests getters of x and y coord. */
  @Test
  @DisplayName("Test get")
  fun testGet() {
    assertEquals(50.0, coordinateA.xCoord)
    assertEquals(100.0, coordinateA.yCoord)
  }

  /** Tests adding two coordinates. */
  @Test
  @DisplayName("Test plus")
  fun testPlus() {
    val coordinateC = coordinateA + coordinateB
    assertEquals(20.0, coordinateC.xCoord)
    assertEquals(125.0, coordinateC.yCoord)
  }

  /** Tests subtracting two coordinates. */
  @Test
  @DisplayName("Test minus")
  fun testMinus() {
    val coordinateC = coordinateA - coordinateB
    assertEquals(80.0, coordinateC.xCoord)
    assertEquals(75.0, coordinateC.yCoord)
  }

  /** Tests multiplying by scalar. */
  @Test
  @DisplayName("Test multiply")
  fun testMultiply() {
    val coordinateC = coordinateA * 2
    assertEquals(100.0, coordinateC.xCoord)
    assertEquals(200.0, coordinateC.yCoord)
  }

  /** Tests multiplying by zero. */
  @Test
  @DisplayName("Test multiply by zero")
  fun testMultiplyByZero() {
    val coordinateC = coordinateA * 0
    assertEquals(0.0, coordinateC.xCoord)
    assertEquals(0.0, coordinateC.yCoord)
  }

  /** Tests dividing by scalar. */
  @Test
  @DisplayName("Test divide")
  fun testDivide() {
    val coordinateC = coordinateA / 2
    assertEquals(25.0, coordinateC.xCoord)
    assertEquals(50.0, coordinateC.yCoord)
  }

  /** Tests dividing by zero. */
  @Test
  @DisplayName("Test divide by zero")
  fun testDividingByZero() {
    assertThrows<IllegalArgumentException> { coordinateA / 0 }
  }

  /** Tests rotation coordinates. */
  @Test
  @DisplayName("Test rotation")
  fun testRotation() {
    val coordinateC = coordinateA.rotated(90.0)
    assertEquals(-100.0, coordinateC.xCoord, DOUBLE_TOLERANCE)
    assertEquals(50.0, coordinateC.yCoord, DOUBLE_TOLERANCE)
  }

  /** Tests rotation coordinates around pivot. */
  @Test
  @DisplayName("Test rotation around pivot")
  fun testRotationPivot() {
    val coordinateC = coordinateA.rotated(90.0, Coordinate(50, 50))
    assertEquals(0.0, coordinateC.xCoord, DOUBLE_TOLERANCE)
    assertEquals(50.0, coordinateC.yCoord, DOUBLE_TOLERANCE)
  }

  /** Tests equals and hashCode on coordinates. */
  @Test
  @DisplayName("Test equals and hashCode")
  fun testEquals() {
    assertEquals(coordinateA, Coordinate(coordinateA.xCoord, coordinateA.yCoord))
    assertNotEquals(coordinateA, Coordinate(coordinateB.xCoord, coordinateA.yCoord))
    assertNotEquals(coordinateA, Coordinate(coordinateA.xCoord, coordinateB.yCoord))
    assertNotEquals(coordinateA, Coordinate(coordinateB.xCoord, coordinateB.yCoord))
    assertEquals(
        coordinateA.hashCode(), Coordinate(coordinateA.xCoord, coordinateA.yCoord).hashCode())

    assertNotEquals(coordinateA, Any())
    assertNotEquals(coordinateA, coordinateB)
    assertNotEquals(coordinateA.hashCode(), coordinateB.hashCode())
  }

  /** Tests toString. */
  @Test
  @DisplayName("Test toString")
  fun testToString() {
    assertEquals("X = 50.0, Y = 100.0", coordinateA.toString())
  }
}
