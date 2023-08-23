import kotlinx.serialization.Serializable

@Serializable
sealed class Visual {
  val id : String = IDGenerator.generateVisualID()
  abstract fun copy(): Visual
}
