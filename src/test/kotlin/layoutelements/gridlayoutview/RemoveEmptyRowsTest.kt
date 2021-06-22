package layoutelements.gridlayoutview

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class RemoveEmptyRowsTest : GridLayoutViewTestBase() {
	
	@Test
	@DisplayName("Remove empty rows from full grid")
	fun testRemoveEmptyRowsOnFullGrid() {
		grid.removeEmptyRows()
		
		//Grid unchanged
		checkSize()
		testUnchanged()
	}
	
	@Test
	@DisplayName("Remove empty rows from partially full grid")
	fun testRemoveEmptyRowsOnPartiallyFullGrid() {
		grid[1, 2] = null
		grid[2, 2] = null
		grid.removeEmptyRows()
		
		checkSize()
		
		//Row 0-1 unchanged
		testUnchanged(rows = 0..1)
		
		//Row 2 unchanged
		assertEquals(contents[0][2], grid[0, 2])
		assertEquals(null, grid[1, 2])
		assertEquals(null, grid[2, 2])
	}
	
	@Test
	@DisplayName("Remove empty first row")
	fun testRemoveEmptyFirstRow() {
		grid[0, 0] = null
		grid[1, 0] = null
		grid[2, 0] = null
		grid.removeEmptyRows()
		
		checkSize(3, 2)
		
		//Rows 0-1 contain former rows 1-2
		testUnchanged(rows = 0..1, rowBias = 1)
	}
	
	@Test
	@DisplayName("Remove empty last row")
	fun testRemoveEmptyLastRow() {
		grid[0, 2] = null
		grid[1, 2] = null
		grid[2, 2] = null
		grid.removeEmptyRows()
		
		checkSize(3, 2)
		
		//Columns 0-1 unchanged
		testUnchanged(rows = 0..1)
	}
	
	@Test
	@DisplayName("Remove empty middle row")
	fun testRemoveEmptyMiddleRow() {
		grid[0, 1] = null
		grid[1, 1] = null
		grid[2, 1] = null
		grid.removeEmptyRows()
		
		checkSize(3, 2)
		
		//Column 0 unchanged
		testUnchanged(rows = 0..0)
		
		//Column 1 contains former column 2
		testUnchanged(rows = 1..1, columnBias = 1)
	}
	
	@Test
	@DisplayName("Remove empty rows from empty grid")
	fun testRemoveEmptyRowsFromEmptyGrid() {
		for (i in 0..2) {
			for (j in 0..2) {
				grid[i, j] = null
			}
		}
		
		grid.removeEmptyRows()
		
		checkSize(0, 0)
	}
}