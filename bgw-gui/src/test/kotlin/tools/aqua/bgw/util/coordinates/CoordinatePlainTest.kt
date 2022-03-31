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

import kotlin.math.sqrt
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import tools.aqua.bgw.DOUBLE_TOLERANCE
import tools.aqua.bgw.util.Coordinate
import tools.aqua.bgw.util.CoordinatePlain

/** Test CoordinatePlain functions. */
class CoordinatePlainTest {

  /** Test corner coordinates. */
  @Test
  @DisplayName("Test corner coordinates")
  fun testCorners() {
    val plain = CoordinatePlain(0, 0, 5, 7)

    assertEquals(plain.topLeft, Coordinate(0, 0))
    assertEquals(plain.bottomLeft, Coordinate(0, 7))
    assertEquals(plain.topRight, Coordinate(5, 0))
    assertEquals(plain.bottomRight, Coordinate(5, 7))

    assertEquals(7.0, plain.height)
    assertEquals(5.0, plain.width)
  }

  /** Test x coordinates flipped. */
  @Test
  @DisplayName("Test x coordinates flipped")
  fun testXInWrongOrder() {
    assertThrows<IllegalArgumentException> { CoordinatePlain(5, 0, 0, 0) }
    assertThrows<IllegalArgumentException> { CoordinatePlain(Coordinate(5, 0), Coordinate(0, 0)) }
  }

  /** Test y coordinates flipped. */
  @Test
  @DisplayName("Test y coordinates flipped")
  fun testYInWrongOrder() {
    assertThrows<IllegalArgumentException> { CoordinatePlain(0, 5, 0, 0) }
    assertThrows<IllegalArgumentException> { CoordinatePlain(Coordinate(0, 5), Coordinate(0, 0)) }
  }

  /** Test rotation by 45 degrees. */
  @Test
  @DisplayName("Test rotation by 45 degrees")
  fun testRotation45Degrees() {
    // Box is 50 x 50
    val plainDimens = 50.0
    val plainPos = 100.0
    val expectedDimens = sqrt(plainDimens * plainDimens + plainDimens * plainDimens)

    val plain = CoordinatePlain(plainPos, plainPos, plainPos + plainDimens, plainPos + plainDimens)

    val rotated =
        plain.rotated(45, Coordinate(plainPos + plainDimens / 2, plainPos + plainDimens / 2))

    // Assert size
    assertEquals(expectedDimens, rotated.width, DOUBLE_TOLERANCE)
    assertEquals(expectedDimens, rotated.height, DOUBLE_TOLERANCE)

    // Assert corners
    val expectedLow = plainPos + plainDimens / 2 - expectedDimens / 2
    val expectedCenter = plainPos + plainDimens / 2
    val expectedHigh = plainPos + plainDimens / 2 + expectedDimens / 2

    assertEquals(expectedCenter, rotated.topLeft.xCoord, DOUBLE_TOLERANCE)
    assertEquals(expectedLow, rotated.topLeft.yCoord, DOUBLE_TOLERANCE)

    assertEquals(expectedHigh, rotated.topRight.xCoord, DOUBLE_TOLERANCE)
    assertEquals(expectedCenter, rotated.topRight.yCoord, DOUBLE_TOLERANCE)

    assertEquals(expectedCenter, rotated.bottomRight.xCoord, DOUBLE_TOLERANCE)
    assertEquals(expectedHigh, rotated.bottomRight.yCoord, DOUBLE_TOLERANCE)

    assertEquals(expectedLow, rotated.bottomLeft.xCoord, DOUBLE_TOLERANCE)
    assertEquals(expectedCenter, rotated.bottomLeft.yCoord, DOUBLE_TOLERANCE)

    // Assert bounds
    assertEquals(expectedLow, rotated.topLeftBound.xCoord, DOUBLE_TOLERANCE)
    assertEquals(expectedLow, rotated.topLeftBound.yCoord, DOUBLE_TOLERANCE)

    assertEquals(expectedHigh, rotated.topRightBound.xCoord, DOUBLE_TOLERANCE)
    assertEquals(expectedLow, rotated.topRightBound.yCoord, DOUBLE_TOLERANCE)

    assertEquals(expectedHigh, rotated.bottomRightBound.xCoord, DOUBLE_TOLERANCE)
    assertEquals(expectedHigh, rotated.bottomRightBound.yCoord, DOUBLE_TOLERANCE)

    assertEquals(expectedLow, rotated.bottomLeftBound.xCoord, DOUBLE_TOLERANCE)
    assertEquals(expectedHigh, rotated.bottomLeftBound.yCoord, DOUBLE_TOLERANCE)
  }

  /** Test rotation by 90 degrees on square. */
  @Test
  @DisplayName("Test rotation by 90 on square")
  fun testRotation90DegreesSquare() {
    // Box is 50 x 50
    val plain = CoordinatePlain(100, 100, 150, 150)
    val rotated = plain.rotated(90, Coordinate(125, 125))

    // Assert size
    assertEquals(plain.width, rotated.width, DOUBLE_TOLERANCE)
    assertEquals(plain.height, rotated.height, DOUBLE_TOLERANCE)

    // Assert corners
    assertEquals(plain.topLeft.xCoord, rotated.bottomLeft.xCoord, DOUBLE_TOLERANCE)
    assertEquals(plain.topLeft.yCoord, rotated.bottomLeft.yCoord, DOUBLE_TOLERANCE)

    assertEquals(plain.topRight.xCoord, rotated.topLeft.xCoord, DOUBLE_TOLERANCE)
    assertEquals(plain.topRight.yCoord, rotated.topLeft.yCoord, DOUBLE_TOLERANCE)

    assertEquals(plain.bottomRight.xCoord, rotated.topRight.xCoord, DOUBLE_TOLERANCE)
    assertEquals(plain.bottomRight.yCoord, rotated.topRight.yCoord, DOUBLE_TOLERANCE)

    assertEquals(plain.bottomLeft.xCoord, rotated.bottomRight.xCoord, DOUBLE_TOLERANCE)
    assertEquals(plain.bottomLeft.yCoord, rotated.bottomRight.yCoord, DOUBLE_TOLERANCE)

    // Assert bounds
    assertEquals(plain.topLeftBound.xCoord, rotated.topLeftBound.xCoord, DOUBLE_TOLERANCE)
    assertEquals(plain.topLeftBound.yCoord, rotated.topLeftBound.yCoord, DOUBLE_TOLERANCE)

    assertEquals(plain.topRightBound.xCoord, rotated.topRightBound.xCoord, DOUBLE_TOLERANCE)
    assertEquals(plain.topRightBound.yCoord, rotated.topRightBound.yCoord, DOUBLE_TOLERANCE)

    assertEquals(plain.bottomRightBound.xCoord, rotated.bottomRightBound.xCoord, DOUBLE_TOLERANCE)
    assertEquals(plain.bottomRightBound.yCoord, rotated.bottomRightBound.yCoord, DOUBLE_TOLERANCE)

    assertEquals(plain.bottomLeftBound.xCoord, rotated.bottomLeftBound.xCoord, DOUBLE_TOLERANCE)
    assertEquals(plain.bottomLeftBound.yCoord, rotated.bottomLeftBound.yCoord, DOUBLE_TOLERANCE)
  }

  /** Test rotation by 90 on rectangle. */
  @Test
  @DisplayName("Test rotation by 90 on rectangle")
  fun testRotation90DegreesRect() {
    // Box is 100 x 50
    val plainWidth = 100.0
    val plainHeight = 50.0
    val plainPos = 100.0

    val plain = CoordinatePlain(plainPos, plainPos, plainPos + plainWidth, plainPos + plainHeight)

    val rotated =
        plain.rotated(90, Coordinate(plainPos + plainWidth / 2, plainPos + plainHeight / 2))

    // Assert size
    assertEquals(plain.height, rotated.width, DOUBLE_TOLERANCE)
    assertEquals(plain.width, rotated.height, DOUBLE_TOLERANCE)

    // Assert corners
    val expectedLowX = plainPos + (plainWidth - plainHeight) / 2
    val expectedHighX = plainPos + plainWidth - (plainWidth - plainHeight) / 2

    val expectedLowY = plainPos - (plainWidth - plainHeight) / 2
    val expectedHighY = plainPos + plainHeight + (plainWidth - plainHeight) / 2

    assertEquals(expectedHighX, rotated.topLeft.xCoord, DOUBLE_TOLERANCE)
    assertEquals(expectedLowY, rotated.topLeft.yCoord, DOUBLE_TOLERANCE)

    assertEquals(expectedHighX, rotated.topRight.xCoord, DOUBLE_TOLERANCE)
    assertEquals(expectedHighY, rotated.topRight.yCoord, DOUBLE_TOLERANCE)

    assertEquals(expectedLowX, rotated.bottomRight.xCoord, DOUBLE_TOLERANCE)
    assertEquals(expectedHighY, rotated.bottomRight.yCoord, DOUBLE_TOLERANCE)

    assertEquals(expectedLowX, rotated.bottomLeft.xCoord, DOUBLE_TOLERANCE)
    assertEquals(expectedLowY, rotated.bottomLeft.yCoord, DOUBLE_TOLERANCE)

    // Assert bounds
    assertEquals(expectedLowX, rotated.topLeftBound.xCoord, DOUBLE_TOLERANCE)
    assertEquals(expectedLowY, rotated.topLeftBound.yCoord, DOUBLE_TOLERANCE)

    assertEquals(expectedHighX, rotated.topRightBound.xCoord, DOUBLE_TOLERANCE)
    assertEquals(expectedLowY, rotated.topRightBound.yCoord, DOUBLE_TOLERANCE)

    assertEquals(expectedHighX, rotated.bottomRightBound.xCoord, DOUBLE_TOLERANCE)
    assertEquals(expectedHighY, rotated.bottomRightBound.yCoord, DOUBLE_TOLERANCE)

    assertEquals(expectedLowX, rotated.bottomLeftBound.xCoord, DOUBLE_TOLERANCE)
    assertEquals(expectedHighY, rotated.bottomLeftBound.yCoord, DOUBLE_TOLERANCE)
  }
}
