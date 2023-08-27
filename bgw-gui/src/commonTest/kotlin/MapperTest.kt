import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.components.uicomponents.Label
import kotlin.test.Test
import kotlin.test.assertContentEquals

class MapperTest {
    @Test
    fun testGridPaneMapping() {
        val gridPane = GridPane<ComponentView>(columns = 5, rows = 5)
        repeat(gridPane.columns) { x ->
            repeat(gridPane.rows) { y ->
                val index = x + y * gridPane.columns
                gridPane[x, y] = Label(
                    width = 100 + (0..20).random(),
                    height = 100 + (0..20).random(),
                    text = "($x, $y)",
                ).apply {
                    onMouseClicked = {
                        gridPane.removeChild(this)
                    }
                }
            }
        }


        assertContentEquals(
            expected = gridPane.grid.clone().toList(),
            actual = gridPane.grid.toList()
        )
    }
}