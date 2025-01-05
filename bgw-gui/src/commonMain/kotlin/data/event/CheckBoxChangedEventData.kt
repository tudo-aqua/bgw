package data.event

import kotlinx.serialization.Serializable

@Serializable
internal class CheckBoxChangedEventData(var value: Boolean) : EventData()