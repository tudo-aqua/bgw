package data.event

import kotlinx.serialization.Serializable

@Serializable
internal class SelectionChangedEventData(var selectedItem: Int) : EventData()