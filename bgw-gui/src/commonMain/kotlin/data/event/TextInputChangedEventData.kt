package data.event

import kotlinx.serialization.Serializable

@Serializable
internal class TextInputChangedEventData(var value: String) : EventData()