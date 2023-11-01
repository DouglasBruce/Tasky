package com.douglasbruce.tasky.core.domain.repository

import com.douglasbruce.tasky.core.common.auth.AuthResult
import com.douglasbruce.tasky.core.model.AgendaItem

interface ReminderRepository {
    suspend fun getReminderById(reminderId: String): AuthResult<AgendaItem.Reminder>
    suspend fun getFutureReminders(): List<AgendaItem.Reminder>
    suspend fun createReminder(reminder: AgendaItem.Reminder): AuthResult<Unit>
    suspend fun updateReminder(reminder: AgendaItem.Reminder): AuthResult<Unit>
    suspend fun deleteReminderById(reminderId: String): AuthResult<Unit>
}