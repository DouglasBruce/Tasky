package com.douglasbruce.tasky.core.data.repository

import com.douglasbruce.tasky.R
import com.douglasbruce.tasky.core.common.auth.AuthResult
import com.douglasbruce.tasky.core.common.utils.UiText
import com.douglasbruce.tasky.core.data.database.dao.TaskDao
import com.douglasbruce.tasky.core.data.database.model.ModifiedAgendaItemEntity
import com.douglasbruce.tasky.core.domain.mapper.toAlarmItem
import com.douglasbruce.tasky.core.domain.mapper.toCreateTaskRequest
import com.douglasbruce.tasky.core.domain.mapper.toTask
import com.douglasbruce.tasky.core.domain.mapper.toTaskEntity
import com.douglasbruce.tasky.core.domain.mapper.toUpdateTaskRequest
import com.douglasbruce.tasky.core.domain.repository.TaskRepository
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

class TaskRepositoryImpl @Inject constructor(
    private val taskyNetwork: RetrofitTaskyNetwork,
    private val dao: TaskDao,
    private val serializer: JsonSerializer,
    private val alarmScheduler: AlarmScheduler,
) : TaskRepository {

    override suspend fun getTaskById(taskId: String): AuthResult<AgendaItem.Task> {
        return authenticatedRetrofitCall(serializer) {
            dao.getTaskById(taskId)?.toTask()?.let {
                AuthResult.Success(data = it)
            } ?: AuthResult.Error(UiText.StringResource(R.string.task_not_found))
        }
    }

    override suspend fun createTask(task: AgendaItem.Task): AuthResult<Unit> {
        dao.upsertTask(task.toTaskEntity())
        alarmScheduler.schedule(task.toAlarmItem())

        val result = authenticatedRetrofitCall(serializer) {
            taskyNetwork.createTask(task.toCreateTaskRequest())
            AuthResult.Success(Unit)
        }

        return if (result is AuthResult.Error) {
            withContext(NonCancellable) {
                dao.upsertModifiedTask(
                    ModifiedAgendaItemEntity(
                        id = task.id,
                        agendaItemType = AgendaItemType.Task,
                        modificationType = ModificationType.Created,
                    )
                )
            }
            result
        } else result
    }

    override suspend fun updateTask(task: AgendaItem.Task): AuthResult<Unit> {
        dao.upsertTask(task.toTaskEntity())
        alarmScheduler.schedule(task.toAlarmItem())

        val result = authenticatedRetrofitCall(serializer) {
            taskyNetwork.updateTask(task.toUpdateTaskRequest())
            AuthResult.Success(Unit)
        }

        return if (result is AuthResult.Error) {
            withContext(NonCancellable) {
                dao.upsertModifiedTask(
                    ModifiedAgendaItemEntity(
                        id = task.id,
                        agendaItemType = AgendaItemType.Task,
                        modificationType = ModificationType.Updated,
                    )
                )
            }
            result
        } else result
    }

    override suspend fun deleteTaskById(taskId: String): AuthResult<Unit> {
        val result = authenticatedRetrofitCall(serializer) {
            dao.deleteTaskById(taskId)
            alarmScheduler.cancel(taskId)
            taskyNetwork.deleteTask(taskId)
            AuthResult.Success(Unit)
        }
        return if (result is AuthResult.Error) {
            withContext(NonCancellable) {
                dao.upsertModifiedTask(
                    ModifiedAgendaItemEntity(
                        id = taskId,
                        agendaItemType = AgendaItemType.Task,
                        modificationType = ModificationType.Deleted
                    )
                )
            }
            result
        } else result
    }

    override suspend fun getFutureTasks(): List<AgendaItem.Task> {
        return dao.getFutureTasks().map { it.toTask() }
    }
}