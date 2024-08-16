package com.pronoidsoftware.core.presentation.ui.alarm

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.pronoidsoftware.core.presentation.designsystem.R
import com.pronoidsoftware.core.presentation.ui.hasNotificationPermission
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (context.hasNotificationPermission()) {
            val agendaItemType =
                intent.getStringExtra(AlarmIntentKeys.EXTRA_AGENDA_ITEM_TYPE) ?: return
            val agendaItemId =
                intent.getStringExtra(AlarmIntentKeys.EXTRA_AGENDA_ITEM_ID) ?: return
            val agendaItemTitle =
                intent.getStringExtra(AlarmIntentKeys.EXTRA_AGENDA_ITEM_TITLE) ?: return
            val agendaItemDescription =
                intent.getStringExtra(AlarmIntentKeys.EXTRA_AGENDA_ITEM_DESCRIPTION) ?: return

            val deeplinkIntent = Intent(
                Intent.ACTION_VIEW,
                "details://item/$agendaItemType/false/$agendaItemId".toUri(),
                // same order as DetailScreen from NavigationRoot
            )
            val pendingIntent = TaskStackBuilder.create(context).run {
                addNextIntentWithParentStack(deeplinkIntent)
                getPendingIntent(
                    agendaItemId.hashCode(),
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
                )
            }

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notification =
                NotificationCompat.Builder(context, NotificationConstants.NOTIFICATION_CHANNEL_ID)
                    .setContentTitle(agendaItemTitle)
                    .setContentText(agendaItemDescription)
                    .setSmallIcon(R.drawable.tasky_logo)
                    .setStyle(NotificationCompat.BigTextStyle())
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build()

            notificationManager.notify(
                agendaItemId.hashCode(),
                notification,
            )
        }
    }
}
