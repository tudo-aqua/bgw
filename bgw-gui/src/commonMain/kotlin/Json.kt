import kotlinx.serialization.encodeToString
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import kotlin.reflect.KClass
import kotlinx.serialization.json.Json as KJson

private val module = SerializersModule {
    polymorphic(ComponentViewData::class) {
        subclass(ButtonData::class)
        subclass(LabelData::class)
    }
    polymorphic(VisualData::class) {
        subclass(ColorVisualData::class)
        subclass(ImageVisualData::class)
        subclass(TextVisualData::class)
        subclass(CompoundVisualData::class)
    }
}

val mapper = KJson { serializersModule = module }
