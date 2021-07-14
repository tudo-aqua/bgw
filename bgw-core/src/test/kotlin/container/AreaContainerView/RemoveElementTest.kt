package container.AreaContainerView

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class RemoveElementTest: AreaContainerViewTestBase() {

    @Test
    @DisplayName("Removes an element")
    fun removeElement() {
        tokenAreaContainer.add(redToken)
        tokenAreaContainer.add(blueToken)
        tokenAreaContainer.add(greenToken)
        tokenAreaContainer.remove(redToken)
        assertEquals(listOf(blueToken, greenToken), tokenAreaContainer.elements)
        assertNull(redToken.parent)
        tokenAreaContainer.remove(redToken)
        assertEquals(listOf(blueToken, greenToken), tokenAreaContainer.elements)
    }

    @Test
    @DisplayName("Remove all Elements")
    fun removeAllElements() {
        tokenAreaContainer.add(redToken)
        tokenAreaContainer.add(blueToken)
        val result = tokenAreaContainer.removeAll()
        assertEquals(listOf(redToken, blueToken), result)
        assertNull(redToken.parent)
        assertNull(blueToken.parent)
    }
}