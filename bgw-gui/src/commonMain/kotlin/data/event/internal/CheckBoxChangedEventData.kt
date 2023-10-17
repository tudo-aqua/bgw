package data.event.internal

import data.event.EventData
import kotlinx.serialization.Serializable

@Serializable
class CheckBoxChangedEventData (var value: Boolean) : EventData()