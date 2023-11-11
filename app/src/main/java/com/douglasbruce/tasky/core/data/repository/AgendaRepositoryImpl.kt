package com.douglasbruce.tasky.core.data.repository

import com.douglasbruce.tasky.core.data.database.dao.EventDao
import com.douglasbruce.tasky.core.data.database.dao.ReminderDao
import com.douglasbruce.tasky.core.data.database.dao.TaskDao
import com.douglasbruce.tasky.core.data.database.model.EventEntity
import com.douglasbruce.tasky.core.data.database.model.ReminderEntity
import com.douglasbruce.tasky.core.data.database.model.TaskEntity
import com.douglasbruce.tasky.core.domain.mapper.toAgendaItems
import com.douglasbruce.tasky.core.domain.mapper.toEvent
import com.douglasbruce.tasky.core.domain.mapper.toEventEntity
import com.douglasbruce.tasky.core.domain.mapper.toReminder
import com.douglasbruce.tasky.core.domain.mapper.toReminderEntity
import com.douglasbruce.tasky.core.domain.mapper.toTask
import com.douglasbruce.tasky.core.domain.mapper.toTaskEntity
import com.douglasbruce.tasky.core.domain.repository.AgendaRepository
import com.douglasbruce.tasky.core.model.AgendaItem
import com.douglasbruce.tasky.core.network.retrofit.RetrofitTaskyNetwork
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject

class AgendaRepositoryImpl @Inject constructor(
    private val taskyNetwork: RetrofitTaskyNetwork,
    private val eventsDao: EventDao,
    private val tasksDao: TaskDao,
    private val remindersDao: ReminderDao,
) : AgendaRepository {

    override fun getAgendaForDate(date: LocalDate): Flow<List<AgendaItem>> {
        val utcStartOfDate = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val utcEndOfDate = date.atStartOfDay(ZoneId.systemDefault()).plusDays(1).toInstant()
            .toEpochMilli()

        return combine(
            eventsDao.getEventsForDate(utcStartOfDate, utcEndOfDate),
            tasksDao.getTasksForDate(utcStartOfDate, utcEndOfDate),
            remindersDao.getRemindersForDate(utcStartOfDate, utcEndOfDate),
        ) { eventEntities, taskEntities, reminderEntities ->
            val events = eventEntities.map { it.toEvent() }
            val tasks = taskEntities.map { it.toTask() }
            val reminders = reminderEntities.map { it.toReminder() }
            (events + tasks + reminders).sortedBy { it.sortDate }
        }
    }

    override suspend fun fetchAgendaForDate(date: LocalDate) {
        val zoneId = ZoneId.systemDefault()
        val utcDateTime =
            ZonedDateTime.of(date, LocalTime.now(), zoneId).toInstant().toEpochMilli()

        val agendaItems = taskyNetwork.getAgenda(
            timeZone = zoneId.toString(),
            time = utcDateTime
        ).toAgendaItems()

        val fetchedEvents: List<EventEntity> =
            agendaItems.filterIsInstance<AgendaItem.Event>().map { it.toEventEntity() }
        val fetchedTasks: List<TaskEntity> =
            agendaItems.filterIsInstance<AgendaItem.Task>().map { it.toTaskEntity() }
        val fetchedReminders: List<ReminderEntity> =
            agendaItems.filterIsInstance<AgendaItem.Reminder>().map { it.toReminderEntity() }

        eventsDao.upsertAllEvents(fetchedEvents)
        tasksDao.upsertAllTasks(fetchedTasks)
        remindersDao.upsertAllReminders(fetchedReminders)
    }
}