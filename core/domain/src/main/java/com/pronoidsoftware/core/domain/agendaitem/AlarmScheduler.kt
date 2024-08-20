package com.pronoidsoftware.core.domain.agendaitem

interface AlarmScheduler {
    fun schedule(agendaItem: AgendaItem)
    fun scheduleAll(agendaItems: List<AgendaItem>)
    fun cancel(agendaItemId: String)
}
