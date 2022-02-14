/*
 *    Copyright 2021 The BoardGameWork Authors
 *    SPDX-License-Identifier: Apache-2.0
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package tools.aqua.bgw.layoutelements.grid

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class RemoveEmptyColumnsTest : GridPaneTestBase() {
	
	@Test
	@DisplayName("Remove empty columns from full grid")
	fun testRemoveEmptyColumnsOnFullGrid() {
		grid.removeEmptyColumns()
		
		//Grid unchanged
		checkSize()
		testUnchanged()
	}
	
	@Test
	@DisplayName("Remove empty columns from partially full grid")
	fun testRemoveEmptyColumnsOnPartiallyFullGrid() {
		grid[1, 2] = null
		grid[2, 2] = null
		grid.removeEmptyColumns()
		
		checkSize()
		
		//Row 0-1 unchanged
		testUnchanged(rows = 0..1)
		
		//Row 2 unchanged
		assertEquals(contents[0][2], grid[0, 2])
		assertEquals(null, grid[1, 2])
		assertEquals(null, grid[2, 2])
	}
	
	@Test
	@DisplayName("Remove empty first column")
	fun testRemoveEmptyFirstColumn() {
		grid[0, 0] = null
		grid[0, 1] = null
		grid[0, 2] = null
		grid.removeEmptyColumns()
		
		checkSize(2, 3)
		
		//Columns 0-1 contain former columns 1-2
		testUnchanged(columns = 0..1, columnBias = 1)
	}
	
	@Test
	@DisplayName("Remove empty last column")
	fun testRemoveEmptyLastColumn() {
		grid[2, 0] = null
		grid[2, 1] = null
		grid[2, 2] = null
		grid.removeEmptyColumns()
		
		checkSize(2, 3)
		
		//Columns 0-1 unchanged
		testUnchanged(columns = 0..1)
	}
	
	@Test
	@DisplayName("Remove empty middle column")
	fun testRemoveEmptyMiddleColumn() {
		grid[1, 0] = null
		grid[1, 1] = null
		grid[1, 2] = null
		grid.removeEmptyColumns()
		
		checkSize(2, 3)
		
		//Column 0 unchanged
		testUnchanged(columns = 0..0)
		
		//Column 1 contains former column 2
		testUnchanged(columns = 1..1, columnBias = 1)
	}
	
	@Test
	@DisplayName("Remove empty columns from empty grid")
	fun testRemoveEmptyColumnsFromEmptyGrid() {
		for (i in 0..2) {
			for (j in 0..2) {
				grid[i, j] = null
			}
		}
		
		grid.removeEmptyColumns()
		
		checkSize(0, 0)
	}
}