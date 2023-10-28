package com.douglasbruce.tasky.core.network

import com.douglasbruce.tasky.core.network.model.NetworkAgenda
import com.douglasbruce.tasky.core.network.model.NetworkAttendeeCheck
import com.douglasbruce.tasky.core.network.model.NetworkEvent
import com.douglasbruce.tasky.core.network.model.NetworkReminder
import com.douglasbruce.tasky.core.network.model.NetworkTask
import com.douglasbruce.tasky.core.network.model.NetworkUser
import com.douglasbruce.tasky.core.network.model.request.CreateReminderRequest
import com.douglasbruce.tasky.core.network.model.request.CreateTaskRequest
import com.douglasbruce.tasky.core.network.model.request.SyncAgendaRequest
import com.douglasbruce.tasky.core.network.model.request.UpdateReminderRequest
import com.douglasbruce.tasky.core.network.model.request.UpdateTaskRequest
import okhttp3.MultipartBody
import retrofit2.http.Part

interface TaskyNetworkDataSource {
    suspend fun login(email: String, password: String): NetworkUser
    suspend fun register(fullName: String, email: String, password: String)
    suspend fun authenticate()
    suspend fun logout()
    suspend fun getAgenda(timeZone: String, time: Long): NetworkAgenda
    suspend fun syncAgenda(request: SyncAgendaRequest)
    suspend fun getFullAgenda(): NetworkAgenda
    suspend fun getEvent(eventId: String): NetworkEvent
    suspend fun createEvent(
        @Part createEventRequest: MultipartBody.Part,
        @Part photos: List<MultipartBody.Part>,
    ): NetworkEvent
    suspend fun updateEvent(
        @Part updateEventRequest: MultipartBody.Part,
        @Part photos: List<MultipartBody.Part>,
    ): NetworkEvent
    suspend fun deleteEvent(eventId: String)
    suspend fun getTask(taskId: String): NetworkTask
    suspend fun createTask(createTaskRequest: CreateTaskRequest)
    suspend fun updateTask(updateTaskRequest: UpdateTaskRequest)
    suspend fun deleteTask(taskId: String)
    suspend fun getReminder(reminderId: String): NetworkReminder
    suspend fun createReminder(createReminderRequest: CreateReminderRequest)
    suspend fun updateReminder(updateReminderRequest: UpdateReminderRequest)
    suspend fun deleteReminder(reminderId: String)
    suspend fun getAttendee(email: String): NetworkAttendeeCheck
    suspend fun leaveEvent(eventId: String)
}