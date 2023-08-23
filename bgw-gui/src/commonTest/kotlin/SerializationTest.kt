import kotlin.test.Test
import kotlinx.serialization.encodeToString
class SerializationTest {

    val scene = Scene<ComponentView>(1920.0, 1080.0, ColorVisual.WHITE).apply {
        components.add(Button(0.0, 0.0, 100.0, 100.0, ColorVisual.BLUE))
        components.add(Button(100.0, 100.0, 100.0, 100.0, ColorVisual.GREEN))
    }

    @Test
    fun testSerialization() {
        val json = mapper.encodeToString(SceneMapper.map(scene))
        println(json)
    }
}