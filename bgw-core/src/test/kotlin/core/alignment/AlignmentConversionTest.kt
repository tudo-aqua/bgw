package core.alignment

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.HorizontalAlignment
import tools.aqua.bgw.core.VerticalAlignment
import kotlin.test.assertEquals

class AlignmentConversionTest {
    @Test
    @DisplayName("Test the horizontalAlignment field of Alignment")
    fun toHorizontal() {
        assertEquals(HorizontalAlignment.LEFT, Alignment.TOP_LEFT.horizontalAlignment)
        assertEquals(HorizontalAlignment.RIGHT, Alignment.TOP_RIGHT.horizontalAlignment)
        assertEquals(HorizontalAlignment.CENTER, Alignment.TOP_CENTER.horizontalAlignment)
        assertEquals(HorizontalAlignment.LEFT, Alignment.BOTTOM_LEFT.horizontalAlignment)
        assertEquals(HorizontalAlignment.RIGHT, Alignment.BOTTOM_RIGHT.horizontalAlignment)
        assertEquals(HorizontalAlignment.CENTER, Alignment.BOTTOM_CENTER.horizontalAlignment)
        assertEquals(HorizontalAlignment.LEFT, Alignment.CENTER_LEFT.horizontalAlignment)
        assertEquals(HorizontalAlignment.RIGHT, Alignment.CENTER_RIGHT.horizontalAlignment)
        assertEquals(HorizontalAlignment.CENTER, Alignment.CENTER.horizontalAlignment)
    }

    @Test
    @DisplayName("Test the verticalAlignment field of Alignment")
    fun toVertical() {
        assertEquals(VerticalAlignment.TOP, Alignment.TOP_LEFT.verticalAlignment)
        assertEquals(VerticalAlignment.TOP, Alignment.TOP_RIGHT.verticalAlignment)
        assertEquals(VerticalAlignment.TOP, Alignment.TOP_CENTER.verticalAlignment)
        assertEquals(VerticalAlignment.BOTTOM, Alignment.BOTTOM_LEFT.verticalAlignment)
        assertEquals(VerticalAlignment.BOTTOM, Alignment.BOTTOM_RIGHT.verticalAlignment)
        assertEquals(VerticalAlignment.BOTTOM, Alignment.BOTTOM_CENTER.verticalAlignment)
        assertEquals(VerticalAlignment.CENTER, Alignment.CENTER_LEFT.verticalAlignment)
        assertEquals(VerticalAlignment.CENTER, Alignment.CENTER_RIGHT.verticalAlignment)
        assertEquals(VerticalAlignment.CENTER, Alignment.CENTER.verticalAlignment)
    }
}