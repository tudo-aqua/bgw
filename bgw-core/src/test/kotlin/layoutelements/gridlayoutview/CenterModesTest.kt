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

package layoutelements.gridlayoutview

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import tools.aqua.bgw.core.Alignment
import kotlin.test.assertEquals

class CenterModesTest : GridLayoutViewTestBase() {
	@Test
	@DisplayName("Set single cell's center mode")
	fun testSetSingleCenterMode() {
		grid.setCellCenterMode(1, 2, Alignment.TOP_LEFT)
		
		assertEquals(Alignment.TOP_LEFT, grid.getCellCenterMode(1, 2))
	}
	
	@Test
	@DisplayName("Set row's center mode")
	fun testSetRowCenterMode() {
		grid.setRowCenterMode(1, Alignment.TOP_LEFT)
		
		testUnchanged(rows = 0..0)
		for (i in 0..2) {
			assertEquals(contents[i][1], grid[i, 1])
			assertEquals(Alignment.TOP_LEFT, grid.getCellCenterMode(i, 1))
		}
		testUnchanged(rows = 2..2)
	}
	
	@Test
	@DisplayName("Set columns's center mode")
	fun testSetColumnCenterMode() {
		grid.setColumnCenterMode(1, Alignment.TOP_LEFT)
		
		testUnchanged(columns = 0..0)
		for (j in 0..2) {
			assertEquals(contents[1][j], grid[1, j])
			assertEquals(Alignment.TOP_LEFT, grid.getCellCenterMode(1, j))
		}
		testUnchanged(columns = 2..2)
	}
	
	@Test
	@DisplayName("Set global center mode")
	fun testSetGlobalCenterMode() {
		grid.setCenterMode(Alignment.TOP_LEFT)
		
		for (i in 0..2) {
			for (j in 0..2) {
				assertEquals(contents[i][j], grid[i, j])
				assertEquals(Alignment.TOP_LEFT, grid.getCellCenterMode(i, j))
			}
		}
	}
}