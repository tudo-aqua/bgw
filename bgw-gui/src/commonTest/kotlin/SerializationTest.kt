import kotlin.test.Test
import kotlinx.serialization.encodeToString
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.layoutviews.CameraPane
import tools.aqua.bgw.components.layoutviews.LayoutView
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.event.MouseButtonType
import tools.aqua.bgw.visual.ColorVisual

class SerializationTest {

    val scene = BoardGameScene(1920.0, 1080.0, ColorVisual.WHITE).apply {
        val label = Label(visual= ColorVisual.RED, width = 200, height = 200, text = "Hello, SoPra!")
        val label2 = Label(posX=200, posY=200, visual= ColorVisual.BLUE, width = 200, height = 200, text = "Hello, SoPra!")
        val pane = Pane<ComponentView>(visual= ColorVisual.GREEN, width = 200, height = 200).apply {
            addAll(label, label2)
        }
        val cameraPane = CameraPane<LayoutView<*>>(visual= ColorVisual.YELLOW, width = 200, height = 200, target = pane)
        addComponents(cameraPane)
    }

    val mouseEventData = MouseEventData( id = "componentView.id",
            posX = 100.0,
            posY = 100.0,
            button = MouseButtonType.LEFT_BUTTON)
    @Test
    fun testSerialization() {
        val json = jsonMapper.encodeToString(mouseEventData)
        println(json)
    }
}