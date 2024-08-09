package data.event.internal

import data.event.EventData
import kotlinx.serialization.Serializable

@Serializable
class ColorInputChangedEventData(var value: String) : EventData()