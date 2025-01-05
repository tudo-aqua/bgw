package data.event

import kotlinx.serialization.Serializable

@Serializable
internal class RadioChangedEventData(var value: Boolean) : EventData()