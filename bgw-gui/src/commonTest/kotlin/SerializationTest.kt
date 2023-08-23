import kotlin.test.Test
import kotlinx.serialization.encodeToString
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.visual.ColorVisual

class SerializationTest {

    val scene = BoardGameScene(1920.0, 1080.0, ColorVisual.WHITE).apply {
        val label = Label(visual= ColorVisual.RED, width = 200, height = 200, text = "Hello, SoPra!")
        val label2 = Label(posX=200, posY=200, visual= ColorVisual.BLUE, width = 200, height = 200, text = "Hello, SoPra!")
        addComponents(label, label2)
    }

    @Test
    fun testSerialization() {
        val json = mapper.encodeToString(SceneMapper.map(scene))
        println(json)
    }
}