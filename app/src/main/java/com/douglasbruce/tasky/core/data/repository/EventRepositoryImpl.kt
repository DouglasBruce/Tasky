package com.douglasbruce.tasky.core.data.repository

import android.net.Uri
import com.douglasbruce.tasky.R
import com.douglasbruce.tasky.core.common.auth.AuthResult
import com.douglasbruce.tasky.core.common.utils.MoshiSerializer
import com.douglasbruce.tasky.core.common.utils.UiText
import com.douglasbruce.tasky.core.data.database.dao.EventDao
import com.douglasbruce.tasky.core.data.database.model.ModifiedAgendaItemEntity
import com.douglasbruce.tasky.core.domain.datastore.UserDataPreferences
import com.douglasbruce.tasky.core.domain.mapper.toAlarmItem
import com.douglasbruce.tasky.core.domain.mapper.toCreateEventRequest
import com.douglasbruce.tasky.core.domain.mapper.toEvent
import com.douglasbruce.tasky.core.domain.mapper.toEventEntity
import com.douglasbruce.tasky.core.domain.mapper.toUpdateEventRequest
import com.douglasbruce.tasky.core.domain.repository.EventRepository
import com.douglasbruce.tasky.core.domain.utils.AlarmScheduler
import com.douglasbruce.tasky.core.domain.utils.PhotoByteConverter
import com.douglasbruce.tasky.core.domain.utils.PhotoExtensionParser
import com.douglasbruce.tasky.core.model.AgendaItem
import com.douglasbruce.tasky.core.model.AgendaItemType
import com.douglasbruce.tasky.core.model.ModificationType
import com.douglasbruce.tasky.core.network.model.NetworkAttendeeCheck
import com.douglasbruce.tasky.core.network.model.request.CreateEventRequest
import com.douglasbruce.tasky.core.network.model.request.UpdateEventRequest
import com.douglasbruce.tasky.core.network.retrofit.RetrofitTaskyNetwork
import com.douglasbruce.tasky.core.network.retrofit.authenticatedRetrofitCall
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.UUID
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    userDataPreferences: UserDataPreferences,
    private val taskyNetwork: RetrofitTaskyNetwork,
    private val dao: EventDao,
    private val serializer: MoshiSerializer,
    private val alarmScheduler: AlarmScheduler,
    private val photoByteConvertor: PhotoByteConverter,
    private val photoExtensionParser: PhotoExtensionParser,
) : EventRepository {

    private val localUserId: Flow<String> = userDataPreferences.userData.map { it.userId }

    override suspend fun getEventById(eventId: String): AuthResult<AgendaItem.Event> {
        return authenticatedRetrofitCall(serializer) {
            dao.getEventById(eventId)?.toEvent()?.let {
                AuthResult.Success(data = it)
            } ?: AuthResult.Error(UiText.StringResource(R.string.event_not_found))
        }
    }

    override suspend fun createEvent(event: AgendaItem.Event): AuthResult<Unit> {
        dao.upsertEvent(event.toEventEntity())
        alarmScheduler.schedule(event.toAlarmItem())

        val requestJson =
            serializer.toJson(event.toCreateEventRequest(), CreateEventRequest::class.java)

        val result = authenticatedRetrofitCall(serializer) {
            val photos = supervisorScope {
                event.photos.mapIndexed { index, agendaPhoto ->
                    async {
                        val parsedUri = Uri.parse(agendaPhoto.uri())
                        val bytes = photoByteConvertor.uriToBytes(parsedUri)
                        val extension = photoExtensionParser.extensionFromUri(parsedUri)

                        MultipartBody.Part.createFormData(
                            name = "photo$index",
                            filename = UUID.randomUUID().toString() + "." + extension,
                            body = bytes.toRequestBody()
                        )
                    }
                }.map {
                    it.await()
                }
            }

            val networkEvent = taskyNetwork.createEvent(
                createEventRequest = MultipartBody.Part.createFormData(
                    "create_event_request",
                    requestJson!!
                ),
                photos = photos
            )

            withContext(NonCancellable) {
                dao.upsertEvent(networkEvent.toEvent().toEventEntity())
            }
            AuthResult.Success(Unit)
        }

        return if (result is AuthResult.Error) {
            withContext(NonCancellable) {
                dao.insertModifiedEvent(
                    ModifiedAgendaItemEntity(
                        id = event.id,
                        agendaItemType = AgendaItemType.Event,
                        modificationType = ModificationType.Created
                    )
                )
            }
            result
        } else result
    }

    override suspend fun updateEvent(
        event: AgendaItem.Event,
        deletedRemotePhotoKeys: List<String>,
    ): AuthResult<Unit> {
        val userId = localUserId.first()

        dao.upsertEvent(event.toEventEntity())
        alarmScheduler.schedule(event.toAlarmItem())

        val requestJson = serializer.toJson(
            event.toUpdateEventRequest(
                deletedPhotoKeys = deletedRemotePhotoKeys,
                isGoing = event.getAttendee(userId)?.isGoing == true
            ), UpdateEventRequest::class.java
        )

        val result = authenticatedRetrofitCall(serializer) {
            val photos = supervisorScope {
                event.photos.mapIndexed { index, agendaPhoto ->
                    async {
                        val parsedUri = Uri.parse(agendaPhoto.uri())
                        val bytes = photoByteConvertor.uriToBytes(parsedUri)
                        val extension = photoExtensionParser.extensionFromUri(parsedUri)

                        MultipartBody.Part.createFormData(
                            name = "photo$index",
                            filename = UUID.randomUUID().toString() + "." + extension,
                            body = bytes.toRequestBody()
                        )
                    }
                }.map {
                    it.await()
                }
            }

            val networkEvent = taskyNetwork.updateEvent(
                updateEventRequest = MultipartBody.Part.createFormData(
                    "update_event_request",
                    requestJson!!
                ),
                photos = photos
            )

            withContext(NonCancellable) {
                dao.upsertEvent(networkEvent.toEvent().toEventEntity())
            }
            AuthResult.Success(Unit)
        }

        return if (result is AuthResult.Error) {
            withContext(NonCancellable) {
                dao.insertModifiedEvent(
                    ModifiedAgendaItemEntity(
                        id = event.id,
                        agendaItemType = AgendaItemType.Event,
                        modificationType = ModificationType.Updated
                    )
                )
            }
            result
        } else result
    }

    override suspend fun deleteEventById(eventId: String): AuthResult<Unit> {
        val result = authenticatedRetrofitCall(serializer) {
            dao.deleteEventById(eventId)
            alarmScheduler.cancel(eventId)
            taskyNetwork.deleteEvent(eventId)
            AuthResult.Success(Unit)
        }
        return if (result is AuthResult.Error) {
            withContext(NonCancellable) {
                dao.insertModifiedEvent(
                    ModifiedAgendaItemEntity(
                        id = eventId,
                        agendaItemType = AgendaItemType.Event,
                        modificationType = ModificationType.Deleted
                    )
                )
            }
            result
        } else result
    }

    override suspend fun getFutureEvents(): List<AgendaItem.Event> {
        return dao.getFutureEvents().map { it.toEvent() }
    }

    override suspend fun getAttendee(email: String): AuthResult<NetworkAttendeeCheck> {
        return authenticatedRetrofitCall(serializer) {
            AuthResult.Success(data = taskyNetwork.getAttendee(email))
        }
    }

    override suspend fun leaveEvent(eventId: String): AuthResult<Unit> {
        return authenticatedRetrofitCall(serializer) {
            taskyNetwork.leaveEvent(eventId)
            withContext(NonCancellable) {
                dao.leaveEvent(eventId)
            }
            AuthResult.Success(Unit)
        }
    }
}