import kotlinx.serialization.Serializable

@Serializable
sealed class Visual {
  abstract fun copy(): Visual
}
