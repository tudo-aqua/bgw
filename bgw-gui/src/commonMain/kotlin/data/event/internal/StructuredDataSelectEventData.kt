package data.event.internal

import data.event.EventData
import kotlinx.serialization.Serializable

@Serializable
class StructuredDataSelectEventData (var index: Int) : EventData()