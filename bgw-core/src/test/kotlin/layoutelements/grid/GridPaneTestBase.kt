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

import org.junit.jupiter.api.BeforeEach
import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.ColorPicker
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.UIComponent
import tools.aqua.bgw.core.Alignment
import kotlin.test.assertEquals
import kotlin.test.assertNull

abstract class GridPaneTestBase {
	protected lateinit var grid: GridPane<UIComponent>
	protected lateinit var centerings: Array<Array<Alignment>>
	
	protected val labels = Array(3) { Label() }
	protected val buttons = Array(3) { Button() }
	protected val colorPickers = Array(3) { ColorPicker() }
	protected val contents = arrayOf(labels, buttons, colorPickers)
	
	@BeforeEach
	fun setUp() {
		grid = GridPane(3, 3)
		centerings = Array(3) { Array(3) { Alignment.CENTER } }
		centerings[0][0] = Alignment.CENTER_LEFT
		centerings[0][1] = Alignment.BOTTOM_RIGHT
		centerings[1][1] = Alignment.TOP_RIGHT
		centerings[1][2] = Alignment.CENTER_RIGHT
		
		for (i in 0..2) {
			for (j in 0..2) {
				grid[i, j] = contents[i][j]
				grid.setCellCenterMode(i, j, centerings[i][j])
			}
		}
	}
	
	protected fun testUnchanged(
		columns: IntRange = 0..2,
		rows: IntRange = 0..2,
		columnBias: Int = 0,
		rowBias: Int = 0
	) {
		for (i in columns) {
			for (j in rows) {
				assertEquals(contents[i + columnBias][j + rowBias], grid[i, j])
				assertEquals(centerings[i + columnBias][j + rowBias], grid.getCellCenterMode(i, j))
			}
		}
	}
	
	protected fun testNull(columns: IntRange = 0..2, rows: IntRange = 0..2) {
		for (i in columns) {
			for (j in rows) {
				assertNull(grid[i, j])
			}
		}
	}
	
	protected fun checkSize(columns: Int = 3, rows: Int = 3) {
		assertEquals(columns, grid.columns)
		assertEquals(rows, grid.rows)
	}
}