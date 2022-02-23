/*
 * Copyright 2021-2022 The BoardGameWork Authors
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
import tools.aqua.bgw.DOUBLE_TOLERANCE
import tools.aqua.bgw.util.Coordinate

/** Test coordinate functions */
class CoordinateTest {
  /** Coordinate A */
  private lateinit var coordinateA: Coordinate
  
  /** Coordinate B */
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
    assertEquals(-100.0, coordinateC.xCoord, DOUBLE_TOLERANCE)
    assertEquals(50.0, coordinateC.yCoord, DOUBLE_TOLERANCE)
  }

  @Test
  @DisplayName("Test rotation around pivot")
  fun testRotationPivot() {
    val coordinateC = coordinateA.rotated(90.0, Coordinate(50, 50))
    assertEquals(0.0, coordinateC.xCoord, DOUBLE_TOLERANCE)
    assertEquals(50.0, coordinateC.yCoord, DOUBLE_TOLERANCE)
  }

  @Test
  @DisplayName("Test equals and hashCode")
  fun testEquals() {
    assertEquals(coordinateA, Coordinate(coordinateA.xCoord, coordinateA.yCoord))
    assertEquals(
        coordinateA.hashCode(), Coordinate(coordinateA.xCoord, coordinateA.yCoord).hashCode())

    assertNotEquals(coordinateA, coordinateB)
    assertNotEquals(coordinateA.hashCode(), coordinateB.hashCode())
  }
}
