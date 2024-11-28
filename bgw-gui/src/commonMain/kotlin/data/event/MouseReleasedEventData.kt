package data.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import tools.aqua.bgw.event.MouseButtonType

@Serializable
@SerialName("MouseReleasedEventData")
internal class MouseReleasedEventData(
    private val pButton: MouseButtonType,
    private val pX: Double,
    private val pY: Double
) : MouseEventData(pButton, pX, pY)
