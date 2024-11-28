package data.event.internal

import data.event.EventData
import kotlinx.serialization.Serializable

@Serializable
internal class ColorInputChangedEventData(var value: String) : EventData()