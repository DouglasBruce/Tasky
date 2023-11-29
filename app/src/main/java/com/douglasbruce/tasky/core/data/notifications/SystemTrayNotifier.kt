package com.douglasbruce.tasky.core.data.notifications

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import com.douglasbruce.tasky.MainActivity
import com.douglasbruce.tasky.R
import com.douglasbruce.tasky.core.domain.notifications.Notifier
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

private const val NOTIFICATION_GROUP = "AGENDA_NOTIFICATIONS"
private const val DEEP_LINK_SCHEME_AND_HOST = "tasky:/"

/**
 * Implementation of [Notifier] that displays notifications in the system tray.
 */
@Singleton
class SystemTrayNotifier @Inject constructor(
    @ApplicationContext private val context: Context,
) : Notifier {

    override fun postAgendaNotifications(
        id: String,
        title: String,
        text: String,
        date: Long,
        type: String,
    ) = with(context) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS,
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val path = when (type) {
            "Task" -> "$DEEP_LINK_SCHEME_AND_HOST/task/$date/false?taskId=$id"
            "Reminder" -> "$DEEP_LINK_SCHEME_AND_HOST/reminder/$date/false?reminderId=$id"
            else -> "$DEEP_LINK_SCHEME_AND_HOST/event/$date/false?eventId=$id"
        }

        val alarmNotification = createAgendaNotification {
            setContentTitle(title)
                .setContentText(text)
                .setContentIntent(agendaPendingIntent(path, id))
                .setSmallIcon(
                    R.drawable.ic_launcher_foreground
                )
                .setGroup(NOTIFICATION_GROUP)
                .setAutoCancel(true)
                .build()
        }

        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(id.hashCode(), alarmNotification)
    }

    private fun Context.createAgendaNotification(
        block: NotificationCompat.Builder.() -> Unit,
    ): Notification {
        ensureNotificationChannelExists()
        return NotificationCompat.Builder(
            this,
            getString(R.string.notification_channel_id),
        )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .apply(block)
            .build()
    }

    private fun Context.ensureNotificationChannelExists() {
        val channel = NotificationChannel(
            getString(R.string.notification_channel_id),
            getString(R.string.notification_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT,
        ).apply {
            description = getString(R.string.notification_channel_description)
        }

        NotificationManagerCompat.from(this).createNotificationChannel(channel)
    }

    private fun Context.agendaPendingIntent(
        path: String,
        id: String,
    ): PendingIntent? = PendingIntent.getActivity(
        this,
        id.hashCode(),
        Intent(
            Intent.ACTION_VIEW,
            path.toUri(),
            context,
            MainActivity::class.java
        ),
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
    )
}
