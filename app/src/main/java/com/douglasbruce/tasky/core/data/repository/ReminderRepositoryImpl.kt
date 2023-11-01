package com.douglasbruce.tasky.core.data.repository

import com.douglasbruce.tasky.R
import com.douglasbruce.tasky.core.common.auth.AuthResult
import com.douglasbruce.tasky.core.common.utils.UiText
import com.douglasbruce.tasky.core.data.database.dao.ReminderDao
import com.douglasbruce.tasky.core.domain.mapper.toCreateReminderRequest
import com.douglasbruce.tasky.core.domain.mapper.toReminder
import com.douglasbruce.tasky.core.domain.mapper.toUpdateReminderRequest
import com.douglasbruce.tasky.core.domain.repository.ReminderRepository
import com.douglasbruce.tasky.core.domain.utils.JsonSerializer
import com.douglasbruce.tasky.core.model.AgendaItem
import com.douglasbruce.tasky.core.network.retrofit.RetrofitTaskyNetwork
import com.douglasbruce.tasky.core.network.retrofit.authenticatedRetrofitCall
import javax.inject.Inject

class ReminderRepositoryImpl @Inject constructor(
    private val taskyNetwork: RetrofitTaskyNetwork,
    private val dao: ReminderDao,
    private val serializer: JsonSerializer,
): ReminderRepository {
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
        //TODO: Create local
        return authenticatedRetrofitCall(serializer) {
            taskyNetwork.createReminder(reminder.toCreateReminderRequest())
            AuthResult.Success(Unit)
        }
    }

    override suspend fun updateReminder(reminder: AgendaItem.Reminder): AuthResult<Unit> {
        //TODO: Update local
        return authenticatedRetrofitCall(serializer) {
            taskyNetwork.updateReminder(reminder.toUpdateReminderRequest())
            AuthResult.Success(Unit)
        }
    }

    override suspend fun deleteReminderById(reminderId: String): AuthResult<Unit> {
        val result = authenticatedRetrofitCall(serializer) {
            dao.deleteReminderById(reminderId)
            taskyNetwork.deleteReminder(reminderId)
            AuthResult.Success(Unit)
        }
        return result //TODO: Check if result is successful or error etc.
    }
}