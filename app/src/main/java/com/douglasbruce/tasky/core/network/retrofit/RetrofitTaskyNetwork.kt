package com.douglasbruce.tasky.core.network.retrofit

import com.douglasbruce.tasky.BuildConfig
import com.douglasbruce.tasky.core.common.auth.AuthResult
import com.douglasbruce.tasky.core.common.auth.asAuthResult
import com.douglasbruce.tasky.core.common.utils.MoshiSerializer
import com.douglasbruce.tasky.core.common.utils.UiText
import com.douglasbruce.tasky.core.network.TaskyNetworkDataSource
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
import com.squareup.moshi.Moshi
import okhttp3.Call
import okhttp3.MultipartBody
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private const val TASKY_BASE_URL = BuildConfig.BACKEND_URL

@Singleton
class RetrofitTaskyNetwork @Inject constructor(
    moshi: Moshi,
    okHttpCallFactory: Call.Factory,
) : TaskyNetworkDataSource {

    private val networkApi =
        Retrofit.Builder()
            .baseUrl(TASKY_BASE_URL)
            .callFactory(okHttpCallFactory)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(RetrofitTaskyNetworkApi::class.java)

    override suspend fun login(email: String, password: String): NetworkUser {
        val request = LoginRequest(
            email = email,
            password = password
        )
        return networkApi.login(request)
    }

    override suspend fun register(fullName: String, email: String, password: String) {
        val request = RegisterRequest(
            fullName = fullName,
            email = email,
            password = password
        )
        networkApi.register(request)
    }

    override suspend fun authenticate() {
        networkApi.authenticate()
    }

    override suspend fun logout() {
        networkApi.logout()
    }

    override suspend fun getAgenda(timeZone: String, time: Long): NetworkAgenda {
        return networkApi.getAgenda(timeZone, time)
    }

    override suspend fun syncAgenda(request: SyncAgendaRequest) {
        networkApi.syncAgenda(request)
    }

    override suspend fun getFullAgenda(): NetworkAgenda {
        return networkApi.getFullAgenda()
    }

    override suspend fun getEvent(eventId: String): NetworkEvent {
        return networkApi.getEvent(eventId)
    }

    override suspend fun createEvent(
        createEventRequest: MultipartBody.Part,
        photos: List<MultipartBody.Part>
    ): NetworkEvent {
        return networkApi.createEvent(createEventRequest, photos)
    }

    override suspend fun updateEvent(
        updateEventRequest: MultipartBody.Part,
        photos: List<MultipartBody.Part>
    ): NetworkEvent {
        return networkApi.updateEvent(updateEventRequest, photos)
    }

    override suspend fun deleteEvent(eventId: String) {
        networkApi.deleteEvent(eventId)
    }

    override suspend fun getTask(taskId: String): NetworkTask {
        return networkApi.getTask(taskId)
    }

    override suspend fun createTask(createTaskRequest: CreateTaskRequest) {
        networkApi.createTask(createTaskRequest)
    }

    override suspend fun updateTask(updateTaskRequest: UpdateTaskRequest) {
        networkApi.updateTask(updateTaskRequest)
    }

    override suspend fun deleteTask(taskId: String) {
        networkApi.deleteTask(taskId)
    }

    override suspend fun getReminder(reminderId: String): NetworkReminder {
        return networkApi.getReminder(reminderId)
    }

    override suspend fun createReminder(createReminderRequest: CreateReminderRequest) {
        networkApi.createReminder(createReminderRequest)
    }

    override suspend fun updateReminder(updateReminderRequest: UpdateReminderRequest) {
        networkApi.updateReminder(updateReminderRequest)
    }

    override suspend fun deleteReminder(reminderId: String) {
        networkApi.deleteReminder(reminderId)
    }

    override suspend fun getAttendee(email: String): NetworkAttendeeCheck {
        return networkApi.getAttendee(email)
    }

    override suspend fun leaveEvent(eventId: String) {
        networkApi.leaveEvent(eventId)
    }
}

suspend inline fun <T> authenticatedRetrofitCall(
    serializer: MoshiSerializer,
    crossinline doCall: suspend () -> AuthResult<T>
): AuthResult<T> {
    return try {
        doCall()
    } catch (e: HttpException) {
        e.printStackTrace()
        e.asAuthResult(serializer)
    } catch (e: IOException) {
        e.printStackTrace()
        AuthResult.Error(UiText.UnknownError)
    }
}