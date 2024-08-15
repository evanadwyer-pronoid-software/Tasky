package com.pronoidsoftware.core.domain.agendaitem

interface AlarmScheduler {
    fun schedule(agendaItem: AgendaItem)
    fun cancel(agendaItemId: String)
}
