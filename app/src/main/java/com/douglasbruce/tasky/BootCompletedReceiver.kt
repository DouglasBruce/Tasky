package com.douglasbruce.tasky

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.douglasbruce.tasky.core.domain.utils.AlarmScheduler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BootCompletedReceiver : BroadcastReceiver() {

    @Inject
    lateinit var alarmScheduler: AlarmScheduler

    @OptIn(DelicateCoroutinesApi::class)
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            GlobalScope.launch(Dispatchers.IO) {
                alarmScheduler.scheduleAllFutureAlarms()
            }
        }
    }
}