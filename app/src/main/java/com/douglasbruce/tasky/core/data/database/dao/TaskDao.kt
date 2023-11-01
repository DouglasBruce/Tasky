package com.douglasbruce.tasky.core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.douglasbruce.tasky.core.data.database.model.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Upsert
    suspend fun upsertTask(task: TaskEntity)

    @Transaction
    suspend fun upsertAllTasks(tasks: List<TaskEntity>) {
        tasks.forEach {
            upsertTask(it)
        }
    }

    @Query(
        value = """
            SELECT * FROM tasks
            WHERE id = :taskId
        """,
    )
    suspend fun getTaskById(taskId: String): TaskEntity

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
            WHERE time >= :startingDate
        """
    )
    suspend fun getFutureTasks(startingDate: Long): List<TaskEntity>

    @Query(
        value = """
            DELETE FROM tasks
            WHERE id = :taskId
        """,
    )
    suspend fun deleteTaskById(taskId: String)
}