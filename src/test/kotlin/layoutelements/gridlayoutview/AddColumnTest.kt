package layoutelements.gridlayoutview

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import tools.aqua.bgw.core.Alignment
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AddColumnTest : GridLayoutViewTestBase() {
	
	@Test
	@DisplayName("Add column at start")
	fun testAddColumnAtStart() {
		grid.addColumns(0, 1)
		
		assertNull(grid[0, 0])
		assertNull(grid[0, 1])
		assertNull(grid[0, 2])
		
		assertEquals(grid.getCellCenterMode(0, 0), Alignment.CENTER)
		assertEquals(grid.getCellCenterMode(0, 1), Alignment.CENTER)
		assertEquals(grid.getCellCenterMode(0, 2), Alignment.CENTER)
		
		for (i in 1..3) {
			assertEquals(grid[i, 0], labels[i - 1])
			assertEquals(grid[i, 1], buttons[i - 1])
			assertEquals(grid[i, 2], colorPickers[i - 1])
		}
		
		assertEquals(3, grid.rows)
		assertEquals(4, grid.columns)
	}
	
	@Test
	@DisplayName("Add column at end")
	fun testAddColumnAtEnd() {
		grid.addColumns(3, 1)
		
		for (i in 0..2) {
			assertEquals(grid[i, 0], labels[i])
			assertEquals(grid[i, 1], buttons[i])
			assertEquals(grid[i, 2], colorPickers[i])
		}
		
		assertNull(grid[3, 0])
		assertNull(grid[3, 1])
		assertNull(grid[3, 2])
		
		assertEquals(grid.getCellCenterMode(3, 0), Alignment.CENTER)
		assertEquals(grid.getCellCenterMode(3, 1), Alignment.CENTER)
		assertEquals(grid.getCellCenterMode(3, 2), Alignment.CENTER)
		
		assertEquals(3, grid.rows)
		assertEquals(4, grid.columns)
	}
	
	@Test
	@DisplayName("Add column in middle")
	fun testAddColumnInMiddle() {
		grid.addColumns(2, 1)
		
		//Column 0 unchanged
		assertEquals(grid[0, 0], labels[0])
		assertEquals(grid[0, 1], buttons[0])
		assertEquals(grid[0, 2], colorPickers[0])
		
		//Column 1 unchanged
		assertEquals(grid[1, 0], labels[1])
		assertEquals(grid[1, 1], buttons[1])
		assertEquals(grid[1, 2], colorPickers[1])
		
		//Column 2 new and null initialized
		assertNull(grid[2, 0])
		assertNull(grid[2, 1])
		assertNull(grid[2, 2])
		
		assertEquals(grid.getCellCenterMode(2, 0), Alignment.CENTER)
		assertEquals(grid.getCellCenterMode(2, 1), Alignment.CENTER)
		assertEquals(grid.getCellCenterMode(2, 2), Alignment.CENTER)
		
		//Column 3 contains elements of previous column 2
		assertEquals(grid[3, 0], labels[2])
		assertEquals(grid[3, 1], buttons[2])
		assertEquals(grid[3, 2], colorPickers[2])
		
		assertEquals(3, grid.rows)
		assertEquals(4, grid.columns)
	}
}