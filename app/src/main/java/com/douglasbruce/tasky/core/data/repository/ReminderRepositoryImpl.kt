package com.douglasbruce.tasky.core.data.repository

import com.douglasbruce.tasky.R
import com.douglasbruce.tasky.core.common.auth.AuthResult
import com.douglasbruce.tasky.core.common.utils.UiText
import com.douglasbruce.tasky.core.data.database.dao.ReminderDao
import com.douglasbruce.tasky.core.data.database.model.ModifiedAgendaItemEntity
import com.douglasbruce.tasky.core.domain.mapper.toAlarmItem
import com.douglasbruce.tasky.core.domain.mapper.toCreateReminderRequest
import com.douglasbruce.tasky.core.domain.mapper.toReminder
import com.douglasbruce.tasky.core.domain.mapper.toReminderEntity
import com.douglasbruce.tasky.core.domain.mapper.toUpdateReminderRequest
import com.douglasbruce.tasky.core.domain.repository.ReminderRepository
import com.douglasbruce.tasky.core.domain.utils.AlarmScheduler
import com.douglasbruce.tasky.core.domain.utils.JsonSerializer
import com.douglasbruce.tasky.core.model.AgendaItem
import com.douglasbruce.tasky.core.model.AgendaItemType
import com.douglasbruce.tasky.core.model.ModificationType
import com.douglasbruce.tasky.core.network.retrofit.RetrofitTaskyNetwork
import com.douglasbruce.tasky.core.network.retrofit.authenticatedRetrofitCall
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReminderRepositoryImpl @Inject constructor(
    private val taskyNetwork: RetrofitTaskyNetwork,
    private val dao: ReminderDao,
    private val serializer: JsonSerializer,
    private val alarmScheduler: AlarmScheduler,
) : ReminderRepository {
    override suspend fun getReminderById(reminderId: String): AuthResult<AgendaItem.Reminder> {
        return authenticatedRetrofitCall(serializer) {
            dao.getReminderById(reminderId)?.toReminder()?.let {
                AuthResult.Success(data = it)
            } ?: AuthResult.Error(UiText.StringResource(R.string.reminder_not_found))
        }
    }

    override suspend fun getFutureReminders(): List<AgendaItem.Reminder> {
        return dao.getFutureReminders().map { it.toReminder() }
    }

    override suspend fun createReminder(reminder: AgendaItem.Reminder): AuthResult<Unit> {
        dao.upsertReminder(reminder.toReminderEntity())
        alarmScheduler.schedule(reminder.toAlarmItem())

        val result =  authenticatedRetrofitCall(serializer) {
            taskyNetwork.createReminder(reminder.toCreateReminderRequest())
            AuthResult.Success(Unit)
        }

        return if (result is AuthResult.Error) {
            withContext(NonCancellable) {
                dao.insertModifiedReminder(
                    ModifiedAgendaItemEntity(
                        id = reminder.id,
                        agendaItemType = AgendaItemType.Reminder,
                        modificationType = ModificationType.Created
                    )
                )
            }
            result
        } else result
    }

    override suspend fun updateReminder(reminder: AgendaItem.Reminder): AuthResult<Unit> {
        dao.upsertReminder(reminder.toReminderEntity())
        alarmScheduler.schedule(reminder.toAlarmItem())

        val result = authenticatedRetrofitCall(serializer) {
            taskyNetwork.updateReminder(reminder.toUpdateReminderRequest())
            AuthResult.Success(Unit)
        }

        return if (result is AuthResult.Error) {
            withContext(NonCancellable) {
                dao.insertModifiedReminder(
                    ModifiedAgendaItemEntity(
                        id = reminder.id,
                        agendaItemType = AgendaItemType.Reminder,
                        modificationType = ModificationType.Updated
                    )
                )
            }
            result
        } else result
    }

    override suspend fun deleteReminderById(reminderId: String): AuthResult<Unit> {
        val result = authenticatedRetrofitCall(serializer) {
            dao.deleteReminderById(reminderId)
            alarmScheduler.cancel(reminderId)
            taskyNetwork.deleteReminder(reminderId)
            AuthResult.Success(Unit)
        }
        return if (result is AuthResult.Error) {
            withContext(NonCancellable) {
                dao.insertModifiedReminder(
                    ModifiedAgendaItemEntity(
                        id = reminderId,
                        agendaItemType = AgendaItemType.Reminder,
                        modificationType = ModificationType.Deleted
                    )
                )
            }
            result
        } else result
    }
}