package com.douglasbruce.tasky.features.event

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.douglasbruce.tasky.core.common.auth.AuthResult
import com.douglasbruce.tasky.core.common.utils.DateUtils
import com.douglasbruce.tasky.core.common.utils.UiText
import com.douglasbruce.tasky.core.domain.datastore.UserDataPreferences
import com.douglasbruce.tasky.core.domain.repository.EventRepository
import com.douglasbruce.tasky.core.model.AgendaItem
import com.douglasbruce.tasky.core.model.AgendaPhoto
import com.douglasbruce.tasky.core.model.NotificationType
import com.douglasbruce.tasky.features.event.form.EventFormEvent
import com.douglasbruce.tasky.features.event.form.EventState
import com.douglasbruce.tasky.features.event.navigation.EventArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.UUID
import javax.inject.Inject

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class EventViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    userDataPreferences: UserDataPreferences,
    private val eventRepository: EventRepository,
) : ViewModel() {

    private val eventArgs: EventArgs = EventArgs(savedStateHandle)
    private val localUserId: Flow<String> = userDataPreferences.userData.map { it.userId }

    private val infoChannel = Channel<UiText>()
    val infoMessages = infoChannel.receiveAsFlow()

    var state by savedStateHandle.saveable {
        mutableStateOf(
            EventState(
                id = eventArgs.eventId,
                fromDate = if (eventArgs.eventId == null) DateUtils.getLocalDate(eventArgs.eventFromDateMilli) else LocalDate.now(),
                toDate = if (eventArgs.eventId == null) DateUtils.getLocalDate(eventArgs.eventFromDateMilli) else LocalDate.now(),
                isEditing = eventArgs.eventIsEditing,
            )
        )
    }
        private set

    init {
        state.id?.let { id ->
            getEvent(id)
        }
    }

    fun onEvent(event: EventFormEvent) {
        when (event) {
            is EventFormEvent.ToggleEditMode -> {
                state = state.copy(isEditing = !state.isEditing)
            }

            is EventFormEvent.OnEditorSave -> {
                if (state.isLoading) {
                    return
                }

                val title = event.title.ifBlank { state.title }
                val desc = event.description.ifBlank { state.description }

                state = state.copy(title = title, description = desc)
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
                viewModelScope.launch {
                    val userId = localUserId.first()
                    val eventId = state.id ?: UUID.randomUUID().toString()

                    val eventItem = AgendaItem.Event(
                        eventId = eventId,
                        eventTitle = state.title ?: "",
                        eventDescription = state.description,
                        from = ZonedDateTime.of(
                            state.fromDate,
                            state.fromTime,
                            ZoneId.systemDefault()
                        ),
                        to = ZonedDateTime.of(
                            state.toDate,
                            state.toTime,
                            ZoneId.systemDefault()
                        ),
                        remindAtTime = NotificationType.notificationTypeToZonedDateTime(
                            state.fromDate,
                            state.fromTime,
                            state.notificationType
                        ),
                        eventNotificationType = state.notificationType,
                        host = if (state.isNew) userId else state.host,
                        isUserEventCreator = if (state.isNew) true else state.isUserEventCreator,
                        photos = state.photos,
                        attendees = state.attendees
                    )

                    val result = when (state.isNew) {
                        true -> eventRepository.createEvent(eventItem)
                        false -> eventRepository.updateEvent(eventItem, emptyList())
                    }

                    when (result) {
                        is AuthResult.Success -> {
                            state = state.copy(closeScreen = true)
                        }

                        is AuthResult.Error -> {
                            result.message?.let {
                                infoChannel.send(result.message)
                            }
                        }

                        else -> {}
                    }
                }
            }

            is EventFormEvent.ToggleDeleteConfirmationClick -> {
                state = state.copy(showDeleteConfirmation = event.show)
            }

            is EventFormEvent.OnDeleteEventClick -> {
                state.id?.let { id ->
                    viewModelScope.launch {
                        when (eventRepository.deleteEventById(id)) {
                            is AuthResult.Success -> {
                                state = state.copy(closeScreen = true)
                            }

                            is AuthResult.Error -> {
                                state = state.copy(closeScreen = true)
                            }

                            is AuthResult.Unauthorized -> {
                                state.copy(logout = true)
                            }

                            is AuthResult.Error -> {
                                state.copy(closeScreen = true)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getEvent(id: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            when (val result = eventRepository.getEventById(id)) {
                is AuthResult.Success -> {
                    result.data?.let { event ->
                        state = state.copy(
                            title = event.eventTitle,
                            description = event.eventDescription,
                            toTime = event.to.toLocalTime(),
                            toDate = event.to.toLocalDate(),
                            fromTime = event.from.toLocalTime(),
                            fromDate = event.from.toLocalDate(),
                            notificationType = event.eventNotificationType,
                            photos = event.photos,
                            attendees = event.attendees,
                            isUserEventCreator = event.isUserEventCreator,
                            isLoading = false,
                        )
                    }
                }

                is AuthResult.Error -> {
                    result.message?.let {
                        infoChannel.send(result.message)
                    }
                    state = state.copy(isLoading = false)
                }

                else -> {}
            }
        }
    }
}