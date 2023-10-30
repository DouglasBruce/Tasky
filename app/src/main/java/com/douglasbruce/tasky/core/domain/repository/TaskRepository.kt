package com.douglasbruce.tasky.core.domain.repository

import com.douglasbruce.tasky.core.common.auth.AuthResult
import com.douglasbruce.tasky.core.model.AgendaItem

interface TaskRepository {
    suspend fun getTaskById(taskId: String): AuthResult<AgendaItem.Task>
    suspend fun getFutureTasks(): List<AgendaItem.Task>
    suspend fun createTask(task: AgendaItem.Task): AuthResult<Unit>
    suspend fun updateTask(task: AgendaItem.Task): AuthResult<Unit>
    suspend fun deleteTaskById(taskId: String): AuthResult<Unit>
}