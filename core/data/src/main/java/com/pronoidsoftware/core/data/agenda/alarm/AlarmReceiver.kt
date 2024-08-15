package com.pronoidsoftware.core.data.agenda.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.pronoidsoftware.core.domain.agendaitem.AgendaItemType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val agendaItemType =
            AgendaItemType.valueOf(
                intent.getStringExtra(AlarmIntentKeys.EXTRA_AGENDA_ITEM_TYPE) ?: return,
            )
        val agendaItemId = intent.getStringExtra(AlarmIntentKeys.EXTRA_AGENDA_ITEM_ID) ?: return
        println("Alarm triggered: $agendaItemType with ID: $agendaItemId")
    }
}
