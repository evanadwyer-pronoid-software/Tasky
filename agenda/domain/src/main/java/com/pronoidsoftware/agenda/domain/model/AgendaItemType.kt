package com.pronoidsoftware.agenda.domain.model

enum class AgendaItemType(private val type: String) {

    EVENT("EVENT"),
    TASK("TASK"),
    REMINDER("REMINDER"),
    ;

    override fun toString(): String {
        return type
    }

    companion object {
        fun from(type: String): AgendaItemType {
            return when (type) {
                "EVENT" -> EVENT
                "TASK" -> TASK
                "REMINDER" -> REMINDER
                else -> error("Unknown Agenda Item type")
            }
        }
    }
}
