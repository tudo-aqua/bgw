package data.event

import kotlinx.serialization.Serializable

@Serializable
internal class ColorInputChangedEventData(var value: String) : EventData()