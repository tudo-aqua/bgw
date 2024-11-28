package data.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import tools.aqua.bgw.event.MouseButtonType

@Serializable
@SerialName("MouseEventData")
internal open class MouseEventData(
    val button: MouseButtonType,
    val posX: Double,
    val posY: Double,
) : InputEventData()