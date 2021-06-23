package layoutelements.gridlayoutview

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

class GrowTest : GridLayoutViewTestBase() {
	@Test
	@DisplayName("Grow to the left")
	fun testGrowLeft() {
		grid.grow(2, 0, 0, 0)
		
		checkSize(5, 3)
		
		testUnchanged(columns = 2..4, columnBias = -2)
	}
	
	@Test
	@DisplayName("Grow to the right")
	fun testGrowRight() {
		grid.grow(0, 2, 0, 0)
		
		checkSize(5, 3)
		
		testUnchanged()
	}
	
	@Test
	@DisplayName("Grow on the top")
	fun testGrowTop() {
		grid.grow(0, 0, 2, 0)
		
		checkSize(3, 5)
		
		testUnchanged(rows = 2..4, rowBias = -2)
	}
	
	@Test
	@DisplayName("Grow on the bottom")
	fun testGrowBottom() {
		grid.grow(0, 0, 0, 2)
		
		checkSize(3, 5)
		
		testUnchanged()
	}
	
	@Test
	@DisplayName("Grow on all sides")
	fun testGrowAllSides() {
		grid.grow(1, 1, 1, 1)
		
		checkSize(5, 5)
		
		testUnchanged(columns = 1..3, rows = 1..3, columnBias = -1, rowBias = -1)
	}
	
	@Test
	@DisplayName("Don't grow by passing 0")
	fun testDontGrow() {
		grid.grow(0, 0, 0, 0)
		
		checkSize()
		testUnchanged()
	}
	
	@Test
	@DisplayName("Don't grow to the left for negative parameter")
	fun testDontGrowLeftForNegativeParameter() {
		assertFailsWith<IllegalArgumentException> {
			grid.grow(-1, 0, 0, 0)
		}
		
		checkSize()
		testUnchanged()
	}
	
	@Test
	@DisplayName("Don't grow to the right for negative parameter")
	fun testDontGrowRightForNegativeParameter() {
		assertFailsWith<IllegalArgumentException> {
			grid.grow(0, -1, 0, 0)
		}
		
		checkSize()
		testUnchanged()
	}
	
	@Test
	@DisplayName("Don't grow on the top for negative parameter")
	fun testDontGrowTopForNegativeParameter() {
		assertFailsWith<IllegalArgumentException> {
			grid.grow(0, 0, -1, 0)
		}
		
		checkSize()
		testUnchanged()
	}
	
	@Test
	@DisplayName("Don't grow on the bottom for negative parameter")
	fun testDontGrowBottomForNegativeParameter() {
		assertFailsWith<IllegalArgumentException> {
			grid.grow(0, 0, 0, -1)
		}
		
		checkSize()
		testUnchanged()
	}
}