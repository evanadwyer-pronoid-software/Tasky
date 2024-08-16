package com.pronoidsoftware.core.presentation.ui.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.pronoidsoftware.core.domain.agendaitem.AgendaItem
import com.pronoidsoftware.core.domain.agendaitem.AgendaItemType
import com.pronoidsoftware.core.domain.agendaitem.AlarmScheduler
import com.pronoidsoftware.core.domain.util.now
import com.pronoidsoftware.core.domain.util.toMillis
import com.pronoidsoftware.core.presentation.ui.R
import com.pronoidsoftware.core.presentation.ui.getTypeString
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.datetime.Clock

class AgendaAlarmScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
    private val clock: Clock,
) : AlarmScheduler {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(agendaItem: AgendaItem) {
        if (Build.VERSION.SDK_INT >= 31 && !alarmManager.canScheduleExactAlarms()) {
            return
        }
        if (agendaItem.notificationDateTime < now(clock)) {
            return
        }

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(
                AlarmIntentKeys.EXTRA_AGENDA_ITEM_TYPE,
                when (agendaItem) {
                    is AgendaItem.Event -> AgendaItemType.EVENT.name
                    is AgendaItem.Reminder -> AgendaItemType.REMINDER.name
                    is AgendaItem.Task -> AgendaItemType.TASK.name
                },
            )
            putExtra(AlarmIntentKeys.EXTRA_AGENDA_ITEM_ID, agendaItem.id)
            putExtra(
                AlarmIntentKeys.EXTRA_AGENDA_ITEM_TITLE,
                agendaItem.title.ifEmpty {
                    context.resources.getString(
                        R.string.new_agenda_item,
                        getTypeString(
                            type = when (agendaItem) {
                                is AgendaItem.Event -> AgendaItemType.EVENT
                                is AgendaItem.Reminder -> AgendaItemType.REMINDER
                                is AgendaItem.Task -> AgendaItemType.TASK
                            },
                            context = context,
                        ),
                    )
                },
            )
            putExtra(AlarmIntentKeys.EXTRA_AGENDA_ITEM_DESCRIPTION, agendaItem.description ?: "")
        }
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            agendaItem.notificationDateTime.toMillis(),
            PendingIntent.getBroadcast(
                context,
                agendaItem.id.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            ),
        )
    }

    override fun cancel(agendaItemId: String) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                agendaItemId.hashCode(),
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            ),
        )
    }
}
