package com.douglasbruce.tasky.core.data.repository

import com.douglasbruce.tasky.core.common.auth.AuthResult
import com.douglasbruce.tasky.core.common.utils.UiText
import com.douglasbruce.tasky.core.data.database.dao.EventDao
import com.douglasbruce.tasky.core.data.database.dao.ReminderDao
import com.douglasbruce.tasky.core.data.database.dao.TaskDao
import com.douglasbruce.tasky.core.data.database.model.ModifiedAgendaItemEntity
import com.douglasbruce.tasky.core.domain.datastore.UserDataPreferences
import com.douglasbruce.tasky.core.domain.mapper.toAgendaItems
import com.douglasbruce.tasky.core.domain.mapper.toCreateEventRequest
import com.douglasbruce.tasky.core.domain.mapper.toCreateReminderRequest
import com.douglasbruce.tasky.core.domain.mapper.toCreateTaskRequest
import com.douglasbruce.tasky.core.domain.mapper.toEvent
import com.douglasbruce.tasky.core.domain.mapper.toEventEntity
import com.douglasbruce.tasky.core.domain.mapper.toReminder
import com.douglasbruce.tasky.core.domain.mapper.toReminderEntity
import com.douglasbruce.tasky.core.domain.mapper.toTask
import com.douglasbruce.tasky.core.domain.mapper.toTaskEntity
import com.douglasbruce.tasky.core.domain.mapper.toUpdateEventRequest
import com.douglasbruce.tasky.core.domain.mapper.toUpdateReminderRequest
import com.douglasbruce.tasky.core.domain.mapper.toUpdateTaskRequest
import com.douglasbruce.tasky.core.domain.repository.AgendaRepository
import com.douglasbruce.tasky.core.domain.utils.JsonSerializer
import com.douglasbruce.tasky.core.model.AgendaItem
import com.douglasbruce.tasky.core.model.ModificationType
import com.douglasbruce.tasky.core.network.model.request.CreateEventRequest
import com.douglasbruce.tasky.core.network.model.request.SyncAgendaRequest
import com.douglasbruce.tasky.core.network.model.request.UpdateEventRequest
import com.douglasbruce.tasky.core.network.retrofit.RetrofitTaskyNetwork
import com.douglasbruce.tasky.core.network.retrofit.authenticatedRetrofitCall
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import okhttp3.MultipartBody
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject

class AgendaRepositoryImpl @Inject constructor(
    userDataPreferences: UserDataPreferences,
    private val taskyNetwork: RetrofitTaskyNetwork,
    private val eventsDao: EventDao,
    private val tasksDao: TaskDao,
    private val remindersDao: ReminderDao,
    private val serializer: JsonSerializer,
) : AgendaRepository {

    private val localUserId: Flow<String> = userDataPreferences.userData.map { it.userId }

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

        val fetchedEvents: List<AgendaItem.Event> =
            agendaItems.filterIsInstance<AgendaItem.Event>()
        val fetchedTasks: List<AgendaItem.Task> =
            agendaItems.filterIsInstance<AgendaItem.Task>()
        val fetchedReminders: List<AgendaItem.Reminder> =
            agendaItems.filterIsInstance<AgendaItem.Reminder>()

        saveAgendaItemsLocally(
            events = fetchedEvents,
            tasks = fetchedTasks,
            reminders = fetchedReminders,
        )
    }

    override suspend fun syncLocalDatabase(
        time: ZonedDateTime,
        updateSelectedDateOnly: Boolean,
    ): AuthResult<List<AgendaItem>> {
        return when (val result = syncModifiedAgendaItems(time, updateSelectedDateOnly)) {
            is AuthResult.Success -> {
                val fetchedEvents: List<AgendaItem.Event> =
                    result.data?.filterIsInstance<AgendaItem.Event>() ?: emptyList()
                val fetchedTasks: List<AgendaItem.Task> =
                    result.data?.filterIsInstance<AgendaItem.Task>() ?: emptyList()
                val fetchedReminders: List<AgendaItem.Reminder> =
                    result.data?.filterIsInstance<AgendaItem.Reminder>() ?: emptyList()

                saveAgendaItemsLocally(
                    events = fetchedEvents,
                    tasks = fetchedTasks,
                    reminders = fetchedReminders
                )

                AuthResult.Success(getOneOffAgendaItems(time))
            }

            is AuthResult.Unauthorized -> {
                AuthResult.Unauthorized()
            }

            is AuthResult.Error -> {
                AuthResult.Error(result.message ?: UiText.UnknownError)
            }
        }
    }

    private suspend fun syncModifiedAgendaItems(
        time: ZonedDateTime,
        updateSelectedDateOnly: Boolean,
    ) = supervisorScope {

        launch {
            syncCreatedEvents(eventsDao.getModifiedEventsWithModType(ModificationType.Created))
        }

        launch {
            syncCreatedTasks(tasksDao.getModifiedTasksWithModType(ModificationType.Created))
        }

        launch {
            syncCreatedReminders(remindersDao.getModifiedRemindersWithModType(ModificationType.Created))
        }

        launch {
            syncUpdatedEvents(eventsDao.getModifiedEventsWithModType(ModificationType.Updated))
        }

        launch {
            syncUpdatedTasks(tasksDao.getModifiedTasksWithModType(ModificationType.Updated))
        }

        launch {
            syncUpdatedReminders(remindersDao.getModifiedRemindersWithModType(ModificationType.Updated))
        }

        val deletedEvents = eventsDao.getModifiedEventsWithModType(ModificationType.Deleted)
        val deletedTasks = tasksDao.getModifiedTasksWithModType(ModificationType.Deleted)
        val deletedReminders =
            remindersDao.getModifiedRemindersWithModType(ModificationType.Deleted)

        val result = authenticatedRetrofitCall(serializer) {
            taskyNetwork.syncAgenda(
                request = SyncAgendaRequest(
                    deletedEventIds = deletedEvents.map { it.id },
                    deletedTaskIds = deletedTasks.map { it.id },
                    deletedReminderIds = deletedReminders.map { it.id },
                )
            )
            AuthResult.Success(Unit)
        }

        if (result is AuthResult.Success) {
            eventsDao.clearModifiedEventsWithModType(ModificationType.Deleted)
            tasksDao.clearModifiedTasksWithModType(ModificationType.Deleted)
            remindersDao.clearModifiedRemindersWithModType(ModificationType.Deleted)
        }

        authenticatedRetrofitCall(serializer) {
            val agendaItems = if (updateSelectedDateOnly) {
                taskyNetwork.getAgenda(
                    timeZone = time.zone.toString(),
                    time = time.toInstant().toEpochMilli()
                ).toAgendaItems()
            } else {
                taskyNetwork.getFullAgenda().toAgendaItems()
            }
            AuthResult.Success(agendaItems)
        }
    }

    private suspend fun getOneOffAgendaItems(time: ZonedDateTime): List<AgendaItem> {
        val utcStartOfDate =
            time.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val utcEndOfDate =
            time.toLocalDate().atStartOfDay(ZoneId.systemDefault()).plusDays(1).toInstant()
                .toEpochMilli()

        return supervisorScope {
            val eventsDeferred = async {
                eventsDao.getOneOffEventsForDate(
                    startOfDate = utcStartOfDate,
                    endOfDate = utcEndOfDate,
                ).map { it.toEvent() }
            }
            val tasksDeferred = async {
                tasksDao.getOneOffTasksForDate(
                    startOfDate = utcStartOfDate,
                    endOfDate = utcEndOfDate,
                ).map { it.toTask() }
            }
            val remindersDeferred = async {
                remindersDao.getOneOffRemindersForDate(
                    startOfDate = utcStartOfDate,
                    endOfDate = utcEndOfDate,
                ).map { it.toReminder() }
            }
            eventsDeferred.await() + tasksDeferred.await() + remindersDeferred.await()
        }
    }

    private suspend fun saveAgendaItemsLocally(
        events: List<AgendaItem.Event>,
        tasks: List<AgendaItem.Task>,
        reminders: List<AgendaItem.Reminder>,
    ) = supervisorScope {
        val eventsJob = launch {
            eventsDao.upsertAllEvents(events.map { it.toEventEntity() })
        }
        val tasksJob = launch {
            tasksDao.upsertAllTasks(tasks.map { it.toTaskEntity() })
        }
        val remindersJob = launch {
            remindersDao.upsertAllReminders(reminders.map { it.toReminderEntity() })
        }
        eventsJob.join()
        tasksJob.join()
        remindersJob.join()
    }

    private suspend fun syncCreatedTasks(createdTasks: List<ModifiedAgendaItemEntity>) =
        supervisorScope {
            createdTasks.map { modifiedTask ->
                launch {
                    tasksDao.getTaskById(modifiedTask.id)?.toTask()?.toCreateTaskRequest()
                        ?.let { request ->
                            val result = authenticatedRetrofitCall(serializer) {
                                taskyNetwork.createTask(createTaskRequest = request)
                                AuthResult.Success(Unit)
                            }

                            if (result is AuthResult.Success) {
                                tasksDao.deleteModifiedTaskWithModType(
                                    id = request.id,
                                    modificationType = ModificationType.Created,
                                )
                            }
                        }
                }
            }.forEach { it.join() }
        }

    private suspend fun syncUpdatedTasks(updatedTasks: List<ModifiedAgendaItemEntity>) =
        supervisorScope {
            updatedTasks.map { modifiedTask ->
                launch {
                    tasksDao.getTaskById(modifiedTask.id)?.toTask()?.toUpdateTaskRequest()
                        ?.let { request ->
                            val result = authenticatedRetrofitCall(serializer) {
                                taskyNetwork.updateTask(updateTaskRequest = request)
                                AuthResult.Success(Unit)
                            }

                            if (result is AuthResult.Success) {
                                tasksDao.deleteModifiedTaskWithModType(
                                    id = request.id,
                                    modificationType = ModificationType.Updated,
                                )
                            }
                        }
                }
            }.forEach { it.join() }
        }

    private suspend fun syncCreatedReminders(createdReminders: List<ModifiedAgendaItemEntity>) =
        supervisorScope {
            createdReminders.map { modifiedReminder ->
                launch {
                    remindersDao.getReminderById(modifiedReminder.id)?.toReminder()
                        ?.toCreateReminderRequest()?.let { request ->
                            val result = authenticatedRetrofitCall(serializer) {
                                taskyNetwork.createReminder(createReminderRequest = request)
                                AuthResult.Success(Unit)
                            }

                            if (result is AuthResult.Success) {
                                remindersDao.deleteModifiedReminderWithModType(
                                    id = request.id,
                                    modificationType = ModificationType.Created,
                                )
                            }
                        }
                }
            }.forEach { it.join() }
        }

    private suspend fun syncUpdatedReminders(updatedReminders: List<ModifiedAgendaItemEntity>) =
        supervisorScope {
            updatedReminders.map { modifiedReminder ->
                launch {
                    remindersDao.getReminderById(modifiedReminder.id)?.toReminder()
                        ?.toUpdateReminderRequest()?.let { request ->
                            val result = authenticatedRetrofitCall(serializer) {
                                taskyNetwork.updateReminder(updateReminderRequest = request)
                                AuthResult.Success(Unit)
                            }

                            if (result is AuthResult.Success) {
                                remindersDao.deleteModifiedReminderWithModType(
                                    id = request.id,
                                    modificationType = ModificationType.Updated,
                                )
                            }
                        }
                }
            }.forEach { it.join() }
        }

    private suspend fun syncCreatedEvents(createdEvents: List<ModifiedAgendaItemEntity>) =
        supervisorScope {
            createdEvents.map { modifiedEvent ->
                launch {
                    eventsDao.getEventById(modifiedEvent.id)?.toEvent()?.toCreateEventRequest()
                        ?.let { request ->
                            val result = authenticatedRetrofitCall(serializer) {
                                taskyNetwork.createEvent(
                                    createEventRequest = MultipartBody.Part.createFormData(
                                        "create_event_request",
                                        serializer.toJson(request, CreateEventRequest::class.java)!!
                                    ),
                                    photos = emptyList()
                                )
                                AuthResult.Success(Unit)
                            }

                            if (result is AuthResult.Success) {
                                eventsDao.deleteModifiedEventWithModType(
                                    id = request.id,
                                    modificationType = ModificationType.Created
                                )
                            }
                        }
                }
            }.forEach { it.join() }
        }

    private suspend fun syncUpdatedEvents(updatedEvents: List<ModifiedAgendaItemEntity>) =
        supervisorScope {
            updatedEvents.map { modifiedEvent ->
                launch {
                    val userId = localUserId.first()
                    val localEvent = eventsDao.getEventById(modifiedEvent.id)?.toEvent()

                    localEvent?.toUpdateEventRequest(
                        deletedPhotoKeys = emptyList(),
                        isGoing = localEvent.getAttendee(userId = userId)?.isGoing ?: false
                    )?.let { request ->
                        val result = authenticatedRetrofitCall(serializer) {
                            taskyNetwork.updateEvent(
                                updateEventRequest = MultipartBody.Part.createFormData(
                                    "update_event_request",
                                    serializer.toJson(request, UpdateEventRequest::class.java)!!
                                ),
                                photos = emptyList()
                            )
                            AuthResult.Success(Unit)
                        }

                        if (result is AuthResult.Success) {
                            eventsDao.deleteModifiedEventWithModType(
                                id = request.id,
                                modificationType = ModificationType.Updated
                            )
                        }
                    }
                }
            }
        }
}