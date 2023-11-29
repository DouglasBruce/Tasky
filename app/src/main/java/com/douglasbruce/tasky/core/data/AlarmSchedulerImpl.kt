package com.douglasbruce.tasky.core.data

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.douglasbruce.tasky.AlarmReceiver
import com.douglasbruce.tasky.core.data.database.dao.EventDao
import com.douglasbruce.tasky.core.data.database.dao.ReminderDao
import com.douglasbruce.tasky.core.data.database.dao.TaskDao
import com.douglasbruce.tasky.core.domain.mapper.toAlarmItem
import com.douglasbruce.tasky.core.domain.mapper.toEvent
import com.douglasbruce.tasky.core.domain.mapper.toReminder
import com.douglasbruce.tasky.core.domain.mapper.toTask
import com.douglasbruce.tasky.core.domain.utils.AlarmScheduler
import com.douglasbruce.tasky.core.model.AlarmItem
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

class AlarmSchedulerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val eventsDao: EventDao,
    private val tasksDao: TaskDao,
    private val remindersDao: ReminderDao,
) : AlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(item: AlarmItem) {
        if (shouldNotScheduleAlarmItem(item)) {
            return
        }

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("TEXT", item.text)
            putExtra("TITLE", item.title)
            putExtra("ID", item.id)
            putExtra("DATE", item.date)
            putExtra("TYPE", item.type)
        }
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            item.time.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,
            PendingIntent.getBroadcast(
                context,
                item.id.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
        )
    }

    override suspend fun scheduleAllFutureAlarms() {
        getAllFutureAlarmItems().forEach { item ->
            if (shouldNotScheduleAlarmItem(item)) {
                return@forEach
            }

            val intent = Intent(context, AlarmReceiver::class.java).apply {
                putExtra("TEXT", item.text)
                putExtra("TITLE", item.title)
                putExtra("ID", item.id)
                putExtra("DATE", item.date)
                putExtra("TYPE", item.type)
            }
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                item.time.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,
                PendingIntent.getBroadcast(
                    context,
                    item.id.hashCode(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
                )
            )
        }
    }

    override fun cancel(id: String) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                id.hashCode(),
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
        )
    }

    override fun cancelAll() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            alarmManager.cancelAll()
        } else {
            while (alarmManager.nextAlarmClock != null) {
                alarmManager.cancel(alarmManager.nextAlarmClock.showIntent)
            }
        }
    }

    private fun shouldNotScheduleAlarmItem(item: AlarmItem): Boolean {
        val currentTime = LocalDateTime.now()
        if (currentTime.isAfter(item.time)) {
            return true
        }

        return false
    }

    private suspend fun getAllFutureAlarmItems(): List<AlarmItem> {
        return supervisorScope {
            val eventsDeferred = async {
                eventsDao.getFutureEvents().map { it.toEvent().toAlarmItem() }
            }
            val tasksDeferred = async {
                tasksDao.getFutureTasks().map { it.toTask().toAlarmItem() }
            }
            val remindersDeferred = async {
                remindersDao.getFutureReminders().map { it.toReminder().toAlarmItem() }
            }
            eventsDeferred.await() + tasksDeferred.await() + remindersDeferred.await()
        }
    }
}