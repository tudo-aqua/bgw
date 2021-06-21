package layoutelements.gridlayoutview

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import tools.aqua.bgw.core.Alignment
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AddRowTest : GridLayoutViewTestBase() {
	
	@Test
	@DisplayName("Add row at start")
	fun testAddRowAtStart() {
		grid.addRows(0, 1)
		
		assertNull(grid[0, 0])
		assertNull(grid[1, 0])
		assertNull(grid[2, 0])
		
		assertEquals(grid.getCellCenterMode(0, 0), Alignment.CENTER)
		assertEquals(grid.getCellCenterMode(1, 0), Alignment.CENTER)
		assertEquals(grid.getCellCenterMode(2, 0), Alignment.CENTER)
		
		for (i in 0..2) {
			assertEquals(grid[i, 1], labels[i])
			assertEquals(grid[i, 2], buttons[i])
			assertEquals(grid[i, 3], colorPickers[i])
		}
		
		assertEquals(4, grid.rows)
		assertEquals(3, grid.columns)
	}
	
	@Test
	@DisplayName("Add row at end")
	fun testAddRowAtEnd() {
		grid.addRows(3, 1)
		
		for (i in 0..2) {
			assertEquals(grid[i, 0], labels[i])
			assertEquals(grid[i, 1], buttons[i])
			assertEquals(grid[i, 2], colorPickers[i])
		}
		
		assertNull(grid[0, 3])
		assertNull(grid[1, 3])
		assertNull(grid[2, 3])
		
		assertEquals(grid.getCellCenterMode(0, 3), Alignment.CENTER)
		assertEquals(grid.getCellCenterMode(1, 3), Alignment.CENTER)
		assertEquals(grid.getCellCenterMode(2, 3), Alignment.CENTER)
		
		assertEquals(4, grid.rows)
		assertEquals(3, grid.columns)
	}
	
	@Test
	@DisplayName("Add row in middle")
	fun testAddRowInMiddle() {
		grid.addRows(2, 1)
		
		//Row 0 unchanged
		assertEquals(grid[0, 0], labels[0])
		assertEquals(grid[1, 0], labels[1])
		assertEquals(grid[2, 0], labels[2])
		
		//Row 1 unchanged
		assertEquals(grid[0, 1], buttons[0])
		assertEquals(grid[1, 1], buttons[1])
		assertEquals(grid[2, 1], buttons[2])
		
		//Row 2 new and null initialized
		assertNull(grid[0, 2])
		assertNull(grid[1, 2])
		assertNull(grid[2, 2])
		
		assertEquals(grid.getCellCenterMode(0, 2), Alignment.CENTER)
		assertEquals(grid.getCellCenterMode(1, 2), Alignment.CENTER)
		assertEquals(grid.getCellCenterMode(2, 2), Alignment.CENTER)
		
		//Row 3 contains elements of previous row 2
		assertEquals(grid[0, 3], colorPickers[0])
		assertEquals(grid[1, 3], colorPickers[1])
		assertEquals(grid[2, 3], colorPickers[2])
		
		//Assert size
		assertEquals(4, grid.rows)
		assertEquals(3, grid.columns)
	}
}