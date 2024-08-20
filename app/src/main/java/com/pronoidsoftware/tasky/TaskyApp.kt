package com.pronoidsoftware.tasky

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.pronoidsoftware.core.domain.notification.NotificationConstants
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber

@HiltAndroidApp
class TaskyApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        val alarmNotificationChannel = NotificationChannel(
            NotificationConstants.ALARM_NOTIFICATION_CHANNEL_ID,
            NotificationConstants.ALARM_NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH,
        )
        val uploadNotificationChannel = NotificationChannel(
            NotificationConstants.UPLOAD_NOTIFICATION_CHANNEL_ID,
            NotificationConstants.UPLOAD_NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW,
        )
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(alarmNotificationChannel)
        notificationManager.createNotificationChannel(uploadNotificationChannel)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
