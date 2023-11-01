package com.douglasbruce.tasky.features.event

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.douglasbruce.tasky.core.common.auth.AuthResult
import com.douglasbruce.tasky.core.common.utils.DateUtils
import com.douglasbruce.tasky.core.domain.datastore.UserDataPreferences
import com.douglasbruce.tasky.core.domain.repository.EventRepository
import com.douglasbruce.tasky.core.model.AgendaItem
import com.douglasbruce.tasky.core.model.AgendaPhoto
import com.douglasbruce.tasky.core.model.NotificationType
import com.douglasbruce.tasky.features.event.form.EventFormEvent
import com.douglasbruce.tasky.features.event.form.EventState
import com.douglasbruce.tasky.features.event.navigation.EventArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.UUID
import javax.inject.Inject

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class EventViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val eventRepository: EventRepository,
    private val userDataPreferences: UserDataPreferences,
) : ViewModel() {

    private val eventArgs: EventArgs = EventArgs(savedStateHandle)
    private val localUserId: String
        get() = runBlocking { userDataPreferences.userData.map { it.userId }.first() }

    var state by savedStateHandle.saveable {
        mutableStateOf(
            EventState(
                id = eventArgs.eventId,
                fromDate = DateUtils.getLocalDate(eventArgs.eventFromDateMilli),
                toDate = DateUtils.getLocalDate(eventArgs.eventToDateMilli),
            )
        )
    }
        private set

    fun onEvent(event: EventFormEvent) {
        when (event) {
            is EventFormEvent.ToggleEditMode -> {
                state = state.copy(isEditing = !state.isEditing)
            }

            is EventFormEvent.OnEditorSave -> {
                state = state.copy(title = event.title, description = event.description)
            }

            is EventFormEvent.OnHideTimePicker -> {
                state = state.copy(showTimePicker = false)
            }

            is EventFormEvent.OnHideDatePicker -> {
                state = state.copy(showDatePicker = false)
            }

            is EventFormEvent.OnTimePickerClick -> {
                state = state.copy(
                    isEditingToTime = event.isToTime,
                    showTimePicker = true,
                )
            }

            is EventFormEvent.OnTimeSelected -> {
                val localTime: LocalTime = LocalTime.of(event.hour, event.minute)

                state = if (state.isEditingToTime) {
                    state.copy(
                        toTime = localTime,
                        showTimePicker = false,
                    )
                } else {
                    val truncatedLocalTime = localTime.truncatedTo(ChronoUnit.MINUTES)
                    val truncatedToTime = state.toTime.truncatedTo(ChronoUnit.MINUTES)

                    val toTime =
                        if (truncatedLocalTime == truncatedToTime || truncatedLocalTime.isAfter(
                                truncatedToTime
                            )
                        ) {
                            localTime.plusMinutes(30)
                        } else {
                            state.toTime
                        }

                    state.copy(
                        fromTime = localTime,
                        toTime = toTime,
                        showTimePicker = false,
                    )
                }
            }

            is EventFormEvent.OnDatePickerClick -> {
                state = state.copy(
                    isEditingToDate = event.isToDate,
                    showDatePicker = true,
                )
            }

            is EventFormEvent.OnDateSelected -> {
                val localDate = DateUtils.getLocalDate(event.dateMillis)

                state = if (state.isEditingToDate) {
                    state.copy(
                        toDate = localDate,
                        showDatePicker = false,
                    )
                } else {
                    val toDate = if (localDate.isAfter(state.toDate)) {
                        localDate
                    } else {
                        state.toDate
                    }

                    state.copy(
                        fromDate = localDate,
                        toDate = toDate,
                        showDatePicker = false,
                    )
                }
            }

            is EventFormEvent.OnNotificationTypeSelection -> {
                state = state.copy(
                    notificationType = event.notificationType
                )
            }

            is EventFormEvent.OnVisitorFilterTypeSelection -> {
                state = state.copy(
                    visitorFilterType = event.visitorFilterType
                )
            }

            is EventFormEvent.OnAddPhotoClick -> {
                val localPhotos = mutableListOf<AgendaPhoto>()
                event.uris.forEach { uri ->
                    val local = AgendaPhoto.Local(uri = uri)
                    localPhotos.add(local)
                }

                state = state.copy(
                    photos = state.photos + localPhotos
                )
            }

            is EventFormEvent.OnRemovePhotoClick -> {
                state = state.copy(
                    photos = state.photos.filter { it.key() != event.key }
                )
            }

            is EventFormEvent.OnSaveClick -> {
                val eventItem = AgendaItem.Event(
                    eventId = state.id ?: UUID.randomUUID().toString(),
                    eventTitle = state.title ?: "",
                    eventDescription = state.description,
                    from = ZonedDateTime.of(
                        state.fromDate,
                        state.fromTime,
                        ZonedDateTime.now().zone
                    ),
                    to = ZonedDateTime.of(
                        state.toDate,
                        state.toTime,
                        ZonedDateTime.now().zone
                    ),
                    remindAtTime = NotificationType.notificationTypeToZonedDateTime(
                        state.fromDate,
                        state.fromTime,
                        state.notificationType
                    ),
                    eventNotificationType = state.notificationType,
                    host = if (state.isNew) localUserId else state.host,
                    isUserEventCreator = if (state.isNew) true else state.isUserEventCreator,
                    photos = state.photos
                )

                viewModelScope.launch {
                    val result = when (state.isNew) {
                        true -> eventRepository.createEvent(eventItem)
                        false -> eventRepository.updateEvent(eventItem, emptyList())
                    }

                    if (result is AuthResult.Success) {
                        //TODO: Navigate back to agenda screen
                    } else if (result is AuthResult.Error) {
                        //TODO: Report errors
                    }
                    //TODO: Logout if unauthorized
                }
            }
        }
    }
}