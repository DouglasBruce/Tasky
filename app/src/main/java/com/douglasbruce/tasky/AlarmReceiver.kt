package com.douglasbruce.tasky

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.douglasbruce.tasky.core.domain.notifications.Notifier
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notifier: Notifier

    override fun onReceive(context: Context?, intent: Intent?) {
        val text = intent?.getStringExtra("TEXT") ?: return
        val title = intent.getStringExtra("TITLE") ?: return
        val id = intent.getStringExtra("ID") ?: return
        val type = intent.getStringExtra("TYPE") ?: return
        val date = intent.getLongExtra("DATE", 0L)

        if (date == 0L) return

        notifier.postAgendaNotifications(
            id = id,
            title = title,
            text = text,
            date = date,
            type = type,
        )
    }
}