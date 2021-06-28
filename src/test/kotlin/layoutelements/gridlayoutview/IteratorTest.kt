package layoutelements.gridlayoutview

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import tools.aqua.bgw.elements.layoutviews.GridLayoutView
import tools.aqua.bgw.elements.uielements.UIElementView
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IteratorTest : GridLayoutViewTestBase() {
	@Test
	@DisplayName("Iterator order test")
	fun testIteratorFullOrder() {
		val order = Stack<UIElementView>()
		for (i in 0..2) {
			for (j in 0..2) {
				order.push(contents[i][j])
			}
		}
		order.reverse()
		
		val iterator = grid.iterator()
		repeat(9) {
			assertTrue { iterator.hasNext() }
			
			val nextItem = iterator.next()
			
			assertEquals(it / 3, nextItem.columnIndex)
			assertEquals(it % 3, nextItem.rowIndex)
			assertEquals(order.pop(), nextItem.element)
		}
		
		assertFalse { iterator.hasNext() }
		assertFailsWith<NoSuchElementException> { iterator.next() }
	}
	
	@Test
	@DisplayName("Iterator test on empty grid")
	fun testIteratorOnEmptyGrid() {
		val emptyGrid = GridLayoutView<UIElementView>(0, 0, 0, 0)
		
		val iterator = emptyGrid.iterator()
		
		assertFalse { iterator.hasNext() }
		assertFailsWith<NoSuchElementException> { iterator.next() }
	}
}