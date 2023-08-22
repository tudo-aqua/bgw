import kotlinx.serialization.Serializable

@Serializable
class Scene<T : ComponentView>(val width: Double, val height: Double, val background: Visual) {
  var components: MutableList<T> = mutableListOf()
}
