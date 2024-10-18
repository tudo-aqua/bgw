package data.event.internal

import data.event.EventData
import kotlinx.serialization.Serializable

@Serializable
class TextInputChangedEventData(var value: String) : EventData()