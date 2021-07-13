package observable.observablelist

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AddTest : ObservableListTestBase() {
	@Test
	@DisplayName("Test add")
	fun testAdd() {
		list.add(42)
		
		for (i in 0 until list.size - 1)
			assertEquals(unordered[i], list[i])
		assertEquals(42, list[5])
		
		checkNotified()
	}
	
	@Test
	@DisplayName("Test add at index 0")
	fun testAddIndexAtStart() {
		list.add(0, 42)
		
		for (i in 0 until list.size - 1)
			assertEquals(unordered[i], list[i + 1])
		assertEquals(42, list[0])
		
		checkNotified()
	}
	
	@Test
	@DisplayName("Test add at last index")
	fun testAddIndexAtEnd() {
		list.add(5, 42)
		
		for (i in 0 until list.size - 1)
			assertEquals(unordered[i], list[i])
		assertEquals(42, list[5])
		
		checkNotified()
	}
	
	@Test
	@DisplayName("Test add at index out of bounds")
	fun testAddIndexOutOfBounds() {
		assertFailsWith<IndexOutOfBoundsException> {
			list.add(-1, 42)
		}
		
		assertFailsWith<IndexOutOfBoundsException> {
			list.add(6, 42)
		}
	}
	
	@Test
	@DisplayName("Test add all")
	fun testAddAll() {
		//13,25,17,13,-4
		val resultList = listOf(13, 25, 17, 13, -4, -1, -2)
		
		list.addAll(listOf(-1, -2))
		
		for (i in list.indices)
			assertEquals(resultList[i], list[i])
		
		checkNotified()
	}
	
	@Test
	@DisplayName("Test add all at index")
	fun testAddAllAtIndex() {
		//13,25,17,13,-4
		val resultList = listOf(13, 25, -1, -2, 17, 13, -4)
		
		list.addAll(2, listOf(-1, -2))
		
		for (i in list.indices)
			assertEquals(resultList[i], list[i])
		
		checkNotified()
	}
	
	@Test
	@DisplayName("Test add all at index out of bounds")
	fun testAddAllAtIndexOutOfBounds() {
		assertFailsWith<IndexOutOfBoundsException> {
			list.addAll(-1, listOf(-1, -2))
		}
		
		assertFailsWith<IndexOutOfBoundsException> {
			list.addAll(6, listOf(-1, -2))
		}
	}
}