package com.douglasbruce.tasky.core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.douglasbruce.tasky.core.data.database.model.ModifiedAgendaItemEntity
import com.douglasbruce.tasky.core.data.database.model.TaskEntity
import kotlinx.coroutines.flow.Flow
import java.time.ZonedDateTime

@Dao
interface TaskDao {
    @Upsert
    suspend fun upsertTask(task: TaskEntity)

    @Upsert
    suspend fun upsertAllTasks(tasks: List<TaskEntity>)

    @Query(
        value = """
            SELECT * FROM tasks
            WHERE id = :taskId
        """,
    )
    suspend fun getTaskById(taskId: String): TaskEntity?

    @Query(
        value = """
            SELECT * FROM tasks
            WHERE time >= :startOfDate
            AND time < :endOfDate
        """,
    )
    fun getTasksForDate(startOfDate: Long, endOfDate: Long): Flow<List<TaskEntity>>

    @Query(
        value = """
            SELECT * FROM tasks
            WHERE time >= :startOfDate
            AND time < :endOfDate
        """,
    )
    fun getOneOffTasksForDate(startOfDate: Long, endOfDate: Long): List<TaskEntity>

    @Query(
        value = """
            SELECT * FROM tasks
            WHERE time >= :startingDate
        """
    )
    suspend fun getFutureTasks(
        startingDate: Long = ZonedDateTime.now().toInstant().toEpochMilli(),
    ): List<TaskEntity>

    @Query(
        value = """
            DELETE FROM tasks
            WHERE id = :taskId
        """,
    )
    suspend fun deleteTaskById(taskId: String)

    @Upsert
    suspend fun upsertModifiedTask(modifiedTask: ModifiedAgendaItemEntity)

    @Query(
        value = """
            SELECT * FROM modified_agenda
            WHERE agendaItemType = 'Task'
        """,
    )
    suspend fun getModifiedTasks(): List<ModifiedAgendaItemEntity>
}