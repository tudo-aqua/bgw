package data.event.internal

import data.event.EventData
import kotlinx.serialization.Serializable

@Serializable
class SelectionChangedEventData(var selectedItem: Int) : EventData()