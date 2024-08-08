package data.event.internal

import data.event.EventData
import kotlinx.serialization.Serializable

@Serializable
class RadioChangedEventData (var value: Boolean) : EventData()