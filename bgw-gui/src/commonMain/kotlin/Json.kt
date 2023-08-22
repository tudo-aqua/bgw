import kotlinx.serialization.encodeToString
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import kotlin.reflect.KClass
import kotlinx.serialization.json.Json as KJson

private val module = SerializersModule {
    polymorphic(ComponentView::class) {
        subclass(Button::class)
    }
}

val mapper = KJson { serializersModule = module }
