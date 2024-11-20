package data.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import tools.aqua.bgw.event.MouseButtonType

@Serializable
@SerialName("MouseExitedEventData")
class MouseExitedEventData(
    private val pX: Double,
    private val pY: Double
) : MouseEventData(MouseButtonType.UNSPECIFIED, pX, pY)