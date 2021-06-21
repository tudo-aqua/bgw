package layoutelements.gridlayoutview

import org.junit.jupiter.api.BeforeEach
import tools.aqua.bgw.elements.layoutviews.GridLayoutView
import tools.aqua.bgw.elements.uielements.Button
import tools.aqua.bgw.elements.uielements.ColorPicker
import tools.aqua.bgw.elements.uielements.Label
import tools.aqua.bgw.elements.uielements.UIElementView

abstract class GridLayoutViewTestBase {
	protected lateinit var grid: GridLayoutView<UIElementView>
	
	protected val labels = Array(3) { Label() }
	protected val buttons = Array(3) { Button() }
	protected val colorPickers = Array(3) { ColorPicker() }
	
	@BeforeEach
	fun setUp() {
		grid = GridLayoutView(3, 3)
		
		for (i in 0..2) {
			grid[i, 0] = labels[i]
			grid[i, 1] = buttons[i]
			grid[i, 2] = colorPickers[i]
		}
	}
}