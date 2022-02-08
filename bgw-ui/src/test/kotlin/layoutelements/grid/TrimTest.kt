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

package layoutelements.grid

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TrimTest : GridPaneTestBase() {
	
	@Test
	@DisplayName("Trim full grid")
	fun testTrimFullGrid() {
		grid.trim()
		
		//Grid unchanged
		checkSize()
		testUnchanged()
	}
	
	@Test
	@DisplayName("Trim partially full grid")
	fun testTrimPartiallyFullGrid() {
		grid[1, 2] = null
		grid[2, 2] = null
		grid.trim()
		
		//Row 0-1 unchanged
		checkSize()
		testUnchanged(rows = 0..1)
		
		//Row 2 unchanged
		assertEquals(contents[0][2], grid[0, 2])
		assertEquals(null, grid[1, 2])
		assertEquals(null, grid[2, 2])
	}
	
	@Test
	@DisplayName("Trim first row")
	fun testTrimFirstRow() {
		grid[0, 0] = null
		grid[1, 0] = null
		grid[2, 0] = null
		grid.trim()
		
		checkSize(3, 2)
		
		//Rows 0-1 contain former rows 1-2
		testUnchanged(rows = 0..1, rowBias = 1)
	}
	
	@Test
	@DisplayName("Trim first column")
	fun testTrimFirstColumn() {
		grid[0, 0] = null
		grid[0, 1] = null
		grid[0, 2] = null
		grid.trim()
		
		checkSize(2, 3)
		
		//Columns 0-1 contain former columns 1-2
		testUnchanged(columns = 0..1, columnBias = 1)
	}
	
	@Test
	@DisplayName("Trim last row")
	fun testTrimLastRow() {
		grid[0, 2] = null
		grid[1, 2] = null
		grid[2, 2] = null
		grid.trim()
		
		checkSize(3, 2)
		
		//Rows 0-1 unchanged
		testUnchanged(rows = 0..1)
	}
	
	@Test
	@DisplayName("Trim last column")
	fun testTrimLastColumn() {
		grid[2, 0] = null
		grid[2, 1] = null
		grid[2, 2] = null
		grid.trim()
		
		checkSize(2, 3)
		
		//Columns 0-1 unchanged
		testUnchanged(columns = 0..1)
	}
	
	@Test
	@DisplayName("Don't trim middle row")
	fun testDontTrimMiddleRow() {
		grid[1, 0] = null
		grid[1, 1] = null
		grid[1, 2] = null
		grid.trim()
		
		//Grid unchanged
		checkSize()
		testUnchanged(columns = 0..0)
		testNull(columns = 1..1)
		testUnchanged(columns = 2..2)
	}
	
	@Test
	@DisplayName("Don't trim middle column")
	fun testDontTrimMiddleColumn() {
		grid[0, 1] = null
		grid[1, 1] = null
		grid[2, 1] = null
		grid.trim()
		
		//Grid unchanged
		checkSize()
		testUnchanged(rows = 0..0)
		testNull(rows = 1..1)
		testUnchanged(rows = 2..2)
	}
}