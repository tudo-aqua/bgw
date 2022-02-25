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

package tools.aqua.bgw.layoutelements.grid

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import tools.aqua.bgw.components.layoutviews.GridPane.Companion.COLUMN_WIDTH_AUTO
import tools.aqua.bgw.components.layoutviews.GridPane.Companion.ROW_HEIGHT_AUTO
import tools.aqua.bgw.components.uicomponents.Label

/** Test get set functions on Grid. */
class GetSetTest : GridPaneTestBase() {
  
  /** Get and set. */
  @Test
  @DisplayName("Get and set")
  fun testGetSet() {
    val item = Label()
    grid[1, 2] = item
    assertEquals(item, grid[1, 2])
  }
  
  /** Get and set null. */
  @Test
  @DisplayName("Get and set null")
  fun testGetSetNull() {
    grid[1, 2] = null
    assertNull(grid[1, 2])
  }
  
  /** Get and set spacing. */
  @Test
  @DisplayName("Get and set spacing")
  fun testGetSetSpacing() {
    grid.spacing = 42.0
    assertEquals(42.0, grid.spacing)
  }
  
  /** Get and set spacing to zero. */
  @Test
  @DisplayName("Get and set spacing to zero")
  fun testGetSetSpacingZero() {
    grid.spacing = 0.0
    assertEquals(0.0, grid.spacing)
  }
  
  /** Get and set spacing negative. */
  @Test
  @DisplayName("Get and set spacing negative")
  fun testGetSetSpacingNegative() {
    assertThrows<IllegalArgumentException> { grid.spacing = -1.0 }
  }
  
  /** Set column width. */
  @Test
  @DisplayName("Set column width")
  fun testGetSetColumnWidth() {
    grid.setColumnWidth(1, 42.0)

    assertEquals(COLUMN_WIDTH_AUTO, grid.getColumnWidth(0))
    assertEquals(42.0, grid.getColumnWidth(1))
    assertEquals(COLUMN_WIDTH_AUTO, grid.getColumnWidth(2))
  }
  
  /** Set column widths. */
  @Test
  @DisplayName("Set column widths")
  fun testGetSetColumnWidths() {
    grid.setColumnWidths(DoubleArray(3) { 42.0 })

    for (i in 0..2) {
      assertEquals(42.0, grid.getColumnWidth(i))
    }
  }
  
  /** Set auto column width. */
  @Test
  @DisplayName("Set auto column width")
  fun testGetSetAutoColumnWidth() {
    grid.setColumnWidths(DoubleArray(3) { 42.0 })
    grid.setAutoColumnWidth(1)

    assertEquals(42.0, grid.getColumnWidth(0))
    assertEquals(COLUMN_WIDTH_AUTO, grid.getColumnWidth(1))
    assertEquals(42.0, grid.getColumnWidth(2))
  }
  
  /** Set auto column widths. */
  @Test
  @DisplayName("Set auto column widths")
  fun testGetSetAutoColumnWidths() {
    grid.setColumnWidths(DoubleArray(3) { 42.0 })
    grid.setAutoColumnWidths()

    for (i in 0..2) {
      assertEquals(COLUMN_WIDTH_AUTO, grid.getColumnWidth(i))
    }
  }
  
  /** Set column width to zero. */
  @Test
  @DisplayName("Set column width to zero")
  fun testGetSetColumnWidthZero() {
    grid.setColumnWidth(1, 0)
  }
  
  /** Set column width to minus one. */
  @Test
  @DisplayName("Set column width to minus one")
  fun testGetSetColumnWidthMinusOne() {
    grid.setColumnWidth(1, -1)
  }
  
  /** Set column width negative. */
  @Test
  @DisplayName("Set column width negative")
  fun testGetSetColumnWidthNegative() {
    assertThrows<IllegalArgumentException> { grid.setColumnWidth(1, -0.5) }

    assertThrows<IllegalArgumentException> { grid.setColumnWidth(1, -42) }
  }
  
  /** Set row height. */
  @Test
  @DisplayName("Set row height")
  fun testGetSetRowHeight() {
    grid.setRowHeight(1, 42.0)

    assertEquals(ROW_HEIGHT_AUTO, grid.getRowHeight(0))
    assertEquals(42.0, grid.getRowHeight(1))
    assertEquals(ROW_HEIGHT_AUTO, grid.getRowHeight(2))
  }
  
  /** Set row heights. */
  @Test
  @DisplayName("Set row heights")
  fun testGetSetRowHeights() {
    grid.setRowHeights(DoubleArray(3) { 42.0 })

    for (i in 0..2) {
      assertEquals(42.0, grid.getRowHeight(i), "index $i")
    }
  }
  
  /** Set auto row height. */
  @Test
  @DisplayName("Set auto row height")
  fun testGetSetAutoRowHeight() {
    grid.setRowHeights(DoubleArray(3) { 42.0 })
    grid.setAutoRowHeight(1)

    assertEquals(42.0, grid.getRowHeight(0))
    assertEquals(ROW_HEIGHT_AUTO, grid.getRowHeight(1))
    assertEquals(42.0, grid.getRowHeight(2))
  }
  
  /** Set auto row heights. */
  @Test
  @DisplayName("Set auto row heights")
  fun testGetSetAutoRowHeights() {
    grid.setRowHeights(DoubleArray(3) { 42.0 })
    grid.setAutoRowHeights()

    for (i in 0..2) {
      assertEquals(ROW_HEIGHT_AUTO, grid.getRowHeight(i))
    }
  }
  
  /** Set row height to zero. */
  @Test
  @DisplayName("Set row height to zero")
  fun testGetSetRowHeightZero() {
    grid.setRowHeight(1, 0)
  }
  
  /** Set row height to minus one. */
  @Test
  @DisplayName("Set row height to minus one")
  fun testGetSetRowHeightMinusOne() {
    grid.setRowHeight(1, -1)
  }
  
  /** Set row height negative. */
  @Test
  @DisplayName("Set row height negative")
  fun testGetSetRowHeightNegative() {
    assertThrows<IllegalArgumentException> { grid.setRowHeight(1, -0.5) }

    assertThrows<IllegalArgumentException> { grid.setRowHeight(1, -42) }
  }
}
