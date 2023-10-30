package com.douglasbruce.tasky.core.network.retrofit

import com.douglasbruce.tasky.core.network.model.NetworkAgenda
import com.douglasbruce.tasky.core.network.model.NetworkAttendeeCheck
import com.douglasbruce.tasky.core.network.model.NetworkEvent
import com.douglasbruce.tasky.core.network.model.NetworkReminder
import com.douglasbruce.tasky.core.network.model.NetworkTask
import com.douglasbruce.tasky.core.network.model.NetworkUser
import com.douglasbruce.tasky.core.network.model.request.CreateReminderRequest
import com.douglasbruce.tasky.core.network.model.request.CreateTaskRequest
import com.douglasbruce.tasky.core.network.model.request.LoginRequest
import com.douglasbruce.tasky.core.network.model.request.RegisterRequest
import com.douglasbruce.tasky.core.network.model.request.SyncAgendaRequest
import com.douglasbruce.tasky.core.network.model.request.UpdateReminderRequest
import com.douglasbruce.tasky.core.network.model.request.UpdateTaskRequest
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

interface RetrofitTaskyNetworkApi {
    @POST("/login")
    suspend fun login(@Body request: LoginRequest): NetworkUser

    @POST("/register")
    suspend fun register(@Body request: RegisterRequest)

    @GET("/authenticate")
    suspend fun authenticate()

    @GET("/logout")
    suspend fun logout()

    @GET("/agenda")
    suspend fun getAgenda(
        @Query("timezone") timeZone: String,
        @Query("time") time: Long
    ): NetworkAgenda

    @POST("/syncAgenda")
    suspend fun syncAgenda(@Body request: SyncAgendaRequest)

    @GET("/fullAgenda")
    suspend fun getFullAgenda(): NetworkAgenda

    @GET("/event")
    suspend fun getEvent(@Query("eventId") eventId: String): NetworkEvent

    @Multipart
    @POST("/event")
    suspend fun createEvent(
        @Part createEventRequest: MultipartBody.Part,
        @Part photos: List<MultipartBody.Part>,
    ): NetworkEvent

    @Multipart
    @PUT("/event")
    suspend fun updateEvent(
        @Part updateEventRequest: MultipartBody.Part,
        @Part photos: List<MultipartBody.Part>,
    ): NetworkEvent

    @DELETE("/event")
    suspend fun deleteEvent(@Query("eventId") eventId: String)

    @GET("/task")
    suspend fun getTask(@Query("taskId") taskId: String): NetworkTask

    @POST("/task")
    suspend fun createTask(@Body createTaskRequest: CreateTaskRequest)

    @PUT("/task")
    suspend fun updateTask(@Body updateTaskRequest: UpdateTaskRequest)

    @DELETE("/task")
    suspend fun deleteTask(@Query("taskId") taskId: String)

    @GET("/reminder")
    suspend fun getReminder(@Query("reminderId") reminderId: String): NetworkReminder

    @POST("/reminder")
    suspend fun createReminder(@Body createReminderRequest: CreateReminderRequest)

    @PUT("/reminder")
    suspend fun updateReminder(@Body updateReminderRequest: UpdateReminderRequest)

    @DELETE("/reminder")
    suspend fun deleteReminder(@Query("reminderId") reminderId: String)

    @GET("/attendee")
    suspend fun getAttendee(@Query("email") email: String): NetworkAttendeeCheck

    @DELETE("/attendee")
    suspend fun leaveEvent(@Query("eventId") eventId: String)
}