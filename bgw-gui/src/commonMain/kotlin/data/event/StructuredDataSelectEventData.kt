package data.event

import kotlinx.serialization.Serializable

@Serializable
internal class StructuredDataSelectEventData(var index: Int) : EventData()