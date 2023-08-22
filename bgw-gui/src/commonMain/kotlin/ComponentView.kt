import kotlinx.serialization.Serializable

@Serializable
sealed class ComponentView(
    var posX: Double,
    var posY: Double,
    var width: Double,
    var height: Double,
    var visual: Visual
) {
    val id = IDGenerator.generateID()
}