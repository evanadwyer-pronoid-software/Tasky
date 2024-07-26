package com.pronoidsoftware.agenda.presentation.overview.model

import kotlinx.serialization.Serializable

// @Serializable(with = AgendaItemSerializer::class)
// enum class AgendaItem(val description: String) {
//    EVENT("EVENT"),
//    TASK("TASK"),
//    REMINDER("REMINDER"),
// }
//
// class AgendaItemSerializer : KSerializer<AgendaItem> {
//    override val descriptor: SerialDescriptor =
//        PrimitiveSerialDescriptor("AgendaItem", PrimitiveKind.STRING)
//
//    override fun deserialize(decoder: Decoder): AgendaItem {
//        val desc = decoder.decodeString()
//        return AgendaItem.entries.first { desc == it.description }
//    }
//
//    override fun serialize(encoder: Encoder, value: AgendaItem) {
//        encoder.encodeString(value.description)
//    }
// }

@Serializable
sealed class AgendaItemUi(
    open val id: String,
) {
    data class TimeMarker(
        override val id: String = "TimeMarker",
    ) : AgendaItemUi(id = id)

    data class Item(
        val item: AgendaOverviewItemUi,
        override val id: String,
    ) : AgendaItemUi(id = id)
}

@Serializable
sealed class AgendaOverviewItemUi(
    open val id: String,
    open val title: String,
    open val description: String,
    open val fromTime: String,
) {
    data class ReminderOverviewUi(
        override val id: String,
        override val title: String,
        override val description: String,
        override val fromTime: String,
    ) : AgendaOverviewItemUi(
        id = id,
        title = title,
        description = description,
        fromTime = fromTime,
    )

    data class TaskOverviewUi(
        override val id: String,
        override val title: String,
        override val description: String,
        override val fromTime: String,
        val completed: Boolean,
    ) : AgendaOverviewItemUi(
        id = id,
        title = title,
        description = description,
        fromTime = fromTime,
    )

    data class EventOverviewUi(
        override val id: String,
        override val title: String,
        override val description: String,
        override val fromTime: String,
        val toTime: String,
    ) : AgendaOverviewItemUi(
        id = id,
        title = title,
        description = description,
        fromTime = fromTime,
    )
}
