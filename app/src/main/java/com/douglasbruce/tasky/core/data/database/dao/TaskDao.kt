package com.douglasbruce.tasky.core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.douglasbruce.tasky.core.data.database.model.TaskEntity

@Dao
interface TaskDao {
    @Upsert
    suspend fun upsertTask(task: TaskEntity)

    @Query(
        value = """
            SELECT * FROM tasks
            WHERE id = :taskId
        """,
    )
    fun getTaskById(taskId: String): TaskEntity

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
    fun deleteTaskById(taskId: String)
}